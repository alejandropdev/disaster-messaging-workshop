package co.edu.javeriana.dm.notification;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class SubscriberRegistry {

    private final List<Subscriber> subscribers = new CopyOnWriteArrayList<>();
    private final AtomicLong sequence = new AtomicLong();

    public Subscriber add(String name, Channel channel, String contact) {
        Subscriber subscriber = Subscriber.of(sequence.incrementAndGet(), name, channel, contact);
        subscribers.add(subscriber);
        return subscriber;
    }

    public List<Subscriber> list() {
        return List.copyOf(subscribers);
    }
}
