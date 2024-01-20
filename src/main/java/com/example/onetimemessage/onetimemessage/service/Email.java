package com.example.onetimemessage.onetimemessage.service;

import com.example.onetimemessage.onetimemessage.config.Config;
import lombok.AllArgsConstructor;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@AllArgsConstructor
public class Email {
    private final UUID id;
    private final String recipient;
    private static final Config CONFIG = new Config();
    public boolean send() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        AtomicBoolean emailStatus = new AtomicBoolean();

        Callable<Boolean> workerCallable = () -> {
            try {
                Properties properties = this.getPropertiesWithBasicSettings();
                Session session = this.getSessionWithCredentials(properties);
                Message message = this.getFilledMimeMessage(session, this.id, this.recipient);
                Transport.send(message);
                emailStatus.set(true);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return emailStatus.get();
        };

        Future<Boolean> result = executorService.submit(workerCallable);
        executorService.shutdown();
        return result.get();
    }
    private Properties getPropertiesWithBasicSettings() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", CONFIG.getEMAIL_HOST());
        properties.put("mail.smtp.port", CONFIG.getEMAIL_PORT());
        return properties;
    }

    private Session getSessionWithCredentials(Properties properties) {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CONFIG.getEMAIL_LOGIN(), CONFIG.getEMAIL_PASSWORD());
            }
        });
    }

    private String createEmailFormatAsString(UUID id) {
        return "Dear Recipient! " +
                "\n\n Someone has sent you a one-time message. The message is available under the below link: \n\n"
                + id.toString() // @TODO make it a full URL - domain + id
                + "\n\n\n Best regards! \n\n The Sender & One Time Message Team!";
    }
    private Message getFilledMimeMessage(Session session, UUID id, String recipient) throws MessagingException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(CONFIG.getEMAIL_LOGIN()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject("You received a one-time message!");
        message.setText(this.createEmailFormatAsString(id));
        return message;
    }
}
