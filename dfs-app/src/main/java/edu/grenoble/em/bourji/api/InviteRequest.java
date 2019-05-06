package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 4/28/19.
 */
public class InviteRequest {

    private String email;

    public InviteRequest() {
        // default no-arg constructor for jackson
    }

    public String getEmail() {
        return this.email;
    }

}
