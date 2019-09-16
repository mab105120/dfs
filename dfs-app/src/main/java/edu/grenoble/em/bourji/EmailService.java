package edu.grenoble.em.bourji;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Moe on 8/25/19.
 */
public class EmailService {

    private final EmailConfiguration emailConfig;

    public EmailService(EmailConfiguration emailConfig) {
        this.emailConfig = emailConfig;
    }

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfig.getUsername(), emailConfig.getPassword());
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("appraisal.confidence@gmail.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setContent(body, "text/html");
        Transport.send(message);
    }
}
