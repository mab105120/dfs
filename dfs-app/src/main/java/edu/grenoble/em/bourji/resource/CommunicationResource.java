package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.Authenticate;
import edu.grenoble.em.bourji.api.SupportMailDetails;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Properties;

/**
 * Created by Moe on 11/11/2017.
 */
@Path("/communication")
@Authenticate
public class CommunicationResource {

    private final String username;
    private final String password;

    public CommunicationResource(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @POST
    @Path("/send-support-email")
    public Response sendSupportEmail(SupportMailDetails mailDetails,
                                     @Context HttpHeaders httpHeaders) {
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
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailDetails.getFrom()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("mohd.bourji@gmail.com"));
            message.setSubject(mailDetails.getSubject());
            message.setText(mailDetails.getBody() + "\n-----------\nTo reply send email to: " + mailDetails.getFrom());
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            return Respond.respondWithError("Failed to send email: " + e.getMessage());
        }
        return Response.ok().build();
    }
}
