package edu.grenoble.em.bourji.db.pojo;

import java.io.Serializable;

/**
 * Created by Moe on 11/6/2017.
 */
public class UserSubmissionIdentifier implements Serializable {

    private String user;
    private int submissionId;


    public UserSubmissionIdentifier() {
        // no-arg constructor for hibernate
    }

    public UserSubmissionIdentifier(String user, int submissionId) {
        this.user = user;
        this.submissionId = submissionId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof UserSubmissionIdentifier)) {
            return false;
        }
        UserSubmissionIdentifier that = (UserSubmissionIdentifier) o;
        return this.user.equals(that.user) && this.submissionId == that.submissionId;
    }

    @Override
    public int hashCode() {
        int hashCode = 5;
        return 37 * hashCode + (
                this.user.hashCode() + submissionId
        );
    }
}