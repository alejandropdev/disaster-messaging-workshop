package co.edu.javeriana.dm.registry;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

final class MessageJson {

    private static final Jsonb JSONB = JsonbBuilder.create();

    private MessageJson() {
    }

    static String toJson(DisasterMessage message) {
        return JSONB.toJson(message);
    }

    static DisasterMessage fromJson(String json) {
        return JSONB.fromJson(json, DisasterMessage.class);
    }
}
