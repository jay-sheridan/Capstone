package ca.cars365.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.cars365.dto.response.VehicleVinResponse;
import ca.cars365.entity.VinDecodeRecord;
import ca.cars365.services.VehicleService;
import ca.cars365.services.VinDecodeRecordService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/vin-decode")
@AllArgsConstructor
public class VinDecodeController {
	
	private final VehicleService vehicleService;
	private final VinDecodeRecordService vinDecodeService;
	
//	@GetMapping("/vin/{vin}")
//	public ResponseEntity<Object> getVehicleByVin(@PathVariable String vin) {
//
//		String url = "https://vpic.nhtsa.dot.gov/api/vehicles/decodevinvalues/" 
//		              + vin + "?format=json";
//
//		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
//		
//		return ResponseEntity.ok(response.getBody());
//	}

	@GetMapping("/{vin}")
	public ResponseEntity<VehicleVinResponse> getVehicleByVin(@PathVariable String vin) {
	    return ResponseEntity.ok(vinDecodeService.decodeVin(vin));
	}
	
	@GetMapping("/records/{vehicleId}")
    public ResponseEntity<VinDecodeRecord> getVinRecordByVehicleId(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(vinDecodeService.getByVehicleId(vehicleId));
    }
	
	@GetMapping("/records/by-vin/{vin}")
    public ResponseEntity<VinDecodeRecord> getVinRecordByVin(@PathVariable String vin) {
        return ResponseEntity.ok(vinDecodeService.getByVin(vin));
    }
}