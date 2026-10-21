package co.edu.javeriana.dm.domain.service;

import co.edu.javeriana.dm.domain.model.DisasterMessage;
import co.edu.javeriana.dm.domain.model.DisasterReport;
import java.util.List;

public interface DisasterRegistry {

    void register(DisasterMessage message);

    List<DisasterReport> list();
}
