package co.edu.javeriana.dm.notification;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;

@ApplicationScoped
public class SmsNotifier implements Notifier {

    private static final Logger LOG = Logger.getLogger(SmsNotifier.class.getName());

    @Override
    public Channel channel() {
        return Channel.SMS;
    }

    @Override
    public void send(Subscriber to, DisasterEvent event) {
        LOG.info("SMS a " + to.getContact() + ": " + event.summary());
    }
}
