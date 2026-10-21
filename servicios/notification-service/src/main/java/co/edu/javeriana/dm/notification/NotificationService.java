package co.edu.javeriana.dm.notification;

import jakarta.ejb.Stateless;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class NotificationService {

    private static final Logger LOG = Logger.getLogger(NotificationService.class.getName());

    @Inject
    private SubscriberDirectory directory;

    @Inject
    @Any
    private Instance<Notifier> notifiers;

    public void notifyAll(DisasterEvent event) {
        directory.list().forEach(s -> notifiers.stream()
                .filter(n -> n.channel() == s.getChannel())
                .findFirst()
                .ifPresent(n -> {
                    try {
                        n.send(s, event);
                    } catch (RuntimeException e) {
                        LOG.log(Level.WARNING, "No se pudo notificar a " + s.getContact(), e);
                    }
                }));
    }
}
