package ca.cars365.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import ca.cars365.dto.request.InspectionAppointmentCreateRequest;
import ca.cars365.dto.response.InspectionAppointmentResponse;
import ca.cars365.entity.InspectionAgency;
import ca.cars365.entity.InspectionAppointment;
import ca.cars365.enums.AppointmentStatus;
import ca.cars365.repositories.InspectionAppointmentRepository;
import ca.cars365.services.InspectionAgencyService;
import ca.cars365.services.InspectionAppointmentService;
import ca.cars365.services.InspectionReportService;
import ca.cars365.services.UserService;
import ca.cars365.services.VehicleListingService;
import ca.cars365.services.VehicleService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InspectionAppointmentServiceImpl implements InspectionAppointmentService {

	private InspectionAppointmentRepository inspectionAppointmentRepository;
	private UserService userService;
	private VehicleService vehicleService;
	private InspectionReportService inspectionReportService;
	private VehicleListingService vehicleListingService;
	
	@Override
	public InspectionAppointmentResponse saveAppointment(InspectionAppointmentCreateRequest appointment, InspectionAgency agency) {

		InspectionAppointment newAppointment  = InspectionAppointment.builder()
				.inspectionAgency(agency)
				.seller(userService.getCurrentUserEntity())
				.vehicle(vehicleService.getVehicleByVin(appointment.getVin()))
				.appointmentDateTime(appointment.getAppointmentDateTime())
				.appointmentStatus(AppointmentStatus.CONFIRMED)
				.build();
		System.out.println("Saving appointment: ");
		InspectionAppointment createdAppointment =  inspectionAppointmentRepository.save(newAppointment);
		
		System.out.println("Created appointment: " );
		inspectionReportService.createInspectionReportForAppointment(createdAppointment);
		
		vehicleListingService.createVehicleListingForVehicle(createdAppointment.getVehicle().getId());
		
		 InspectionAppointmentResponse response = InspectionAppointmentResponse.builder()
				 .id(createdAppointment.getId())
				 .appointmentNumber(createdAppointment.getAppointmentNumber())
				 .inspectionAgencyId(createdAppointment.getInspectionAgency().getId())
				 .inspectionAgencyName(createdAppointment.getInspectionAgency().getName())
				 .sellerName(createdAppointment.getSeller().getFullName())
				 .sellerId(createdAppointment.getSeller().getId())
				 .vehicleId(createdAppointment.getVehicle().getId())
				 .vehicleName(createdAppointment.getVehicle().getVehicleName())
				 .appointmentDateTime(createdAppointment.getAppointmentDateTime())
				 .appointmentStatus(createdAppointment.getAppointmentStatus())
				 .createdAt(createdAppointment.getCreatedAt())
				 .updatedAt(createdAppointment.getUpdatedAt())
				 .build();
		 
		 return response;
				
	}

	@Override
	public InspectionAppointment getAppointmentById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InspectionAppointment> getByInspectionRequest(Long inspectionRequestId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InspectionAppointment> getAllAppointments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InspectionAppointment updateAppointment(Long id, InspectionAppointment updatedAppointment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAppointment(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<InspectionAppointment> getAppointmentsByAgencyId(Long agencyId) {
		
		return inspectionAppointmentRepository.findByInspectionAgencyId(agencyId);
	}

}
