package co.edu.javeriana.dm.registry;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class DisasterRegistry {

    @Inject
    EntityManager em;

    @Transactional
    public boolean register(JsonObject message) {
        // el broker entrega al menos una vez; reportId evita registrar dos veces
        long existing = em.createQuery("select count(r) from DisasterReport r where r.reportId = :id", Long.class)
                .setParameter("id", message.getString("reportId"))
                .getSingleResult();
        if (existing > 0) {
            return false;
        }
        em.persist(DisasterReport.from(message));
        return true;
    }

    public List<DisasterReport> list() {
        return em.createQuery("select r from DisasterReport r order by r.reportedAt desc", DisasterReport.class)
                .getResultList();
    }
}
