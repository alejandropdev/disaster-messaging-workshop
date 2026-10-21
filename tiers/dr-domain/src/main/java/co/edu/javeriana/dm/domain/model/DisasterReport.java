package co.edu.javeriana.dm.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "disaster_report")
public class DisasterReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String reportId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Instant reportedAt;

    protected DisasterReport() {
    }

    private DisasterReport(DisasterMessage m) {
        this.reportId = m.reportId();
        this.type = m.type();
        this.location = m.location();
        this.severity = m.severity();
        this.description = m.description();
        this.reportedAt = m.reportedAt();
    }

    public static DisasterReport from(DisasterMessage message) {
        return new DisasterReport(message);
    }

    public Long getId() { return id; }
    public String getReportId() { return reportId; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public Severity getSeverity() { return severity; }
    public String getDescription() { return description; }
    public Instant getReportedAt() { return reportedAt; }
}
