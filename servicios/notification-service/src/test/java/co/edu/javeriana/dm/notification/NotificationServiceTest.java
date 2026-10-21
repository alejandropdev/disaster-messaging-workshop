package co.edu.javeriana.dm.notification;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.enterprise.inject.Instance;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock SubscriberDirectory directory;
    @Mock Instance<Notifier> notifiers;
    @Mock Notifier email;
    @Mock Notifier sms;
    @InjectMocks NotificationService service;

    private final DisasterEvent event = new DisasterEvent("r-1", "Sismo", "Bogotá", "ALTA", "6.1");

    @Test
    void usesTheNotifierOfEachSubscriberChannel() {
        var mail = Subscriber.register("Cruz Roja", Channel.EMAIL, "cr@example.org");
        when(directory.list()).thenReturn(List.of(mail));
        when(notifiers.stream()).thenAnswer(i -> Stream.of(email, sms));
        when(email.channel()).thenReturn(Channel.EMAIL);
        lenient().when(sms.channel()).thenReturn(Channel.SMS);

        service.notifyAll(event);

        verify(email).send(mail, event);
        verify(sms, never()).send(any(), any());
    }

    @Test
    void oneFailureDoesNotStopTheRest() {
        var a = Subscriber.register("A", Channel.EMAIL, "a@example.org");
        var b = Subscriber.register("B", Channel.EMAIL, "b@example.org");
        when(directory.list()).thenReturn(List.of(a, b));
        when(notifiers.stream()).thenAnswer(i -> Stream.of(email));
        when(email.channel()).thenReturn(Channel.EMAIL);
        org.mockito.Mockito.doThrow(new IllegalStateException("smtp")).when(email).send(a, event);

        service.notifyAll(event);

        verify(email).send(b, event);
    }

    @Test
    void emailSubscriberNeedsValidAddress() {
        assertThrows(IllegalArgumentException.class,
                () -> Subscriber.register("X", Channel.EMAIL, "sin-arroba"));
    }
}
