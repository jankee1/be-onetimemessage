package com.example.onetimemessage.onetimemessage.service;

import com.example.onetimemessage.onetimemessage.config.Config;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;

@Service
public class EmailService {
    private final Config config;
    private final String emailLogin;
    private final String emailPassword;
    private final String emailHost;
    private final int emailPort;

    public EmailService(Config config) {
        this.config = config;
        this.emailLogin = this.config.getEmailLogin();
        this.emailPassword = this.config.getEmailPassword();
        this.emailHost = this.config.getEmailHost();
        this.emailPort = this.config.getEmailPort();
    }

    public boolean send(UUID id, String recipient) {
        var emailStatus = false;

        try {
            var properties = this.getPropertiesWithBasicSettings();
            var session = this.getSessionWithCredentials(properties);
            var message = this.getFilledMimeMessage(session, id, recipient);

            Transport.send(message);

            emailStatus = true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return emailStatus;
    }

    private Properties getPropertiesWithBasicSettings() {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", this.emailHost);
        properties.put("mail.smtp.port", this.emailPort);

        return properties;
    }

    private Session getSessionWithCredentials(Properties properties) {
        var login = this.emailLogin;
        var password =this.emailPassword;

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(login, password);
            }
        });
    }

    private String createEmailFormatAsString(UUID id) {
        return "Dear Recipient! " +
                "\n\nOne-time message is awaiting you under the below link: \n\n"
                + id.toString() // @TODO make it a full URL - domain + id
                + "\n\nKeep in mind that it can be read only once."
                + "\n\n\nBest regards!";
    }

    private Message getFilledMimeMessage(Session session, UUID id, String recipient) throws MessagingException {
        var message = new MimeMessage(session);

        message.setFrom(new InternetAddress(this.emailLogin));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject("You received a one-time message!");
        message.setText(this.createEmailFormatAsString(id));

        return message;
    }
}
