package co.edu.javeriana.dm.notification;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/subscribers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SubscriberResource {

    public record SubscriberRequest(String name, Channel channel, String contact) {
    }

    @Inject
    SubscriberRegistry registry;

    @POST
    public Response add(SubscriberRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("body es obligatorio");
        }
        Subscriber saved = registry.add(request.name(), request.channel(), request.contact());
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }

    @GET
    public List<Subscriber> list() {
        return registry.list();
    }
}
