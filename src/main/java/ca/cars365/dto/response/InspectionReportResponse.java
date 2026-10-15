package ca.cars365.dto.response;

import ca.cars365.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InspectionReportResponse {

	
	private AppointmentStatus appointmentStatus;
	private String vehicleName;
	private String sellerName;
	private String reportNumber;
	private Integer inspectionScore;
    private java.util.Map<String, Object> condition;
	
	
}
