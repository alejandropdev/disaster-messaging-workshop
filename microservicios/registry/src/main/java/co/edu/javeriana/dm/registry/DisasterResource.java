package co.edu.javeriana.dm.registry;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.time.Instant;
import java.util.List;

@Path("/api/disasters")
@Produces(MediaType.APPLICATION_JSON)
public class DisasterResource {

    public record DisasterView(String reportId, String type, String location, Severity severity,
                               String description, Instant reportedAt) {
        static DisasterView of(DisasterReport r) {
            return new DisasterView(r.getReportId(), r.getType(), r.getLocation(), r.getSeverity(),
                    r.getDescription(), r.getReportedAt());
        }
    }

    @Inject
    DisasterRegistry registry;

    @GET
    public List<DisasterView> list() {
        return registry.list().stream().map(DisasterView::of).toList();
    }
}
