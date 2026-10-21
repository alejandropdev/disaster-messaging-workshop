package co.edu.javeriana.dm.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class NotificationServiceTest {

    private final DisasterEvent event = new DisasterEvent("r-1", "Sismo", "Bogotá", "ALTA", "6.1");

    private static class Recorder implements Notifier {
        final Channel channel;
        final List<String> sent = new ArrayList<>();
        boolean failing;

        Recorder(Channel channel) {
            this.channel = channel;
        }

        @Override
        public Channel channel() {
            return channel;
        }

        @Override
        public void send(Subscriber to, DisasterEvent event) {
            if (failing) {
                throw new IllegalStateException("smtp");
            }
            sent.add(to.contact());
        }
    }

    @Test
    void usesTheNotifierOfEachSubscriberChannel() {
        var registry = new SubscriberRegistry();
        registry.add("Cruz Roja", Channel.EMAIL, "cr@example.org");
        registry.add("Brigada", Channel.SMS, "+573001112233");
        var email = new Recorder(Channel.EMAIL);
        var sms = new Recorder(Channel.SMS);

        new NotificationService(registry, List.of(email, sms)).notifyAll(event);

        assertEquals(List.of("cr@example.org"), email.sent);
        assertEquals(List.of("+573001112233"), sms.sent);
    }

    @Test
    void oneFailureDoesNotStopTheRest() {
        var registry = new SubscriberRegistry();
        registry.add("A", Channel.EMAIL, "a@example.org");
        registry.add("B", Channel.SMS, "+573001112233");
        var email = new Recorder(Channel.EMAIL);
        email.failing = true;
        var sms = new Recorder(Channel.SMS);

        new NotificationService(registry, List.of(email, sms)).notifyAll(event);

        assertEquals(List.of("+573001112233"), sms.sent);
    }

    @Test
    void emailSubscriberNeedsValidAddress() {
        assertThrows(IllegalArgumentException.class,
                () -> new SubscriberRegistry().add("X", Channel.EMAIL, "sin-arroba"));
    }
}
