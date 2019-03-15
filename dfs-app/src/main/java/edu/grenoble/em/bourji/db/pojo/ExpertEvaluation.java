package edu.grenoble.em.bourji.db.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Moe on 5/7/18.
 */
@Entity
@Table(name = "EXPERT_EVAL")
@IdClass(ExpertEvaluation.UniqueIdentifier.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpertEvaluation {
    @Id
    @Column(name = "ID", nullable = false)
    private String id;
    @Id
    @Column(name = "EVAL_CODE", nullable = false)
    private String evalCode;
    @Column(name = "STD_LEARNING", nullable = false)
    private double studentLearning;
    @Column(name = "INST_PRACTICE", nullable = false)
    private double instructionalPractice;
    @Column(name = "PROFESSIONALISM", nullable = false)
    private double professionalism;
    @Column(name = "OVERALL", nullable = false)
    private double overall;

    public ExpertEvaluation() {
        // Default no-arg constructor for hibernate
    }

    public ExpertEvaluation(String id, String evalCode, double studentLearning, double instructionalPractice, double professionalism, double overall) {
        this.id = id;
        this.evalCode = evalCode;
        this.studentLearning = studentLearning;
        this.instructionalPractice = instructionalPractice;
        this.professionalism = professionalism;
        this.overall = overall;
    }

    public String getId() {
        return id;
    }

    public String getEvalCode() {
        return evalCode;
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

    public static class UniqueIdentifier implements Serializable {
        private String id;
        private String evalCode;

        public UniqueIdentifier() {
            // default no-arg constructor for hibernate
        }

        public String getId() {
            return id;
        }

        public String getEvalCode() {
            return evalCode;
        }

        @Override
        public boolean equals(Object o) {
            if(o == null) return false;
            if(!(o instanceof UniqueIdentifier)) return false;
            UniqueIdentifier that = (UniqueIdentifier) o;
            return this.id.equals(that.id) && this.evalCode.equals(that.evalCode);
        }

        @Override
        public int hashCode() {
            int hashCode = 5;
            return 37 * hashCode + (
                    this.id.hashCode() + this.evalCode.hashCode()
            );
        }
    }
}