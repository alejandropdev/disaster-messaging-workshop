package co.edu.javeriana.dm.registry;

import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class DisasterConsumer {

    @Inject
    DisasterRegistry registry;

    @Inject
    @Channel("registered")
    Emitter<JsonObject> registered;

    @Incoming("received")
    @Blocking
    public void onReceived(JsonObject message) {
        if (registry.register(message)) {
            registered.send(message);
        }
    }
}
