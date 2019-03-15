package edu.grenoble.em.bourji.db.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Moe on 4/18/18.
 */
@Entity
@Table(name = "PROFILES")
public class ParticipantProfile {

    @Id
    @Column(name = "ID", nullable = false)
    private String id;
    @Column(name = "PROFILE", nullable = false)
    private String profile;

    public ParticipantProfile() {
        // default no-arg constructor for hibernate
    }

    public ParticipantProfile(String id, String profile) {
        this.id = id;
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public String getProfile() {
        return profile;
    }
}