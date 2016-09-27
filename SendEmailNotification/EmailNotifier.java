import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Copyright 2016 Igor Rubis
 * Licensed under the Apache License, Version 2.0
 *
 * dependencies:
 *      compile     group: 'javax.mail',                name: 'mail',                   version: '1.4.7'
 *      compile     group: 'javax.mail',                name: 'javax.mail-api',         version: '1.5.2'
 *
 * new EmailNotifier(email, gmailPass).getThread().join();
 *
 * Disable gmail 2-step authentication to make this work
 */

public class EmailNotifier implements Runnable {
    private final String userName;
    private final String passWord;
    private final Properties mailServerProperties;
    private final Session getMailSession;
    private final MimeMessage generateMailMessage;
    private Thread t;

    public EmailNotifier(final String userName, final String passWord) {
        t = new Thread(this);
        this.userName = userName;
        this.passWord = passWord;
        mailServerProperties = System.getProperties();
        getMailSession = Session.getInstance(mailServerProperties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, passWord);
            }
        });
        generateMailMessage = new MimeMessage(getMailSession);
        t.start();
    }

    public void run() {
        synchronized (this) {
            try {
                mailServerProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
                mailServerProperties.put("mail.smtp.port", "587");
                mailServerProperties.put("mail.smtp.auth", "true");
                mailServerProperties.put("mail.smtp.starttls.enable", "true");


                generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(userName));
                generateMailMessage.setSubject("Greetings...");
                String emailBody = "Email body";
                generateMailMessage.setContent(emailBody, "text/html");

                Transport transport = getMailSession.getTransport("smtp");

                try {
                    transport.connect("smtp.gmail.com", userName, passWord);
                    transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
                } finally {
                    transport.close();
                }
            } catch (Exception e) {
                //do smth
            }
        }
    }

    public Thread getThread() {
        return t;
    }
}
