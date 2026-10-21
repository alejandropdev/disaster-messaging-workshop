package co.edu.javeriana.dm.business;

import co.edu.javeriana.dm.domain.model.Channel;
import co.edu.javeriana.dm.domain.model.DisasterMessage;
import co.edu.javeriana.dm.domain.model.Subscriber;
import co.edu.javeriana.dm.domain.notification.Notifier;
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
    public void send(Subscriber to, DisasterMessage message) {
        LOG.info("SMS a " + to.getContact() + ": " + message.summary());
    }
}
