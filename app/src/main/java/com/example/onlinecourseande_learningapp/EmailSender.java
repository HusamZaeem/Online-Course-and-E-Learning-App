package com.example.onlinecourseande_learningapp;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
public class EmailSender {

    public static void sendEmail(String to, String subject, String body) throws MessagingException {
        final String from = "onlinecoursesandelearning@gmail.com";
        final String password = "Online@Courses@E-Learning1997";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }

}
