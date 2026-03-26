package ca.cars365.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.cars365.entity.InspectionAgency;

@Repository
public interface InspectionAgencyRepository extends JpaRepository<InspectionAgency, Long> {

	Optional<InspectionAgency> findByLicenseNumber(String licenseNumber);

    Optional<InspectionAgency> findByEmail(String email);

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByEmail(String email);
    
}
