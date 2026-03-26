package ca.cars365.entity;

import java.time.LocalDate;
import java.util.UUID;

import ca.cars365.enums.CertificationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "certification_records")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificationRecord extends BaseEntity {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_record_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String certificateNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private CertificationStatus status = CertificationStatus.PENDING;

    private LocalDate issuedAt;
    private LocalDate expiresAt;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(columnDefinition = "TEXT")
    private String holdReason;

    @Column(length = 500)
    private String publicReportUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_request_id", nullable = false, unique = true)
    private InspectionRequest inspectionRequest;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_report_id", nullable = false, unique = true)
    private InspectionReport inspectionReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_by_agency_id")
    private InspectionAgency issuedByAgency;

    @PrePersist
    public void generateCertificateNumber() {
        if (this.certificateNumber == null || this.certificateNumber.isBlank()) {
            this.certificateNumber = "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }
}
