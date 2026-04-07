package ca.cars365.dto.request;

import ca.cars365.enums.ListingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerVehicleUpdateRequest {
	
	private Double askingPrice;
	private ListingStatus listingStatus;

}
