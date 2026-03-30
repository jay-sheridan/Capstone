package ca.cars365.entity;

import java.time.LocalDate;
import java.util.UUID;

import ca.cars365.enums.ReportResult;
import jakarta.persistence.CascadeType;
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
@Table(name = "inspection_reports")
@AllArgsConstructor
	@NoArgsConstructor
	@Builder
public class InspectionReport extends BaseEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_report_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String reportNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ReportResult overallResult;

    @Column(columnDefinition = "TEXT")
    private String summary;

    private Boolean odometerVerified;
    private Boolean vinVerified;
    private String engineCondition;
    private String transmissionCondition;
    private String brakeCondition;
    private String tireCondition;
    private String bodyCondition;
    private String frameCondition;
    private Boolean safetyCheckPassed;
    private Boolean emissionCheckPassed;

    @Column(columnDefinition = "TEXT")
    private String recommendedRepairs;

    private LocalDate inspectionDate;


    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_listing_id", nullable = false, unique = true)
    private VehicleListing vehicleListing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_agency_id", nullable = false)
    private InspectionAgency inspectionAgency;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_appointment_id", nullable = false, unique = true)
    private InspectionAppointment inspectionAppointment;	

    @PrePersist
    public void generateReportNumber() {
        if (this.reportNumber == null || this.reportNumber.isBlank()) {
            this.reportNumber = "RPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }
}
