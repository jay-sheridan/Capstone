package ca.cars365.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.cars365.entity.VehicleListing;

@Repository
public interface VehicleListingRepository extends JpaRepository<VehicleListing, Long> {

	Optional<VehicleListing> findByVehicleId(Long vehicleId);

	boolean existsByVehicleId(Long vehicleId);

	List<VehicleListing> findBySellerId(Long sellerId);

	List<VehicleListing> findByInspectionAgencyId(Long inspectionAgencyId);

	List<VehicleListing> findByStatus(String status);

	List<VehicleListing> findByVisibleToBuyersTrue();
}
