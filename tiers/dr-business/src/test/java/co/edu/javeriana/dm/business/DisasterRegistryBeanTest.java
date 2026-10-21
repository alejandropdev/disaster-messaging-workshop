package co.edu.javeriana.dm.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.edu.javeriana.dm.domain.model.Channel;
import co.edu.javeriana.dm.domain.model.DisasterMessage;
import co.edu.javeriana.dm.domain.model.DisasterReport;
import co.edu.javeriana.dm.domain.model.Severity;
import co.edu.javeriana.dm.domain.model.Subscriber;
import co.edu.javeriana.dm.domain.notification.Notifier;
import co.edu.javeriana.dm.domain.repository.DisasterRepository;
import co.edu.javeriana.dm.domain.repository.SubscriberRepository;
import jakarta.enterprise.inject.Instance;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DisasterRegistryBeanTest {

    @Mock DisasterRepository disasters;
    @Mock SubscriberRepository subscribers;
    @Mock Instance<Notifier> notifiers;
    @Mock Notifier email;
    @Mock Notifier sms;
    @InjectMocks DisasterRegistryBean registry;

    private final DisasterMessage message = DisasterMessage.create("Sismo", "Bogotá", Severity.ALTA, "6.1");

    @Test
    void duplicateReportIsIgnored() {
        when(disasters.existsByReportId(message.reportId())).thenReturn(true);

        registry.register(message);

        verify(disasters, never()).save(any());
        verify(subscribers, never()).findAll();
    }

    @Test
    void notifiesEachSubscriberThroughItsChannel() {
        var mail = Subscriber.register("Cruz Roja", Channel.EMAIL, "cr@example.org");
        when(subscribers.findAll()).thenReturn(List.of(mail));
        when(notifiers.stream()).thenAnswer(i -> Stream.of(email, sms));
        when(email.channel()).thenReturn(Channel.EMAIL);
        lenient().when(sms.channel()).thenReturn(Channel.SMS);

        registry.register(message);

        verify(disasters).save(any(DisasterReport.class));
        verify(email).send(mail, message);
        verify(sms, never()).send(any(), any());
    }

    @Test
    void failingNotifierDoesNotAbortRegistration() {
        var mail = Subscriber.register("Cruz Roja", Channel.EMAIL, "cr@example.org");
        when(subscribers.findAll()).thenReturn(List.of(mail));
        when(notifiers.stream()).thenAnswer(i -> Stream.of(email));
        when(email.channel()).thenReturn(Channel.EMAIL);
        org.mockito.Mockito.doThrow(new IllegalStateException("smtp caído")).when(email).send(any(), any());

        registry.register(message);

        verify(disasters).save(any(DisasterReport.class));
    }
}
