package ca.cars365.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.cars365.dto.response.InspectionAgencyResponse;
import ca.cars365.services.InspectionAgencyService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/seller")
@AllArgsConstructor
public class SellerController {

	private InspectionAgencyService inspectionAgencyService;

	@GetMapping("/profile")
	public ResponseEntity<String> getSellerProfile() {
		return null;
	}

	@GetMapping("/agencies")
	public ResponseEntity<List<InspectionAgencyResponse>> getAllInspectionAgencies() {
		List<InspectionAgencyResponse> agencies = inspectionAgencyService.getAllAgencies();
		return ResponseEntity.ok(agencies);
	}
}
