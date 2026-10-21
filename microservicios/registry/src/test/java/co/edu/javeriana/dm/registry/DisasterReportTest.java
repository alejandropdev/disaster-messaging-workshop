package co.edu.javeriana.dm.registry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.vertx.core.json.JsonObject;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class DisasterReportTest {

    @Test
    void buildsReportFromMessage() {
        var at = Instant.parse("2026-10-20T10:00:00Z");
        var message = new JsonObject()
                .put("reportId", "r-1").put("type", "Sismo").put("location", "Bogotá")
                .put("severity", "ALTA").put("description", "6.1").put("reportedAt", at.toString());

        var report = DisasterReport.from(message);

        assertEquals("r-1", report.getReportId());
        assertEquals(Severity.ALTA, report.getSeverity());
        assertEquals(at, report.getReportedAt());
    }
}
