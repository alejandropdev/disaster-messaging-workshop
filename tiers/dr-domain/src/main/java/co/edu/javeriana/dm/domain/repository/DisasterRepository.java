package co.edu.javeriana.dm.domain.repository;

import co.edu.javeriana.dm.domain.model.DisasterReport;
import java.util.List;

public interface DisasterRepository {

    boolean existsByReportId(String reportId);

    void save(DisasterReport report);

    List<DisasterReport> findAll();
}
