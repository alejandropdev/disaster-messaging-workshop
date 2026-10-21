package co.edu.javeriana.dm.business;

import co.edu.javeriana.dm.domain.model.Subscriber;
import co.edu.javeriana.dm.domain.repository.SubscriberRepository;
import co.edu.javeriana.dm.domain.service.SubscriberDirectory;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class SubscriberDirectoryBean implements SubscriberDirectory {

    @EJB
    private SubscriberRepository repository;

    @Override
    public Subscriber add(Subscriber subscriber) {
        return repository.save(subscriber);
    }

    @Override
    public List<Subscriber> list() {
        return repository.findAll();
    }
}
