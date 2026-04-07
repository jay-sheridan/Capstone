package ca.cars365.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.cars365.dto.response.VehicleListingResponse;
import ca.cars365.services.VehicleListingService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/listings")
@AllArgsConstructor
public class VehicleListingController {
	
	private VehicleListingService vehicleListingService;

	@GetMapping(value = {"/", "" })
    public ResponseEntity<List<VehicleListingResponse>> getVisibleListings() {
        return ResponseEntity.ok(vehicleListingService.getVisibleListings());
    }
}
