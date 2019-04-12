package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 4/9/19.
 */
public class Invitee {
    private String id;
    private String email;

    public Invitee() {
        // default no-arg constructor for jackson
    }

    public Invitee(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String recipientAddress() {
        return email == null ? id : email;
    }
}
