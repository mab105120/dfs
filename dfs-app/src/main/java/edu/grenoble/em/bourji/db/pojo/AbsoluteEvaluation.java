package edu.grenoble.em.bourji.db.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.persistence.IdClass;
import java.io.Serializable;

/**
 * Created by Moe on 4/11/18.
 */
@Entity
@Table(name = "EVALUATION")
@IdClass(AbsoluteEvaluation.UniqueIdentifier.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbsoluteEvaluation {

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    private String user;
    @Id
    @Column(name = "EVAL_CODE", nullable = false, length = 5)
    private String evaluationCode;
    @Id
    @Column(name = "SUBMISSION_ID", nullable = false)
    private int submissionId;
    @Column(name = "STD_LEARNING", nullable = false)
    private int studentLearning;
    @Column(name = "INST_PRACTICE", nullable = false)
    private int instructionalPractice;
    @Column(name = "PROFESSIONALISM", nullable = false)
    private int professionalism;
    @Column(name = "OVERALL", nullable = false)
    private int overall;
    @Column(name = "PROMOTE", nullable = false)
    private String promote;
    @Column(name = "OPEN_TIME", nullable = false)
    private String timeIn;
    @Column(name = "CLOSE_TIME", nullable = false)
    private String timeOut;

    public AbsoluteEvaluation() {
        // no-arg default constructor for hibernate
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

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public int getStudentLearning() {
        return studentLearning;
    }

    public int getInstructionalPractice() {
        return instructionalPractice;
    }

    public int getProfessionalism() {
        return professionalism;
    }

    public int getOverall() {
        return overall;
    }

    public String getPromote() {
        return promote;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
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
            if(!(o instanceof AbsoluteEvaluation.UniqueIdentifier))
                return false;
            AbsoluteEvaluation.UniqueIdentifier that = (AbsoluteEvaluation.UniqueIdentifier) o;
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