package ca.cars365.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InspectionReportCreateRequest {

	private Long appointmentId;
	private String reportDetails;
}
