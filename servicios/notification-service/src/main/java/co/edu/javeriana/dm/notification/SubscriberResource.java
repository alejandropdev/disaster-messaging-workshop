package co.edu.javeriana.dm.notification;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Path("/subscribers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SubscriberResource {

    public record SubscriberRequest(String name, Channel channel, String contact) {
    }

    public record SubscriberView(Long id, String name, Channel channel, String contact) {
        static SubscriberView of(Subscriber s) {
            return new SubscriberView(s.getId(), s.getName(), s.getChannel(), s.getContact());
        }
    }

    @Inject
    private SubscriberDirectory directory;

    @POST
    public Response add(SubscriberRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("body es obligatorio");
        }
        Subscriber saved = directory.add(Subscriber.register(request.name(), request.channel(), request.contact()));
        return Response.status(Response.Status.CREATED).entity(SubscriberView.of(saved)).build();
    }

    @GET
    public List<SubscriberView> list() {
        return directory.list().stream().map(SubscriberView::of).toList();
    }
}
