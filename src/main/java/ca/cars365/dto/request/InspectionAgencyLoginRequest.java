package ca.cars365.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionAgencyLoginRequest {

	private String licenseNumber;
	private String password;
}