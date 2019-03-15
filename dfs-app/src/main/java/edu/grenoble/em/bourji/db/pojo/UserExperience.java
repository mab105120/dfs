package edu.grenoble.em.bourji.db.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Created by Moe on 9/11/17.
 */
@Entity
@Table(name = "USER_EXPERIENCE")
@JsonIgnoreProperties(ignoreUnknown = true)
@IdClass(UserSubmissionIdentifier.class)
public class UserExperience {

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    private String user;
    @Column(name = "TITLE", nullable = false, length = 64)
    private String title;
    @Column(name = "TOTAL_SUBORDINATES", nullable = false, length = 64)
    private String subordinates;
    @Column(name = "PROF_EXPERIENCE", nullable = false, length = 64)
    private String professionalExperience;
    @Column(name = "APPRAISAL_EXPERIENCE", nullable = false, length = 64)
    private String appraisalExperience;
    @Column(name = "TOTAL_REVIEWS", nullable = false, length = 64)
    private String totalReviews;
    @Column(name = "TOTAL_REVIEWEES", nullable = false, length = 64)
    private String totalReviewees;
    @Column(name = "PERSONNEL_SELECTION", nullable = false, length = 64)
    private String personnelSelection;
    @Column(name = "CANDIDATES_REVIEWS", length = 64)
    private String totalCandidates;
    @Id
    @Column(name = "SUBMISSION_ID", nullable = false, unique = true)
    private int submissionId;
    @Column(name="EDUCATION_EMPLOYMENT", length = 10)
    private String isEmployedInEducation;
    @Column(name = "TRAINING_FREQUENCY", length = 64)
    private String trainingFrequency;
    @Column(name = "TRAINING_TYPE", length = 64)
    private String trainingType;
    @Column(name="TRAINING_TYPE_COMMENT", length = 300)
    private String trainingTypeComment;

    public UserExperience() {
        // no-arg constructor for hibernate
    }

    public UserExperience(String user, String title, String subordinates, String professionalExperience, String appraisalExperience, String totalReviews, String totalReviewees, String personnelSelection, String totalCandidates) {
        this.user = user;
        this.title = title;
        this.subordinates = subordinates;
        this.professionalExperience = professionalExperience;
        this.appraisalExperience = appraisalExperience;
        this.totalReviews = totalReviews;
        this.totalReviewees = totalReviewees;
        this.personnelSelection = personnelSelection;
        this.totalCandidates = totalCandidates;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public String getTitle() {
        return title;
    }

    public String getProfessionalExperience() {
        return professionalExperience;
    }

    public String getAppraisalExperience() {
        return appraisalExperience;
    }

    public String getTotalReviews() {
        return totalReviews;
    }

    public String getTotalReviewees() {
        return totalReviewees;
    }

    public String getSubordinates() {
        return subordinates;
    }

    public String getPersonnelSelection() {
        return personnelSelection;
    }

    public String getTotalCandidates() {
        return totalCandidates;
    }

    public String getIsEmployedInEducation() {
        return isEmployedInEducation;
    }

    public String getTrainingFrequency() {
        return trainingFrequency;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public String getTrainingTypeComment() {
        return trainingTypeComment;
    }
}