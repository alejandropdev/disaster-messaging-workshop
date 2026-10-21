package co.edu.javeriana.dm.domain.service;

import co.edu.javeriana.dm.domain.model.Subscriber;
import java.util.List;

public interface SubscriberDirectory {

    Subscriber add(Subscriber subscriber);

    List<Subscriber> list();
}
