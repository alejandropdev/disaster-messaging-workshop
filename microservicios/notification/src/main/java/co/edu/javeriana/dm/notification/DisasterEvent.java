package co.edu.javeriana.dm.notification;

import io.vertx.core.json.JsonObject;

public record DisasterEvent(String reportId, String type, String location, String severity, String description) {

    public static DisasterEvent from(JsonObject m) {
        return new DisasterEvent(m.getString("reportId"), m.getString("type"), m.getString("location"),
                m.getString("severity"), m.getString("description"));
    }

    public String summary() {
        return "[%s] %s en %s. %s".formatted(severity, type, location, description == null ? "" : description).trim();
    }
}
