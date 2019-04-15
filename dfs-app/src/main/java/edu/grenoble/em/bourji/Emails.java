package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.api.EmailDetails;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moe on 4/4/19.
 */
public class Emails {

    private Map<String, EmailDetails> emails = new HashMap<>();

    Emails() {
        emails.put("invitation-email", buildInvitationEmail());
        emails.put("confirmation-email", buildConfirmationEmail());
        emails.put("love-email", buildLoveEmail());
    }

    public EmailDetails getEmail(String usecase) {
        return emails.get(usecase);
    }

    private EmailDetails buildInvitationEmail() {
        EmailDetails invitationEmail = new EmailDetails();
        invitationEmail.setSubject("A teacher selected you to give them performance feedback!");
        invitationEmail.setBody(readEmailContentFromFile("invitation.html"));
        return invitationEmail;
    }

    private EmailDetails buildLoveEmail() {
        EmailDetails loveEmail = new EmailDetails();
        loveEmail.setSubject("Baby loves you!");
        loveEmail.setBody(readEmailContentFromFile("love.html"));
        return loveEmail;
    }

    private EmailDetails buildConfirmationEmail() {
        EmailDetails email = new EmailDetails();
        email.setSubject("Send invitation endpoint hit!");
        email.setBody("");
        return email;
    }

    private String readEmailContentFromFile(String filename) {
        File f = new File(getClass().getClassLoader().getResource("pairs.txt").getFile());
        try {
            return FileUtils.readFileToString(f, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Could not build invitation email. Failed to read email content from file. Details: " + e.getMessage());
        }
    }
}
