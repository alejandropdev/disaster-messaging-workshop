package co.edu.javeriana.dm.intake;

import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Path("/api/disasters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DisasterResource {

    @Inject
    @Channel("received")
    Emitter<JsonObject> received;

    @POST
    public Response receive(DisasterRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("body es obligatorio");
        }
        JsonObject message = request.toMessage();
        received.send(message);
        return Response.accepted(Map.of("reportId", message.getString("reportId"))).build();
    }
}
