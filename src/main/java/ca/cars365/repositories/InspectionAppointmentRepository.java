package ca.cars365.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.cars365.entity.InspectionAppointment;

@Repository
public interface InspectionAppointmentRepository extends JpaRepository<InspectionAppointment, Long>{

	List<InspectionAppointment> findByAppointmentDateTime(LocalDateTime appointmentDateTime);
	
	List<InspectionAppointment> findByInspectionRequestId(Long inspectionRequestId);
	
	List<InspectionAppointment> findByAppointmentStatus(String appointmentStatus);
	
	List<InspectionAppointment> findByInspectionAgencyId(Long InspectionAgencyId);
	
	boolean existsByVehicleId(Long vehicleId);
	
	boolean existsByInspectionRequestId(Long inspectionRequestId);
	
	
}
