package ca.cars365.dto.response;

import java.time.LocalDateTime;

import ca.cars365.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencyInspectionAppointmentResponse {

	private Long id;

    private String appointmentNumber;

    private LocalDateTime appointmentDateTime;

    private AppointmentStatus appointmentStatus;

    private String bookingNotes;

    // Seller info (lightweight)
    private Long sellerId;
    private String sellerName;

    // Vehicle info (lightweight)
    private Long vehicleId;
    private String vehicleName;

    // Report info (optional)
    private Long inspectionReportId;
    private String reportNumber;

    // timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
