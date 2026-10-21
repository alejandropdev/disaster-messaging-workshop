package co.edu.javeriana.dm.business;

import co.edu.javeriana.dm.domain.model.Channel;
import co.edu.javeriana.dm.domain.model.DisasterMessage;
import co.edu.javeriana.dm.domain.model.Subscriber;
import co.edu.javeriana.dm.domain.notification.Notifier;
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
    public void send(Subscriber to, DisasterMessage message) {
        try {
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress("alertas@disaster.local"));
            mail.setRecipient(Message.RecipientType.TO, new InternetAddress(to.getContact()));
            mail.setSubject("Alerta: " + message.type() + " en " + message.location(), "UTF-8");
            mail.setText(message.summary(), "UTF-8");
            Transport.send(mail);
        } catch (MessagingException e) {
            throw new IllegalStateException(e);
        }
    }
}
