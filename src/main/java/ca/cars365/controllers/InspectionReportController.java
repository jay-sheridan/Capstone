package ca.cars365.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.cars365.dto.request.InspectionReportCreateRequest;
import ca.cars365.dto.response.InspectionReportResponse;
import ca.cars365.services.InspectionReportService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/inspection/reports")
@AllArgsConstructor
public class InspectionReportController {

	private InspectionReportService inspectionReportService;
	
	@PostMapping
	public ResponseEntity<InspectionReportResponse> createInspectionReport(@RequestBody InspectionReportCreateRequest report) {
//		InspectionReportResponse createdReport = inspectionReportService.saveReport(report);
		return ResponseEntity.ok(null);
}
}