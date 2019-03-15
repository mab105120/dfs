package edu.grenoble.em.bourji.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Moe on 8/29/17.
 */
public class TeacherDossier {

    @JsonProperty("SL")
    private JobFunctionReview studentLearning;
    @JsonProperty("IP")
    private JobFunctionReview instructionalPractice;
    @JsonProperty("PF")
    private JobFunctionReview professionalism;

    public TeacherDossier() {
        // default no-arg constructor for jackson
    }

    public TeacherDossier(JobFunctionReview studentLearning,
                          JobFunctionReview instructionalPractice,
                          JobFunctionReview professionalism) {
        this.studentLearning = studentLearning;
        this.instructionalPractice = instructionalPractice;
        this.professionalism = professionalism;
    }

    public JobFunctionReview getStudentLearning() {
        return studentLearning;
    }

    public JobFunctionReview getInstructionalPractice() {
        return instructionalPractice;
    }

    public JobFunctionReview getProfessionalism() {
        return professionalism;
    }

    public void setStudentLearning(JobFunctionReview studentLearning) {
        this.studentLearning = studentLearning;
    }

    public void setInstructionalPractice(JobFunctionReview instructionalPractice) {
        this.instructionalPractice = instructionalPractice;
    }

    public void setProfessionalism(JobFunctionReview professionalism) {
        this.professionalism = professionalism;
    }
}