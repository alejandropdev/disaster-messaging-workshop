package co.edu.javeriana.dm.web;

import co.edu.javeriana.dm.domain.model.DisasterMessage;
import co.edu.javeriana.dm.domain.model.DisasterReport;
import co.edu.javeriana.dm.domain.model.Severity;
import co.edu.javeriana.dm.domain.service.DisasterIntake;
import co.edu.javeriana.dm.domain.service.DisasterRegistry;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RequestScoped
@Path("/disasters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DisasterResource {

    public record DisasterRequest(String type, String location, Severity severity, String description) {
    }

    public record DisasterView(String reportId, String type, String location, Severity severity,
                               String description, Instant reportedAt) {
        static DisasterView of(DisasterReport r) {
            return new DisasterView(r.getReportId(), r.getType(), r.getLocation(), r.getSeverity(),
                    r.getDescription(), r.getReportedAt());
        }
    }

    @Inject
    private DisasterIntake intake;

    @Inject
    private DisasterRegistry registry;

    @POST
    public Response receive(DisasterRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("body es obligatorio");
        }
        DisasterMessage message = DisasterMessage.create(
                request.type(), request.location(), request.severity(), request.description());
        intake.publish(message);
        return Response.accepted(Map.of("reportId", message.reportId())).build();
    }

    @GET
    public List<DisasterView> list() {
        return registry.list().stream().map(DisasterView::of).toList();
    }
}
