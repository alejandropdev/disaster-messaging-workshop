package co.edu.javeriana.dm.notification;

import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class DisasterConsumer {

    @Inject
    NotificationService notifications;

    @Incoming("registered")
    @Blocking
    public void onRegistered(JsonObject message) {
        notifications.notifyAll(DisasterEvent.from(message));
    }
}
