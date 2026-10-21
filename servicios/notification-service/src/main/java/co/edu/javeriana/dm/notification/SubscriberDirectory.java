package co.edu.javeriana.dm.notification;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class SubscriberDirectory {

    @PersistenceContext(unitName = "notificationPU")
    private EntityManager em;

    public Subscriber add(Subscriber subscriber) {
        em.persist(subscriber);
        return subscriber;
    }

    public List<Subscriber> list() {
        return em.createQuery("select s from Subscriber s order by s.id", Subscriber.class).getResultList();
    }
}
