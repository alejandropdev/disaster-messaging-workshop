package co.edu.javeriana.dm.intake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DisasterRequestTest {

    @Test
    void typeIsRequired() {
        assertThrows(IllegalArgumentException.class,
                () -> new DisasterRequest(" ", "Bogotá", "ALTA", null).toMessage());
    }

    @Test
    void severityMustBeKnown() {
        assertThrows(IllegalArgumentException.class,
                () -> new DisasterRequest("Sismo", "Bogotá", "CRITICA", null).toMessage());
    }

    @Test
    void messageCarriesFieldsAndFreshId() {
        var request = new DisasterRequest("Sismo", "Bogotá", "ALTA", "6.1");
        var a = request.toMessage();
        var b = request.toMessage();
        assertEquals("Sismo", a.getString("type"));
        assertEquals("ALTA", a.getString("severity"));
        assertNotEquals(a.getString("reportId"), b.getString("reportId"));
    }
}
