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
	private ca.cars365.repositories.InspectionReportRepository reports;

	@PostMapping(value = {"", "/"})
	public ResponseEntity<InspectionReportResponse> createInspectionReport(
			@RequestBody InspectionReportCreateRequest report) {
		
		InspectionReport existedEmptyReport = reports.findByInspectionAppointmentId(report.getInspectionAppointmentId())
			.orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Report not found for this appointment."));
		if (!existedEmptyReport.getInspectionAgency().getId().equals(inspectionAgencyService.getCurrentLoggedInAgency().getId())) {
			throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Only the assigned agency can update this report.");
		}
		if (Boolean.TRUE.equals(report.getApprove()) && report.getOverallResult() != ca.cars365.enums.ReportResult.PASS) {
			throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Only a passing inspection can be approved and published.");
		}
		report.setReportId(existedEmptyReport.getId());
		report.setVehicleId(existedEmptyReport.getVehicle().getId());
		return ResponseEntity.ok(inspectionReportService.saveInspectionReport(report, existedEmptyReport));
	}

	@org.springframework.web.bind.annotation.GetMapping("/agency/{agencyId}")
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public java.util.List<java.util.Map<String, Object>> getReports(@org.springframework.web.bind.annotation.PathVariable Long agencyId) {
		Long current = inspectionAgencyService.getCurrentLoggedInAgency().getId();
		if (!current.equals(agencyId)) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Only your agency reports are available.");
		return reports.findByInspectionAgencyId(current).stream().map(report -> {
			java.util.Map<String, Object> result = new java.util.HashMap<>();
			result.put("id", report.getId()); result.put("reportNumber", report.getReportNumber());
			result.put("vehicleId", report.getVehicle().getId()); result.put("overallResult", report.getOverallResult());
			result.put("inspectionDate", report.getInspectionDate()); return result;
		}).toList();
	}
}
