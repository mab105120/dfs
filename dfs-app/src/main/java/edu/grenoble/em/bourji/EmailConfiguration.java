package edu.grenoble.em.bourji;

/**
 * Created by Moe on 11/12/2017.
 */
public class EmailConfiguration {

    private String username;
    private String password;

    public EmailConfiguration() {
        // default no-arg constructor for jackson
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
