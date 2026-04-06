package ca.cars365.services;

import org.springframework.stereotype.Service;

import ca.cars365.entity.InspectionAppointment;
import ca.cars365.entity.InspectionReport;

@Service
public interface InspectionReportService {

	InspectionReport saveInspectionReport(InspectionReport report);

    InspectionReport getInspectionReportById(Long id);

    InspectionReport getInspectionReportByRequestId(Long inspectionRequestId);

    InspectionReport getInspectionReportByVehicleId(Long vehicleId);

    InspectionReport updateInspectionReport(Long id, InspectionReport updatedReport);

    void deleteInspectionReport(Long id);
    
    InspectionReport createInspectionReportForAppointment(InspectionAppointment appointment);
    
    Integer getInspectionScoreForVehicle(Long vehicleId);
}
