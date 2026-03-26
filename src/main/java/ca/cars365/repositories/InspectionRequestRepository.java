package ca.cars365.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.cars365.entity.InspectionRequest;

@Repository
public interface InspectionRequestRepository extends JpaRepository<InspectionRequest, Long> {

	Optional<InspectionRequest> findByRequestNumber(String requestNumber);
	
	List<InspectionRequest> findBySellerId(Long sellerId);
	
	List<InspectionRequest> findByVehicleId(Long vehicleId);
	
	List<InspectionRequest> findByInspectionAgencyId(Long inspectionAgencyId);
	
	List<InspectionRequest> findByStatus(String status);
}
