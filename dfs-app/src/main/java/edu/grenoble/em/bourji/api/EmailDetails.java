package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 11/15/17.
 */
public class EmailDetails {

    private String to = "mohd.bourji@gmail.com";
    private String from = "mail@dfs.com";
    private String subject;
    private String body;

    public EmailDetails() {
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

    public String getTo() {
        return to;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
