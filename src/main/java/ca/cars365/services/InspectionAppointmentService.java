package ca.cars365.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ca.cars365.dto.request.InspectionAppointmentCreateRequest;
import ca.cars365.dto.response.InspectionAppointmentResponse;
import ca.cars365.entity.InspectionAgency;
import ca.cars365.entity.InspectionAppointment;

@Service
public interface InspectionAppointmentService {

	InspectionAppointmentResponse saveAppointment(InspectionAppointmentCreateRequest appointment, InspectionAgency agency);

    InspectionAppointment getAppointmentById(Long id);

    List<InspectionAppointment> getByInspectionRequest(Long inspectionRequestId);

    List<InspectionAppointment> getAllAppointments();

    InspectionAppointment updateAppointment(Long id, InspectionAppointment updatedAppointment);

    List<InspectionAppointment> getAppointmentsByAgencyId(Long agencyId);
    
    void deleteAppointment(Long id);

	void markAppointmentAsCompleted(Long inspectionAppointmentId);
}
