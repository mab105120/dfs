package edu.grenoble.em.bourji.api;

import edu.grenoble.em.bourji.ExperimentMode;

/**
 * Created by Moe on 4/18/18.
 */
public class ParticipantProfile {

    private boolean isRelative;
    private boolean includeConfidenceScale;
    private boolean offerTraining;
    private int practice;
    private int evaluations;
    private String feedback;
    private int duration;
    private ExperimentMode mode;

    public ParticipantProfile() {
        // Default no-arg constructor for Jackson
    }

    public ParticipantProfile( boolean isRelative, boolean includeConfidenceScale, int practice, int evaluations,
                               String feedback, int duration, ExperimentMode mode) {
        this.isRelative = isRelative;
        this.includeConfidenceScale = includeConfidenceScale;
        this.practice = practice;
        this.evaluations = evaluations;
        this.feedback = feedback;
        this.duration = duration;
        this.mode = mode;
        this.offerTraining = false;
    }

    public ParticipantProfile( boolean isRelative, boolean includeConfidenceScale, boolean offerTraining, int practice, int evaluations,
                               String feedback, int duration, ExperimentMode mode) {
        this.isRelative = isRelative;
        this.includeConfidenceScale = includeConfidenceScale;
        this.offerTraining = offerTraining;
        this.practice = practice;
        this.evaluations = evaluations;
        this.feedback = feedback;
        this.duration = duration;
        this.mode = mode;
    }

    public boolean isRelative() {
        return isRelative;
    }

    public boolean isIncludeConfidenceScale() {
        return includeConfidenceScale;
    }

    public int getPractice() {
        return practice;
    }

    public int getEvaluations() {
        return evaluations;
    }

    public String getFeedback() {
        return feedback;
    }

    public int getDuration() {
        return duration;
    }

    public ExperimentMode getMode() {
        return mode;
    }

    public boolean isOfferTraining() {
        return offerTraining;
    }
}
