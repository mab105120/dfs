package edu.grenoble.em.bourji.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Moe on 8/29/17.
 */
public class JobFunctionReview {

    private String teacherName;

    public JobFunctionReview(String teacherName) {
        this.teacherName = teacherName;
    }


    @JsonProperty("SP1")
    private String supervisor1Review;
    @JsonProperty("SP2")
    private String supervisor2Review;
    @JsonProperty("SP3")
    private String supervisor3Review;

    public JobFunctionReview() {
        // default no arg constructor for jackson
    }

    public String getSupervisor1Review() {
        return teacherName != null ? setTeacherName(supervisor1Review, teacherName) : supervisor1Review;
    }

    public String getSupervisor2Review() {
        return teacherName != null ? setTeacherName(supervisor2Review, teacherName) : supervisor2Review;
    }

    public String getSupervisor3Review() {
        return teacherName != null ? setTeacherName(supervisor3Review, teacherName) : supervisor3Review;
    }

    private String setTeacherName(String review, String teacherName) {
        return review.replaceAll(String.format("${%s}","teacher"), teacherName);
    }

    public void setSupervisor1Review(String supervisor1Review) {
        this.supervisor1Review = supervisor1Review;
    }

    public void setSupervisor2Review(String supervisor2Review) {
        this.supervisor2Review = supervisor2Review;
    }

    public void setSupervisor3Review(String supervisor3Review) {
        this.supervisor3Review = supervisor3Review;
    }
}