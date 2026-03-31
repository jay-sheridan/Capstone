package ca.cars365.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehiclePhotoResponse {

    private Long id;
    private String fileUrl;
    private Boolean primaryPhoto;
    private Integer displayOrder;
}