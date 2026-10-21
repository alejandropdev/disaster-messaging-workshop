package co.edu.javeriana.dm.notification;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class EmailNotifier implements Notifier {

    @ConfigProperty(name = "smtp.host")
    String host;

    @ConfigProperty(name = "smtp.port")
    String port;

    @Override
    public Channel channel() {
        return Channel.EMAIL;
    }

    @Override
    public void send(Subscriber to, DisasterEvent event) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        try {
            MimeMessage mail = new MimeMessage(Session.getInstance(props));
            mail.setFrom(new InternetAddress("alertas@disaster.local"));
            mail.setRecipient(Message.RecipientType.TO, new InternetAddress(to.contact()));
            mail.setSubject("Alerta: " + event.type() + " en " + event.location(), "UTF-8");
            mail.setText(event.summary(), "UTF-8");
            Transport.send(mail);
        } catch (MessagingException e) {
            throw new IllegalStateException(e);
        }
    }
}
