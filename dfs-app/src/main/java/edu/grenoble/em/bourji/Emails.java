package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.api.EmailDetails;

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
    }

    public EmailDetails getEmail(String usecase) {
        return emails.get(usecase);
    }

    private EmailDetails buildInvitationEmail() {
        EmailDetails invitationEmail = new EmailDetails();
        invitationEmail.setSubject("A teacher selected you to give them performance feedback!");
        invitationEmail.setBody("Hello,\n\n" +
                "Our records show that you participated in our survey through Qualtrics. Thank you for your participation. One of the teachers eligible for tenure promotion has reviewed your profile and requests your feedback on their performance. Please use the link below to review the teacher's performance dossier and submit your evaluation:\n" +
                "<http://link.to.profile/teacher-to-review>\n" +
                "Your evaluation will be used, along with other factors, to evaluate the teacher's case for promotion. Please be mindful of the impact your input will have on personnel selection.\n" +
                "We appreciate your time.\n\n" +
                "Regards,\n" +
                "Tenure Committee\n" +
                "Kingston High School");
        return invitationEmail;
    }

    private EmailDetails buildConfirmationEmail() {
        EmailDetails email = new EmailDetails();
        email.setSubject("Send invitation endpoint hit!");
        email.setBody("");
        return email;
    }
}
