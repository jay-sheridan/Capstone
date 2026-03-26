package ca.cars365.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ca.cars365.enums.InspectionRequestStatus;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inspection_requests")
public class InspectionRequest extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inspection_request_id")
	private Long id;

	@Column(nullable = false, unique = true, length = 100)
	private String requestNumber;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	@Builder.Default
	private InspectionRequestStatus status = InspectionRequestStatus.SUBMITTED;

	private LocalDate preferredDate;

	@Column(length = 100)
	private String preferredTimeSlot;

	@Column(columnDefinition = "TEXT")
	private String sellerNotes;

	@Column(nullable = false)
	@Builder.Default
	private Boolean submissionChecklistComplete = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vehicle_id", nullable = false)
	private Vehicle vehicle;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seller_id", nullable = false)
	private Users seller;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inspection_agency_id", nullable = false)
	private InspectionAgency inspectionAgency;

	@OneToMany(mappedBy = "inspectionRequest", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InspectionAppointment> appointments = new ArrayList<>();

	@OneToOne(mappedBy = "inspectionRequest", cascade = CascadeType.ALL, orphanRemoval = true)
	private InspectionReport inspectionReport;

	@OneToOne(mappedBy = "inspectionRequest", cascade = CascadeType.ALL, orphanRemoval = true)
	private CertificationRecord certificationRecord;

	@OneToMany(mappedBy = "inspectionRequest", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InspectionStatusHistory> statusHistory = new ArrayList<>();

	@PrePersist
	public void generateRequestNumber() {
		if (this.requestNumber == null || this.requestNumber.isBlank()) {
			this.requestNumber = "REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
		}
	}
}
