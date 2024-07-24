import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Email {
    private static final String FROM_EMAIL = "ssuunaarthur@gmail.com";
    private static final String USERNAME = "ssuunaarthur@gmail.com"; // your Gmail username
    private static final String PASSWORD = "mcst fqxh npjm fdmg"; // your Gmail password
    private static final String HOST = "smtp.gmail.com";

    private static Session getSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        return Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });
    }

    public static void notifyRep(String representativeEmail) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(representativeEmail));
            message.setSubject("New applicant to validate");
            message.setText("I hope you are doing well. I am sending this email to inform you that a new applicant has been submitted to validate. Please log into the system to validate the applicant.");

            Transport.send(message);
            System.out.println("Representative notification sent successfully....");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void notifyParticipant(String participantEmail, String status) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(participantEmail));

            if ("confirm".equalsIgnoreCase(status)) {
                message.setSubject("Application Confirmed");
                message.setText("Congratulations! Your application has been confirmed. Please log into the system to proceed and attempt the challenges.");
            } else if ("reject".equalsIgnoreCase(status)) {
                message.setSubject("Application Rejected");
                message.setText("We regret to inform you that your application has been rejected and you have been denied from participating in the challenges.");
            }

            Transport.send(message);
            System.out.println("Participant notification sent successfully....");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}