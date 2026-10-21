package co.edu.javeriana.dm.business;

import co.edu.javeriana.dm.domain.model.DisasterMessage;
import co.edu.javeriana.dm.domain.model.DisasterReport;
import co.edu.javeriana.dm.domain.model.Subscriber;
import co.edu.javeriana.dm.domain.notification.Notifier;
import co.edu.javeriana.dm.domain.repository.DisasterRepository;
import co.edu.javeriana.dm.domain.repository.SubscriberRepository;
import co.edu.javeriana.dm.domain.service.DisasterRegistry;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class DisasterRegistryBean implements DisasterRegistry {

    private static final Logger LOG = Logger.getLogger(DisasterRegistryBean.class.getName());

    @EJB
    private DisasterRepository disasters;

    @EJB
    private SubscriberRepository subscribers;

    @Inject
    @Any
    private Instance<Notifier> notifiers;

    @Override
    public void register(DisasterMessage message) {
        // el broker entrega al menos una vez; reportId evita registrar y notificar dos veces
        if (disasters.existsByReportId(message.reportId())) {
            LOG.info("Reporte duplicado ignorado: " + message.reportId());
            return;
        }
        disasters.save(DisasterReport.from(message));
        subscribers.findAll().forEach(s -> notify(s, message));
    }

    @Override
    public List<DisasterReport> list() {
        return disasters.findAll();
    }

    private void notify(Subscriber subscriber, DisasterMessage message) {
        notifiers.stream()
                .filter(n -> n.channel() == subscriber.getChannel())
                .findFirst()
                .ifPresent(n -> {
                    try {
                        n.send(subscriber, message);
                    } catch (RuntimeException e) {
                        LOG.log(Level.WARNING, "No se pudo notificar a " + subscriber.getContact(), e);
                    }
                });
    }
}
