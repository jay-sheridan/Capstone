package ca.cars365.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InspectionReportResponse {

	private Long id;
	private String reportNumber;
	private Long inspectionAppointmentId;
	private String inspectionAppointmentNumber;
	private Long vehicleId;
	private String vehicleName;
	private String inspectionAgencyName;
	private String reportDetailsUrl;
}
