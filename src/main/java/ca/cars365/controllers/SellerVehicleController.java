package ca.cars365.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.cars365.dto.request.SellerVehicleUpdateRequest;
import ca.cars365.dto.response.SellerVehicleResponse;
import ca.cars365.entity.Vehicle;
import ca.cars365.services.UserService;
import ca.cars365.services.VehicleService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/seller/vehicles")
@AllArgsConstructor
public class SellerVehicleController {
	
	private VehicleService vehicleService;
	private UserService userService;
	
	@GetMapping(value = {"/", "" })
	public ResponseEntity<List<SellerVehicleResponse>> getSellerVehicles() {
		return ResponseEntity.ok(vehicleService.getSellerVehicles());
	}
	
	@PutMapping("/{vehicleId}")
	public ResponseEntity<SellerVehicleResponse> updateSellerVehicle(@PathVariable Long vehicleId, @RequestBody SellerVehicleUpdateRequest updateRequest) {
			SellerVehicleResponse updatedVehicle = vehicleService.updateVehicle(vehicleId, updateRequest);
			return ResponseEntity.ok(updatedVehicle);
	}

}
