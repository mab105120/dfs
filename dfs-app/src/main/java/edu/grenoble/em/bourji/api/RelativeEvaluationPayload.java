package edu.grenoble.em.bourji.api;

import edu.grenoble.em.bourji.db.pojo.EvaluationActivity;
import edu.grenoble.em.bourji.db.pojo.RelativeEvaluation;

import java.util.List;

/**
 * Created by Moe on 9/28/17.
 */
public class RelativeEvaluationPayload {

    private RelativeEvaluation recommendation;
    private List<EvaluationActivity> activities;
    private String datetimeIn;
    private String datetimeOut;
    private String mode;

    public RelativeEvaluationPayload() {
        // no-arg default constructor for jackson
    }

    public RelativeEvaluation getRecommendation() {
        return recommendation;
    }

    public List<EvaluationActivity> getActivities() {
        return activities;
    }

    public String getDatetimeIn() {
        return datetimeIn;
    }

    public String getDatetimeOut() {
        return datetimeOut;
    }

    public String getMode() {
        return mode;
    }
}
