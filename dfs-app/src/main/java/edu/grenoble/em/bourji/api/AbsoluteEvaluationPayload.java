package edu.grenoble.em.bourji.api;

import edu.grenoble.em.bourji.db.pojo.EvaluationActivity;
import edu.grenoble.em.bourji.db.pojo.AbsoluteEvaluation;

import java.util.List;

/**
 * Created by Moe on 4/11/18.
 */
public class AbsoluteEvaluationPayload {

    private AbsoluteEvaluation recommendation;
    private List<EvaluationActivity> activities;
    private String datetimeIn;
    private String datetimeOut;
    private String mode;

    public AbsoluteEvaluationPayload() {
        // no-arg default constructor for jackson
    }

    public AbsoluteEvaluation getRecommendation() {
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
