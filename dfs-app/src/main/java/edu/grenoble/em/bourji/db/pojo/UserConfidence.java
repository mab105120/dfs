package edu.grenoble.em.bourji.db.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Moe on 9/12/2017.
 */
@Entity
@Table(name = "USER_CONFIDENCE")
@JsonIgnoreProperties(ignoreUnknown = true)
@IdClass(UserConfidence.UniqueIdentifier.class)
public class UserConfidence {

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    private String user;
    @Id
    @Column(name = "ITEM_CODE", nullable = false, length = 64)
    private String itemCode;
    @Column(name = "RESPONSE", nullable = false)
    private int response;
    @Id
    @Column(name = "SUBMISSION_ID", nullable = false, unique = true)
    private int submissionId;

    public UserConfidence(String user, String itemCode, int response) {
        this.user = user;
        this.itemCode = itemCode;
        this.response = response;
    }

    public UserConfidence() {
        // no-arg constructor for hibernate
    }

    public static class UniqueIdentifier implements Serializable {
        private String user;
        private String itemCode;
        private int submissionId;

        public UniqueIdentifier() {
            // no-arg constructor for hibernate
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof UniqueIdentifier)) {
                return false;
            }
            UniqueIdentifier that = (UniqueIdentifier) o;
            return this.user.equals(that.user) && Objects.equals(this.itemCode, that.itemCode)
                    && this.submissionId == that.submissionId;
        }

        @Override
        public int hashCode() {
            int hashCode = 5;
            return 37 * hashCode + (
                    this.itemCode.hashCode()
            );
        }
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public int getResponse() {
        return response;
    }
}
