package ca.cars365.dto.response;

import java.time.LocalDateTime;

import ca.cars365.enums.InspectionStatus;
import ca.cars365.enums.ListingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleListingResponse {

	private Long id;
    private java.util.List<String> photoIds;
    private String title;
    private String description;
    private Double listingPrice;
    private Integer mileageKm;
    private Integer modelYear;
    private String fuelType;
    private String fuelTypeSecondary;
    private String electrificationLevel;
    private String bodyType;
    private ListingStatus status;
    private String inspectionAgencyName;
    private String imageUrl;
    private Integer inspectionScore;
    private java.util.Map<String, Object> condition;
    
    
    private LocalDateTime publishedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
