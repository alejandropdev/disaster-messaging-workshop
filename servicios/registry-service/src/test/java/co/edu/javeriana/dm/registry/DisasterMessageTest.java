package co.edu.javeriana.dm.registry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DisasterMessageTest {

    @Test
    void messageRequiresType() {
        assertThrows(IllegalArgumentException.class,
                () -> DisasterMessage.create(" ", "Bogotá", Severity.ALTA, null));
    }

    @Test
    void messageRequiresSeverity() {
        assertThrows(IllegalArgumentException.class,
                () -> DisasterMessage.create("Sismo", "Bogotá", null, null));
    }

    @Test
    void eachMessageGetsItsOwnId() {
        var a = DisasterMessage.create("Sismo", "Bogotá", Severity.ALTA, "6.1");
        var b = DisasterMessage.create("Sismo", "Bogotá", Severity.ALTA, "6.1");
        assertNotEquals(a.reportId(), b.reportId());
    }

    @Test
    void summaryIncludesSeverityAndPlace() {
        var m = DisasterMessage.create("Inundación", "Cali", Severity.MEDIA, "Río Cauca desbordado");
        assertEquals("[MEDIA] Inundación en Cali. Río Cauca desbordado", m.summary());
    }
}
