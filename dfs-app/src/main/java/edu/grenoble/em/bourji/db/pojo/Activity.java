package edu.grenoble.em.bourji.db.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Moe on 9/24/17.
 */
@Entity
@Table(name = "ACTIVITY")
public class Activity implements Serializable {

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    private String user;
    @Id
    @Column(name = "ACTIVITY", nullable = false, length = 10)
    private String activity;
    @Id
    @Column(name = "TIME", nullable = false)
    private Timestamp time;

    public Activity() {
        // no-arg default hibernate constructor
    }

    public Activity(String user, String activity) {
        this.user = user;
        this.activity = activity;
        this.time = Timestamp.valueOf(LocalDateTime.now());
    }

    public String getUser() {
        return user;
    }

    public String getActivity() {
        return activity;
    }

    public Timestamp getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof Activity)) return false;
        Activity that = (Activity) o;
        return this.user.equals(that.user) &&
                this.activity.equals(that.activity) &&
                this.time.equals(that.time);
    }

    @Override
    public int hashCode() {
        int hashCode = 5;
        return 37 * hashCode + (
                this.user.hashCode() + this.activity.hashCode() + this.time.hashCode()
        );
    }
}