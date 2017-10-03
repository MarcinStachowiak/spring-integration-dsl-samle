package com.ms.notificationservice.service.impl;

import com.ms.notificationservice.service.MailService;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service
public class MailServiceImpl implements MailService {

    @Override
    public void sendMessageOrderIsReady(String recipientEmail) {
        System.out.println("Sending e-mail to "+recipientEmail);
        Properties props = buildProperties();
        Session session = Session.getInstance(props, buildAuthenticator());
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from-email@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Package ready to fly");
            message.setText("Your package is ready to fly!");

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private Properties buildProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        return props;
    }

    private Authenticator buildAuthenticator() {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailKeys.LOGIN, MailKeys.PASSWORD);
            }
        };
    }


}
