package co.edu.javeriana.dm.notification;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@ApplicationScoped
public class EmailNotifier implements Notifier {

    @Resource(lookup = "java:jboss/mail/Default")
    private Session session;

    @Override
    public Channel channel() {
        return Channel.EMAIL;
    }

    @Override
    public void send(Subscriber to, DisasterEvent event) {
        try {
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress("alertas@disaster.local"));
            mail.setRecipient(Message.RecipientType.TO, new InternetAddress(to.getContact()));
            mail.setSubject("Alerta: " + event.type() + " en " + event.location(), "UTF-8");
            mail.setText(event.summary(), "UTF-8");
            Transport.send(mail);
        } catch (MessagingException e) {
            throw new IllegalStateException(e);
        }
    }
}
