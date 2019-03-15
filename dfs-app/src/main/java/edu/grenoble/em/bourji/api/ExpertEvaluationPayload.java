package edu.grenoble.em.bourji.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Moe on 5/1/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpertEvaluationPayload {
    private String evaluationCode;
    private double studentLearning;
    private double instructionalPractice;
    private double professionalism;
    private double overall;

    public ExpertEvaluationPayload() {
        // default no-arg constructor for Jackson
    }

    public ExpertEvaluationPayload(String evaluationCode, double studentLearning, double instructionalPractice, double professionalism, double overall) {
        this.evaluationCode = evaluationCode;
        this.studentLearning = studentLearning;
        this.instructionalPractice = instructionalPractice;
        this.professionalism = professionalism;
        this.overall = overall;
    }

    public static ExpertEvaluationPayload getRandomResponse(String evaluationCode, double sl, double ip, double pf) {
        double randomSl, randomIp, randomPf, overall;
        randomSl = round(getRandom(sl - 1, sl + 1));
        randomIp = round(getRandom(ip - 1, ip + 1));
        randomPf = round(getRandom(pf - 1, pf + 1));
        overall = round((randomSl + randomIp + randomPf) / 3);
        return new ExpertEvaluationPayload(evaluationCode, randomSl, randomIp, randomPf, overall);
    }

    private static double round(double d) {
        return Math.round(d * 10) / 10.0;
    }

    private static double getRandom(double lo, double hi) {
        if (lo < 1) lo = 1;
        if (hi > 7) hi = 7;
        return lo + Math.random() * (hi - lo);
    }

    public String getEvaluationCode() {
        return evaluationCode;
    }

    public double getStudentLearning() {
        return studentLearning;
    }

    public double getInstructionalPractice() {
        return instructionalPractice;
    }

    public double getProfessionalism() {
        return professionalism;
    }

    public double getOverall() {
        return overall;
    }
}
