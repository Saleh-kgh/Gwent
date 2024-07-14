package controller;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail {
    public static boolean run(String email, String subject, String body) {
        // اطلاعات سرور SMTP و حساب کاربری
        String host = "smtp.gmail.com";
        String port = "587";
        String username = "senderEmail@gmail.com";
        String appPassword = "appPassword";

        // تنظیمات properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // ایجاد session
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        });

        try {
            // ایجاد پیام
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("senderEmail@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(body);

            // ارسال پیام
            Transport.send(message);

            System.out.println("Email sent successfully");
            return true;

        } catch (MessagingException e) {
            System.out.println("Can't send email to " + email);
            return false;
        }
    }
}