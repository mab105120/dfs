package edu.grenoble.em.bourji.db.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Moe on 4/9/19.
 */
@Entity
@Table(name = "INVITES")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Invite {

    @Id
    @Column(name="ID", nullable = false)
    private String id;
    @Column(name = "STAGE", nullable = false)
    private String stage;
    @Column(name = "ADDRESS", nullable = false)
    private String email;
    @Column(name = "TIME")
    private Timestamp time;

    public Invite() {
        // no-arg default constructor for hibernate
    }

    public Invite(String id, String stage, String email) {
        this.id = id;
        this.stage = stage;
        this.email = email;
        this.time = Timestamp.valueOf(LocalDateTime.now());
    }

    public String getId() {
        return id;
    }

    public String getStage() {
        return stage;
    }

    public String getEmail() {
        return email;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
