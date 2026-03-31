package ca.cars365.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionAgencyResponse {
	private Long id;

    private String name;

    private String address;

    private String city;

    private String provinceState;

    private String postalCode;

    private String country;

    private String phoneNumber;

    private String contactPersonName;

}
