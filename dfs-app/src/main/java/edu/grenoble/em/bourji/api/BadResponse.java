package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 9/12/2017.
 */
public class BadResponse {
    private String message;

    public BadResponse() {
        // no-arg constructor for jackson
    }

    public BadResponse withMessage(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return message;
    }
}
