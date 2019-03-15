package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.Authenticate;
import edu.grenoble.em.bourji.db.dao.AppraisalConfidenceDAO;
import edu.grenoble.em.bourji.db.dao.QuestionnaireDAO;
import edu.grenoble.em.bourji.db.pojo.RelativeEvaluation;
import edu.grenoble.em.bourji.db.pojo.UserConfidence;
import io.dropwizard.hibernate.UnitOfWork;
import org.joda.time.DateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Moe on 4/10/2018.
 */
@Path("/validate")
@Authenticate
public class ValidationResource {

    private final AppraisalConfidenceDAO appraisalConfidenceDAO;
    private final QuestionnaireDAO questionnaireDAO;

    public ValidationResource(AppraisalConfidenceDAO appraisalConfidenceDAO, QuestionnaireDAO questionnaireDAO) {
        this.appraisalConfidenceDAO = appraisalConfidenceDAO;
        this.questionnaireDAO = questionnaireDAO;
    }

    // TODO: Implement a validation method
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAverageDurationPerEvaluation(@Context ContainerRequestContext requestContext) {
        String userId = requestContext.getProperty("user").toString();
        List<RelativeEvaluation> recommendations = appraisalConfidenceDAO.getAllRecommendations(userId);
        boolean passedDurationTest = averageDurationAboveThreshold(recommendations);
        boolean passedAttentionTest = passedAttentionTest(userId);
        if (!passedDurationTest)
            return Response.ok(new ValidationResult(false, "duration")).build();
        else if (!passedAttentionTest)
            return Response.ok(new ValidationResult(false, "attention")).build();
        return Response.ok(new ValidationResult(true, null)).build();
    }

    private boolean passedAttentionTest(String userId) {
        List<UserConfidence> response = this.questionnaireDAO.getUserConfidenceDAO().getUserConfidence(userId);
        int jd2 = 0, jd9 = 0;
        for (UserConfidence r : response)
            if (r.getItemCode().equals("jsd2"))
                jd2 = r.getResponse();
            else if (r.getItemCode().equals("jsd9"))
                jd9 = r.getResponse();
        return jd2 == jd9 * -1;
    }

    private boolean averageDurationAboveThreshold(List<RelativeEvaluation> recommendations) {
        Map<String, Double> durationPerEvaluation = new HashMap<>();
        for (RelativeEvaluation recommendation : recommendations) {
            String evalCode = recommendation.getEvaluationCode();
            double duration = (DateTime.parse(recommendation.getCloseTime()).getMillis() - DateTime.parse(recommendation.getOpenTime()).getMillis()) / 1000.00;
            if (durationPerEvaluation.get(evalCode) == null)
                durationPerEvaluation.put(evalCode, duration);
            else if (durationPerEvaluation.get(evalCode) < duration)
                durationPerEvaluation.put(evalCode, duration);
        }
        double totalDuration = 0.0;
        for (Map.Entry<String, Double> entry : durationPerEvaluation.entrySet())
            totalDuration += entry.getValue();
        double averageDuration = totalDuration / 9.0;
        return averageDuration > 60;
    }

    private class ValidationResult {
        private boolean result;
        private String term;

        ValidationResult(boolean result, String term) {
            this.result = result;
            this.term = term;
        }

        public boolean isResult() {
            return result;
        }

        public String getTerm() {
            return term;
        }
    }
}