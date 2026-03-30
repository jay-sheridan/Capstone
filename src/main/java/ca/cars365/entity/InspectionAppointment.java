package ca.cars365.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.cars365.enums.AppointmentStatus;
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


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private AppointmentStatus appointmentStatus = AppointmentStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String bookingNotes;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seller_id", nullable = false)
	private Users seller;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vehicle_id", nullable = false)
	private Vehicle vehicle;
    
    @OneToOne(mappedBy = "inspectionAppointment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
	private InspectionReport inspectionReport;
    
	@Column(nullable = false, unique = true, length = 100)
	private String appointmentNumber;
	
	@PrePersist
	public void generateAppointmentNumber() {
		if (this.appointmentNumber == null || this.appointmentNumber.isBlank()) {
			this.appointmentNumber = "REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
		}
	}
}
