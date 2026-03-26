package ca.cars365.entity;

import java.time.LocalDateTime;

import ca.cars365.enums.AppointmentStatus;
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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inspection_appointments")
public class InspectionAppointment extends BaseEntity {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_appointment_id")
    private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inspection_agency_id", nullable = false)
	private InspectionAgency inspectionAgency;

    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    private Integer estimatedDurationMinutes;

    @Column(length = 255)
    private String locationAddress;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String provinceState;

    @Column(length = 30)
    private String postalCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private AppointmentStatus appointmentStatus = AppointmentStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String bookingNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_request_id", nullable = false)
    private InspectionRequest inspectionRequest;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    
}
