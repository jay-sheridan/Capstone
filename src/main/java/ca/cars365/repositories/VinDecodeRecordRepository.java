package ca.cars365.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.cars365.entity.VinDecodeRecord;

@Repository
public interface VinDecodeRecordRepository extends JpaRepository<VinDecodeRecord, Long> {

	VinDecodeRecord findByVin(String vin);
	
	Optional<VinDecodeRecord> findByVehicleId(Long vehicleId);
	
	boolean existsByVin(String vin);

}
