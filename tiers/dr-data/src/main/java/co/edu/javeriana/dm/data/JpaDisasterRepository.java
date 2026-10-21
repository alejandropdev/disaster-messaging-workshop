package co.edu.javeriana.dm.data;

import co.edu.javeriana.dm.domain.model.DisasterReport;
import co.edu.javeriana.dm.domain.repository.DisasterRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class JpaDisasterRepository implements DisasterRepository {

    @PersistenceContext(unitName = "disasterPU")
    private EntityManager em;

    @Override
    public boolean existsByReportId(String reportId) {
        return em.createQuery("select count(r) from DisasterReport r where r.reportId = :id", Long.class)
                .setParameter("id", reportId)
                .getSingleResult() > 0;
    }

    @Override
    public void save(DisasterReport report) {
        em.persist(report);
    }

    @Override
    public List<DisasterReport> findAll() {
        return em.createQuery("select r from DisasterReport r order by r.reportedAt desc", DisasterReport.class)
                .getResultList();
    }
}
