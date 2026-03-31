package ca.cars365.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.cars365.dto.request.InspectionAppointmentCreateRequest;
import ca.cars365.dto.response.InspectionAppointmentResponse;
import ca.cars365.entity.InspectionAppointment;
import ca.cars365.services.InspectionAppointmentService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/inspection/appointments")
@AllArgsConstructor
public class InspectionAppointmentController {
	
	private InspectionAppointmentService inspectionAppointmentService;

	@PostMapping
	public ResponseEntity<InspectionAppointmentResponse> createInspectionAppointment(@RequestBody InspectionAppointmentCreateRequest appointment) {
		
		InspectionAppointmentResponse createdAppointment = inspectionAppointmentService.saveAppointment(appointment);
		return ResponseEntity.ok(createdAppointment);
			
	}
}
