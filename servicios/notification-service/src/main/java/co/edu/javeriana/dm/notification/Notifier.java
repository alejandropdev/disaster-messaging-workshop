package co.edu.javeriana.dm.notification;

public interface Notifier {

    Channel channel();

    void send(Subscriber to, DisasterEvent event);
}
