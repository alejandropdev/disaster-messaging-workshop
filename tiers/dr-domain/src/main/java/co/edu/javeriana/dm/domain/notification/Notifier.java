package co.edu.javeriana.dm.domain.notification;

import co.edu.javeriana.dm.domain.model.Channel;
import co.edu.javeriana.dm.domain.model.DisasterMessage;
import co.edu.javeriana.dm.domain.model.Subscriber;

public interface Notifier {

    Channel channel();

    void send(Subscriber to, DisasterMessage message);
}
