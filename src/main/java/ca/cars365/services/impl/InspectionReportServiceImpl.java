package ca.cars365.services.impl;

import org.springframework.stereotype.Service;

import ca.cars365.entity.InspectionAppointment;
import ca.cars365.entity.InspectionReport;
import ca.cars365.repositories.InspectionReportRepository;
import ca.cars365.services.InspectionReportService;
import ca.cars365.services.VehicleListingService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InspectionReportServiceImpl implements InspectionReportService {

	private InspectionReportRepository inspectionReportRepository;
		
	@Override
	public InspectionReport saveInspectionReport(InspectionReport report) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InspectionReport getInspectionReportById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InspectionReport getInspectionReportByRequestId(Long inspectionRequestId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InspectionReport getInspectionReportByVehicleId(Long vehicleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InspectionReport updateInspectionReport(Long id, InspectionReport updatedReport) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteInspectionReport(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public InspectionReport createInspectionReportForAppointment(InspectionAppointment appointment) {
		
		InspectionReport newReport = InspectionReport.builder()
				
				.vehicle(appointment.getVehicle())
				.inspectionAgency(appointment.getInspectionAgency())
				.inspectionAppointment(appointment)
				.build();
		System.out.println("Creating at : " + newReport.getCreatedAt());
		 return inspectionReportRepository.save(newReport);
	}

	@Override
	public Integer getInspectionScoreForVehicle(Long vehicleId) {
		InspectionReport report = inspectionReportRepository.findByVehicleId(vehicleId).getFirst();
		return report.getScore();
	}
	
	

}
