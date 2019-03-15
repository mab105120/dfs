package edu.grenoble.em.bourji.db.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Moe on 9/28/17.
 */
@Entity
@Table(name = "EVAL_ACTIVITY")
@IdClass(EvaluationActivity.UniqueIdentifier.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EvaluationActivity {

    @Id
    @Column(name = "ID", length = 64, nullable = false)
    private String user;
    @Column(name = "EVAL_CODE", length = 10, nullable = false)
    private String evaluationCode;
    @Column(name = "REVIEW_CODE", length = 200, nullable = false)
    private String selectedReview;
    @Id
    @Column(name = "OPEN_TIME", length = 30, nullable = false) // 2017-09-29T03:44:54.656
    private String openTime;
    @Column(name = "CLOSE_TIME", length = 30, nullable = false)
    private String closeTime;
    @Column(name = "SUBMISSION_ID", nullable = false)
    private int submissionId;

    public EvaluationActivity() {
        // default no-arg constructor for jackson
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEvaluationCode() {
        return evaluationCode;
    }

    public String getSelectedReview() {
        return selectedReview;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    @Override
    public String toString() {
        return String.format("{evaluationCode: %s, selectedReview: %s, openTime: %s, closeTime: %s}",
                evaluationCode, selectedReview, openTime, closeTime);
    }

    public static class UniqueIdentifier implements Serializable {

        private String user;
        private String openTime;

        public UniqueIdentifier() {
            // no-arg default constructor for hibernate
        }

        public String getUser() {
            return user;
        }

        public String getOpenTime() {
            return openTime;
        }

        @Override
        public boolean equals(Object o) {
            if(o == null) return false;
            if(!(o instanceof  UniqueIdentifier)) return false;
            UniqueIdentifier that = (UniqueIdentifier) o;
            return this.user.equals(that.user) && this.openTime.equals(that.openTime);
        }

        @Override
        public int hashCode() {
            int hashCode = 5;
            return 37 * hashCode + (
                    this.user.hashCode() + this.openTime.hashCode()
            );
        }
    }
}