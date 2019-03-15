package edu.grenoble.em.bourji.db.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Created by Moe on 9/9/2017.
 */
@Entity
@Table(name = "USER_DEMOGRAPHIC")
@JsonIgnoreProperties(ignoreUnknown = true)
@IdClass(UserSubmissionIdentifier.class)
public class UserDemographic {

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    private String user;
    @Id
    @Column(name = "SUBMISSION_ID", nullable = false)
    private Integer submissionId;
    @Column(name = "AGE", nullable = false, length = 10)
    private String age;
    @Column(name = "GENDER", nullable = false, length = 10)
    private String gender;
    @Column(name = "EDUCATION", nullable = false, length = 64)
    private String education;
    @Column(name = "EDUCATION_EXPERIENCE", length = 64)
    private String educationExperience;
    @Column(name = "DIVISION", nullable = false, length = 64)
    private String division;

    public UserDemographic() {
        // no-arg constructor for hibernate
    }

    public UserDemographic(String user, String age, String gender, String education, String division) {
        this.user = user;
        this.age = age;
        this.gender = gender;
        this.education = education;
        this.division = division;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getEducation() {
        return education;
    }

    public String getDivision() {
        return division;
    }

    public String getUser() {
        return user;
    }

    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

    public String getEducationExperience() {
        return educationExperience;
    }
}