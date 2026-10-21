package co.edu.javeriana.dm.intake;

import io.vertx.core.json.JsonObject;
import java.time.Instant;
import java.util.UUID;

public record DisasterRequest(String type, String location, String severity, String description) {

    public JsonObject toMessage() {
        required(type, "type");
        required(location, "location");
        required(severity, "severity");
        Severity level;
        try {
            level = Severity.valueOf(severity);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("severity debe ser BAJA, MEDIA o ALTA");
        }
        return new JsonObject()
                .put("reportId", UUID.randomUUID().toString())
                .put("type", type)
                .put("location", location)
                .put("severity", level.name())
                .put("description", description == null ? "" : description)
                .put("reportedAt", Instant.now().toString());
    }

    private static void required(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " es obligatorio");
        }
    }
}
