package ca.cars365.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import ca.cars365.enums.InspectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerVehicleResponse {

    private Long id;
    private String vehicleName;
    private Double askingPrice;

    // VIN Info
    private String vin;
    private String make;
    private String model;
    private Integer modelYear;

    // Photos
    private List<VehiclePhotoResponse> photos;
    private String primaryPhotoUrl;

    // Inspection Info
    private InspectionStatus inspectionStatus;
    private Integer totalAppointments;
    private Integer totalReports;
    private Boolean hasReport;

    // Latest Appointment (optional summary)
    private AppointmentSummaryResponse latestAppointment;

    // Latest Report (optional summary)
    private ReportSummaryResponse latestReport;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
