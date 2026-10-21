package co.edu.javeriana.dm.domain.repository;

import co.edu.javeriana.dm.domain.model.Subscriber;
import java.util.List;

public interface SubscriberRepository {

    Subscriber save(Subscriber subscriber);

    List<Subscriber> findAll();
}
