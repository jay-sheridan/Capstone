package ca.cars365.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.cars365.entity.CertificationRecord;

@Repository
public interface CertificationRecordRepository extends JpaRepository<CertificationRecord, Long> {

	Optional<CertificationRecord> findByVehicleId(Long vehicleId);
	
	Optional<CertificationRecord> findByCertificateNumber(String certificationNumber);
	
	Optional<CertificationRecord> findByInspectionRequestId(Long inspectionRequestId);
	
	
}
