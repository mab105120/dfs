package edu.grenoble.em.bourji.db.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Moe on 9/5/2017.
 */
@Entity
@Table(name = "PERFORMANCE_REVIEW")
@IdClass(PerformanceReview.UniqueIdentifier.class)
public class PerformanceReview {

    @Id
    @Column(name = "ID", length = 10, nullable = false)
    private String id;
    @Id
    @Column(name = "JOB_FUNCTION", length = 64, nullable = false)
    private String jobFunction;
    @Id
    @Column(name = "SUPERVISOR", length = 64, nullable = false)
    private String supervisor;
    @Column(name = "REVIEW", length = 1000, nullable = false)
    private String review;

    public PerformanceReview() {
        // no-arg default constructor for hibernate
    }

    public PerformanceReview(String id, String jobFunction, String supervisor, String review) {
        this.id = id;
        this.jobFunction = jobFunction;
        this.supervisor = supervisor;
        this.review = review;
    }

    public String getId() {
        return id;
    }

    public String getJobFunction() {
        return jobFunction;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public String getReview() {
        return review;
    }

    public static class UniqueIdentifier implements Serializable {
        private String id;
        private String jobFunction;
        private String supervisor;

        public UniqueIdentifier() {
            // no-arg constructor for hibernate
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof UniqueIdentifier)) return false;
            UniqueIdentifier that = (UniqueIdentifier) o;
            return this.id.equals(that.id)
                    && this.jobFunction.equals(that.jobFunction)
                    && this.supervisor.equals(that.supervisor);
        }

        @Override
        public int hashCode() {
            int hashCode = 5;
            return 37 * hashCode + (
                    this.id.hashCode() + this.jobFunction.hashCode() + this.supervisor.hashCode()
            );
        }
    }
}