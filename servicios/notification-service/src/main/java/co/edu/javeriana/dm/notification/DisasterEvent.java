package co.edu.javeriana.dm.notification;

public record DisasterEvent(String reportId, String type, String location, String severity, String description) {

    public String summary() {
        return "[%s] %s en %s. %s".formatted(severity, type, location, description == null ? "" : description).trim();
    }
}
