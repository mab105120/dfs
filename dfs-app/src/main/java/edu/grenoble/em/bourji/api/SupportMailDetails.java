package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 11/15/17.
 */
public class SupportMailDetails {

    private String from;
    private String subject;
    private String body;

    public SupportMailDetails() {
        // no-arg default constructor for jackson
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}
