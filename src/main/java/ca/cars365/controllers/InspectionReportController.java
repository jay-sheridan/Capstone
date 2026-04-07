package ca.cars365.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.cars365.dto.request.InspectionReportCreateRequest;
import ca.cars365.dto.response.InspectionReportResponse;
import ca.cars365.entity.InspectionReport;
import ca.cars365.services.InspectionAgencyService;
import ca.cars365.services.InspectionReportService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/inspection/reports")
@AllArgsConstructor
public class InspectionReportController {

	private InspectionReportService inspectionReportService;
	private InspectionAgencyService inspectionAgencyService;

	@PostMapping(value = {"", "/"})
	public ResponseEntity<InspectionReportResponse> createInspectionReport(
			@RequestBody InspectionReportCreateRequest report) {
		
		InspectionReport existedEmptyReport = inspectionReportService.getInspectionReportById(report.getReportId());
		if (existedEmptyReport.getInspectionAgency().getId() != inspectionAgencyService.getCurrentLoggedInAgency().getId()) {
			throw new RuntimeException("Unauthorized to update this report");
		}
		return ResponseEntity.ok(inspectionReportService.saveInspectionReport(report, existedEmptyReport));
	}
}