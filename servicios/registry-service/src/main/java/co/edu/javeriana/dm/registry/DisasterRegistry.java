package co.edu.javeriana.dm.registry;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jms.JMSConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Topic;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class DisasterRegistry {

    private static final Logger LOG = Logger.getLogger(DisasterRegistry.class.getName());

    @PersistenceContext(unitName = "registryPU")
    private EntityManager em;

    @Inject
    @JMSConnectionFactory("java:/JmsXA")
    private JMSContext jms;

    @Resource(lookup = "java:/jms/topic/DisasterRegistered")
    private Topic registered;

    public void register(DisasterMessage message) {
        // el broker entrega al menos una vez; reportId evita registrar y publicar dos veces
        long existing = em.createQuery("select count(r) from DisasterReport r where r.reportId = :id", Long.class)
                .setParameter("id", message.reportId())
                .getSingleResult();
        if (existing > 0) {
            LOG.info("Reporte duplicado ignorado: " + message.reportId());
            return;
        }
        em.persist(DisasterReport.from(message));
        jms.createProducer().send(registered, MessageJson.toJson(message));
    }

    public List<DisasterReport> list() {
        return em.createQuery("select r from DisasterReport r order by r.reportedAt desc", DisasterReport.class)
                .getResultList();
    }
}
