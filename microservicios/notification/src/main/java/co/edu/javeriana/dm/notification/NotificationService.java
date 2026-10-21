package co.edu.javeriana.dm.notification;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class NotificationService {

    private static final Logger LOG = Logger.getLogger(NotificationService.class.getName());

    private final SubscriberRegistry subscribers;
    private final Iterable<Notifier> notifiers;

    @Inject
    NotificationService(SubscriberRegistry subscribers, @Any Instance<Notifier> notifiers) {
        this(subscribers, (Iterable<Notifier>) notifiers);
    }

    NotificationService(SubscriberRegistry subscribers, Iterable<Notifier> notifiers) {
        this.subscribers = subscribers;
        this.notifiers = notifiers;
    }

    public void notifyAll(DisasterEvent event) {
        for (Subscriber subscriber : subscribers.list()) {
            for (Notifier notifier : notifiers) {
                if (notifier.channel() != subscriber.channel()) {
                    continue;
                }
                try {
                    notifier.send(subscriber, event);
                } catch (RuntimeException e) {
                    LOG.log(Level.WARNING, "No se pudo notificar a " + subscriber.contact(), e);
                }
            }
        }
    }
}
