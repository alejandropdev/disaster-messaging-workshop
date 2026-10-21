package co.edu.javeriana.dm.business;

import co.edu.javeriana.dm.domain.model.DisasterMessage;
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
