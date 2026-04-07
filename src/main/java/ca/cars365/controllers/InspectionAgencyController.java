package ca.cars365.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.cars365.dto.response.AgencyInspectionAppointmentResponse;
import ca.cars365.dto.response.InspectionAgencyResponse;
import ca.cars365.services.InspectionAgencyService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/agencies")
@AllArgsConstructor
public class InspectionAgencyController {
	
	private InspectionAgencyService inspectionAgencyService;
	
	
	@GetMapping("/inspection/appointments")
	public ResponseEntity<List<AgencyInspectionAppointmentResponse>> getInspectionsByAgency() {
		
		return ResponseEntity.ok(inspectionAgencyService.getAllAppointmentsForAgency());
		
	}
	

}
