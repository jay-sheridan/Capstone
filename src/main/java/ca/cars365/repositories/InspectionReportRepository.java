package ca.cars365.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.cars365.entity.InspectionReport;

@Repository
public interface InspectionReportRepository extends JpaRepository<InspectionReport, Long> {

	
	List<InspectionReport> findByVehicleId(Long vehicleId);
	
	Optional<InspectionReport> findByReportNumber(String reportNumber);
	
	
	
	

}
