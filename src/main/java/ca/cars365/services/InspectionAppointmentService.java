package ca.cars365.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ca.cars365.entity.InspectionAppointment;

@Service
public interface InspectionAppointmentService {

	InspectionAppointment saveAppointment(InspectionAppointment appointment);

    InspectionAppointment getAppointmentById(Long id);

    List<InspectionAppointment> getByInspectionRequest(Long inspectionRequestId);

    List<InspectionAppointment> getAllAppointments();

    InspectionAppointment updateAppointment(Long id, InspectionAppointment updatedAppointment);

    void deleteAppointment(Long id);
}
