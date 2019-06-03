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
//        emails.put("confirmation-email", buildConfirmationEmail());
    }

    public EmailDetails getEmail(String usecase) {
        return emails.get(usecase);
    }

    private EmailDetails buildInvitationEmail() {
        EmailDetails invitationEmail = new EmailDetails();
        invitationEmail.setSubject("A teacher selected you to give them performance feedback!");
        invitationEmail.setBody(getInvitationEmailBody());
        return invitationEmail;
    }

    private EmailDetails buildConfirmationEmail() {
        EmailDetails email = new EmailDetails();
        email.setSubject("Send invitation endpoint hit!");
        email.setBody("");
        return email;
    }

    private String readEmailContentFromFile(String filename) {
        String filepath = String.format("emails/%s", filename);
        File f = new File(getClass().getClassLoader().getResource(filepath).getFile());
        try {
            return FileUtils.readFileToString(f, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Could not build invitation email. Failed to read email content from file. Details: " + e.getMessage());
        }
    }

    private String getInvitationEmailBody() {
        return "<h3>Hello,</h3>\n" +
                "<p>\n" +
                "    Our records show that you participated in our survey through MTurk. Thank you for your participation.<br>\n" +
                "    One of the teachers eligible for tenure promotion has reviewed your profile and requests your feedback on their performance.\n" +
                "    Please click <a href=\"www.google.com\">here</a> to review the teacher's performance dossier and submit your evaluation.<br>\n" +
                "    Your input will be considered for the final promotion decision. Please be mindful of the impact your decisions will have on personnel selection.\n" +
                "</p>\n" +
                "</br>\n" +
                "<h4>We appreciate your time!</h4>\n" +
                "<p>Regards,<br>Tenure Committee Kingston High School</p>";
    }
}
