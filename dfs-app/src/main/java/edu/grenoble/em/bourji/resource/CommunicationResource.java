package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.Authenticate;
import edu.grenoble.em.bourji.Emails;
import edu.grenoble.em.bourji.TimeUtils;
import edu.grenoble.em.bourji.api.EmailDetails;
import edu.grenoble.em.bourji.db.dao.InviteDAO;
import edu.grenoble.em.bourji.db.pojo.Invite;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Properties;

/**
 * Created by Moe on 11/11/2017.
 */
@Path("/communication")
@Authenticate
public class CommunicationResource {

    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CommunicationResource.class);
    private final String username;
    private final String password;
    private Emails emails;
    private InviteDAO inviteDAO;

    public CommunicationResource(String username, String password, Emails emails, InviteDAO inviteDAO) {
        this.username = username;
        this.password = password;
        this.emails = emails;
        this.inviteDAO = inviteDAO;
    }

    @POST
    @Path("/send-support-email")
    public Response sendSupportEmail(EmailDetails emailDetails) {
        try {
            String body = String.format("%s\n---To reply send email to %s---", emailDetails.getBody(), emailDetails.getFrom());
            sendEmail(
                    emailDetails.getFrom(),
                    emailDetails.getTo(),
                    emailDetails.getSubject(),
                    body
            );
        } catch (MessagingException e) {
            return Respond.respondWithError("Failed to send email: " + e.getMessage());
        }
        return Response.ok().build();
    }

    @POST
    @Path("/send-invitations")
    @UnitOfWork
    public Response sendInvitations(@QueryParam("automated") boolean automated) {
        String sender = "mail@dfs.com";
        EmailDetails invitationEmail = emails.getEmail("invitation-email");
        LOGGER.info("Sending invites for evaluation. Retrieving list of invitees...");
        try {
            List<Invite> inviteeList = inviteDAO.getPendingUserEmails();
            if (inviteeList.isEmpty()) {
                LOGGER.info("Invitee list is empty. Nothing to do");
                return Response.ok().build();
            }
            LOGGER.info(String.format("Found %s invitees. Sending emails..", inviteeList.size()));
            int totalInvitesSent = 0;
            int totalReminders = 0;
            for (Invite invite : inviteeList) {
                String recipientAddress = invite.getEmail() == null ? invite.getId() : invite.getEmail();
//                long hoursSinceLastSeen = TimeUtils.hoursSince(invite.getTime());
                long minutesSinceLastSeen = TimeUtils.minutesSince(invite.getTime());
                if (minutesSinceLastSeen > 15) {
                    if (invite.getStage().equalsIgnoreCase("PENDING_INVITE")) {
                        sendEmail(sender, recipientAddress, invitationEmail.getSubject(), invitationEmail.getBody());
                        inviteDAO.updateInviteeStatus(invite, "INVITE_SENT");
                        totalInvitesSent++;
                    } else if (invite.getStage().equalsIgnoreCase("INVITE_SENT")) {
                        String subject = String.format("REMINDER: %s", invitationEmail.getSubject());
                        sendEmail(sender, recipientAddress, subject, invitationEmail.getBody());
                        inviteDAO.updateInviteeStatus(invite, "REMINDER_SENT_1");
                        totalReminders++;
                    }
                    else if (invite.getStage().startsWith("REMINDER_SENT")) {
                        String subject = String.format("REMINDER: %s", invitationEmail.getSubject());
                        int lastReminder = Integer.parseInt(invite.getStage().substring(invite.getStage().length() - 1));
                        sendEmail(sender, recipientAddress, subject, invitationEmail.getBody());
                        inviteDAO.updateInviteeStatus(invite, String.format("REMINDER_SENT_%s", lastReminder + 1));
                        totalReminders++;
                    }
                }
            }
            LOGGER.info(String.format("Sent emails to qualified participants. New invites: %s | Reminders: %s", totalInvitesSent, totalReminders));
            if (automated) {
                LOGGER.info("Sending notification email to the boss...");
                EmailDetails confirmEmail = emails.getEmail("confirmation-email");
                sendEmail(sender, "mohd.bourji@gmail.com", confirmEmail.getSubject(), confirmEmail.getBody());
            }
        } catch (Throwable e) {
            String message = "Failed to send invitations. Details: " + e.getMessage();
            LOGGER.error(message);
            return Respond.respondWithError(message);
        }
        return Response.ok().build();
    }

    private void sendEmail(String from, String to, String subject, String body) throws MessagingException {
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(body);
        Transport.send(message);
    }

}
