package ca.cars365.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import ca.cars365.dto.response.AgencyInspectionAppointmentResponse;
import ca.cars365.dto.response.InspectionAgencyResponse;
import ca.cars365.entity.InspectionAgency;
import ca.cars365.entity.InspectionAppointment;
import ca.cars365.repositories.InspectionAgencyRepository;
import ca.cars365.services.InspectionAgencyService;
import ca.cars365.services.InspectionAppointmentService;
import ca.cars365.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class InspectionAgencyServiceImpl implements InspectionAgencyService {

	private InspectionAgencyRepository inspectionAgencyRepository;
	private InspectionAppointmentService inspectionAppointmentService; //

	@Override
	public InspectionAgency saveAgency(InspectionAgency agency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InspectionAgency getAgencyById(Long id) {

		return inspectionAgencyRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Inspection agency not found with id: " + id));
	}

	@Override
	public InspectionAgency getAgencyByLicenseNumber(String licenseNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InspectionAgencyResponse> getAllAgencies() {
		// TODO Auto-generated method stub

		List<InspectionAgency> agencies = inspectionAgencyRepository.findAll();

		return agencies.stream()
				.map(agency -> InspectionAgencyResponse.builder().id(agency.getId()).name(agency.getName())
						.address(agency.getAddress()).city(agency.getCity()).provinceState(agency.getProvinceState())
						.postalCode(agency.getPostalCode()).country(agency.getCountry())
						.phoneNumber(agency.getPhoneNumber()).contactPersonName(agency.getContactPersonName()).build())
				.toList();
	}

	@Override
	public InspectionAgency updateAgency(Long id, InspectionAgency updatedAgency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAgency(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<AgencyInspectionAppointmentResponse> getAllAppointmentsForAgency() {

		return mapToAgencyInspectionAppointmentResponse(getCurrentLoggedInAgency()) ;
	}

	public List<AgencyInspectionAppointmentResponse> mapToAgencyInspectionAppointmentResponse(InspectionAgency agency) {

		List<InspectionAppointment> appointments = inspectionAppointmentService.getAppointmentsByAgencyId(agency.getId());
		
		 return appointments.stream()
				 .map(appointment -> AgencyInspectionAppointmentResponse.builder()
						 .id(appointment.getId())
						 .appointmentNumber(appointment.getAppointmentNumber())
						 .appointmentDateTime(appointment.getAppointmentDateTime())
						 .appointmentStatus(appointment.getAppointmentStatus())
						 .bookingNotes(appointment.getBookingNotes())
						 .sellerId(appointment.getSeller().getId())
						 .sellerName(appointment.getSeller().getFullName())
						 .vehicleId(appointment.getVehicle().getId())
						 .vehicleName(appointment.getVehicle().getVehicleName())
						 .inspectionReportId(appointment.getInspectionReport().getId())
						 .reportNumber(appointment.getInspectionReport().getReportNumber())
						 .createdAt(appointment.getCreatedAt())
						 .updatedAt(appointment.getUpdatedAt())
						 .build())
				 .toList();
	}
	
	public InspectionAgency getCurrentLoggedInAgency() {
		
		String licenseNumber = SecurityUtils.getCurrentUserEmail(); // Assuming email is used as the unique identifier
																	// for agencies
		return inspectionAgencyRepository.findByLicenseNumber(licenseNumber).orElseThrow(
				() -> new RuntimeException("Inspection agency not found for license number: " + licenseNumber));
	}

}
