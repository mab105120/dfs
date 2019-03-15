package edu.grenoble.em.bourji.db.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Moe on 9/18/17.
 */
@Entity
@Table(name = "EVALUATION")
@IdClass(RelativeEvaluation.UniqueIdentifier.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelativeEvaluation {

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    private String user;
    @Id
    @Column(name = "EVAL_CODE", nullable = false, length = 5)
    private String evaluationCode;
    @Id
    @Column(name = "SUBMISSION_ID", nullable = false)
    private int submissionId;
    @Column(name = "RECOMMENDATION_PICK", nullable = false, length = 5)
    private String recommendationPick;
    @Column(name = "ABS_CONFIDENCE", nullable = false)
    private int absConfidence;
    @Column(name = "REL_CONFIDENCE", nullable = false)
    private int relConfidence;
    @Column(name = "COMMENT", nullable = false, length = 250)
    private String comment;
    @Column(name ="OPEN_TIME", nullable = false, length = 30)
    private String openTime;
    @Column(name = "CLOSE_TIME", nullable = false, length = 30)
    private String closeTime;

    public RelativeEvaluation() {
        // no-arg default constructor for hibernate
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public String getEvaluationCode() {
        return evaluationCode;
    }

    public String getRecommendationPick() {
        return recommendationPick;
    }

    public int getAbsConfidence() {
        return absConfidence;
    }

    public int getRelConfidence() {
        return relConfidence;
    }

    public String getComment() {
        return comment;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public static class UniqueIdentifier implements Serializable {

        private String user;
        private String evaluationCode;
        private int submissionId;

        public UniqueIdentifier() {
            // no-arg constructor for hibernate
        }

        public String getUser() {
            return user;
        }

        public String getEvalulationCode() {
            return evaluationCode;
        }

        public int getSubmissionId() {
            return submissionId;
        }

        @Override
        public boolean equals(Object o) {
            if(o == null) return false;
            if(!(o instanceof UniqueIdentifier))
                return false;
            UniqueIdentifier that = (UniqueIdentifier) o;
            return this.user.equals(that.user)
                    && this.evaluationCode.equals(that.evaluationCode)
                    && this.submissionId == that.submissionId;
        }

        @Override
        public int hashCode() {
            int hashCode = 5;
            return 37 * hashCode + (
                    this.user.hashCode() + this.evaluationCode.hashCode() + this.submissionId
            );
        }
    }

}
