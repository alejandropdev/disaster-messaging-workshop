package co.edu.javeriana.dm.data;

import co.edu.javeriana.dm.domain.model.Subscriber;
import co.edu.javeriana.dm.domain.repository.SubscriberRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class JpaSubscriberRepository implements SubscriberRepository {

    @PersistenceContext(unitName = "disasterPU")
    private EntityManager em;

    @Override
    public Subscriber save(Subscriber subscriber) {
        em.persist(subscriber);
        return subscriber;
    }

    @Override
    public List<Subscriber> findAll() {
        return em.createQuery("select s from Subscriber s order by s.id", Subscriber.class).getResultList();
    }
}
