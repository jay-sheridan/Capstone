package ca.cars365.services;

import org.springframework.stereotype.Service;

import ca.cars365.dto.request.InspectionReportCreateRequest;
import ca.cars365.dto.response.InspectionReportResponse;
import ca.cars365.entity.InspectionAppointment;
import ca.cars365.entity.InspectionReport;

@Service
public interface InspectionReportService {

	InspectionReportResponse saveInspectionReport(InspectionReportCreateRequest report, InspectionReport existedEmptyReport);

    InspectionReport getInspectionReportById(Long id);

    InspectionReport getInspectionReportByRequestId(Long inspectionRequestId);

    InspectionReport getInspectionReportByVehicleId(Long vehicleId);

    InspectionReport updateInspectionReport(Long id, InspectionReport updatedReport);

    void deleteInspectionReport(Long id);
    
    InspectionReport createInspectionReportForAppointment(InspectionAppointment appointment);
    
    Integer getInspectionScoreForVehicle(Long vehicleId);
}
