package ca.cars365.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class VehicleVinResponse {
	private String engineModel;
    private String make;
    private String model;
    private String modelYear;
    private String fuelTypePrimary;
    private String vehicleType;
    private String plantCountry;
    private String transmissionStyle;
}
