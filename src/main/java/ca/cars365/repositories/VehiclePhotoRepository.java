package ca.cars365.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.cars365.entity.VehiclePhoto;

@Repository
public interface VehiclePhotoRepository extends JpaRepository<VehiclePhoto, Long> {
	List<VehiclePhoto> findByVehicleIdOrderByDisplayOrderAsc(Long vehicleId);
	
	VehiclePhoto findFirstByVehicleIdOrderByDisplayOrderAsc(Long vehicleId);
}
