package co.edu.javeriana.dm.registry;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public record DisasterMessage(String reportId, String type, String location, Severity severity,
                              String description, Instant reportedAt) implements Serializable {

    public DisasterMessage {
        required(reportId, "reportId");
        required(type, "type");
        required(location, "location");
        if (severity == null) {
            throw new IllegalArgumentException("severity es obligatorio");
        }
        if (reportedAt == null) {
            throw new IllegalArgumentException("reportedAt es obligatorio");
        }
        description = description == null ? "" : description;
    }

    public static DisasterMessage create(String type, String location, Severity severity, String description) {
        return new DisasterMessage(UUID.randomUUID().toString(), type, location, severity, description, Instant.now());
    }

    public String summary() {
        return "[%s] %s en %s. %s".formatted(severity, type, location, description).trim();
    }

    private static void required(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " es obligatorio");
        }
    }
}
