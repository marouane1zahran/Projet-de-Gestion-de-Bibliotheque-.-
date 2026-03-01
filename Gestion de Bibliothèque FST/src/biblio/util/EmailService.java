package biblio.util; 

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {

   
    private static final String EMAIL_EXPEDITEUR = "biblio.fst.marrakech@gmail.com";
    private static final String MOT_DE_PASSE_APP = "aaco miin huud qrah";
    public static boolean envoyerEmail(String emailDestinataire, String sujet, String contenu) {
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

       
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_EXPEDITEUR, MOT_DE_PASSE_APP);
            }
        });

        try {
           
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_EXPEDITEUR));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestinataire));
            message.setSubject(sujet);
            message.setText(contenu);

            
            Transport.send(message);
            System.out.println("E-mail envoyé avec succès à " + emailDestinataire);
            return true;

        } catch (MessagingException e) {
            System.err.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
            return false;
        }
    }
}