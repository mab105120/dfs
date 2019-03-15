package edu.grenoble.em.bourji.db.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by Moe on 9/23/2017.
 */
@Entity
@Table(name = "STATUS")
@IdClass(Status.UniqueIdentifier.class)
public class Status implements Serializable {

    @Id
    @Column(name = "ID", length = 64, nullable = false)
    private String user;
    @Id
    @Column(name = "STATUS", length = 64, nullable = false)
    private String status;
    @Column(name = "TIME")
    private Timestamp time;
    @Id
    @Column(name = "SUBMISSION_ID", nullable = false, unique = true)
    private int submissionId;

    public Status() {
        // no-arg default constructor for hibernate
    }

    public Status(String user, String status, int submissionId) {
        this.user = user;
        this.status = status;
        this.submissionId = submissionId;
        this.time = Timestamp.valueOf(LocalDateTime.now());
    }

    public String getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getTime() {
        return time;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public static class UniqueIdentifier implements Serializable {
        private String user;
        private String status;
        private int submissionId;

        public UniqueIdentifier() {
            // no-arg default constructor for hibernate
        }

        public String getUser() {
            return user;
        }

        public String getStatus() {
            return status;
        }

        public int getSubmissionId() {
            return submissionId;
        }

        @Override
        public boolean equals(Object o) {
            if(o == null) return false;
            if(!(o instanceof Status.UniqueIdentifier)) return false;
            Status.UniqueIdentifier that = (Status.UniqueIdentifier) o;
            return Objects.equals(this.user, that.user) &&
                    Objects.equals(this.status, that.status) &&
                    this.submissionId == that.submissionId;
        }

        @Override
        public int hashCode() {
            int hashCode = 5;
            return 37 * hashCode + (
                    this.user.hashCode() + this.status.hashCode() +  submissionId
            );
        }
    }
}
