package ca.cars365.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import ca.cars365.dto.response.VehicleVinResponse;
import ca.cars365.services.VehicleService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/vehicles")
@AllArgsConstructor
public class VehicleController {

	private final VehicleService vehicleService;
	
	@GetMapping("/{vehicleId}")
	public ResponseEntity<Object> getVehicleById(@PathVariable Long vehicleId) {
		return null;
	}
	
}
