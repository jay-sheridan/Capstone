package ca.cars365.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ca.cars365.exception.InspectionAgencyNotFoundException;
import ca.cars365.repositories.InspectionAgencyRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InspectionAgencyDetailsService {
	
	private final InspectionAgencyRepository inspectionAgencyRepository;
	
	public UserDetails loadByLicenseNumber(String licenseNumber) {
        return inspectionAgencyRepository.findByLicenseNumber(licenseNumber)
        		.orElseThrow(() -> {
					throw new InspectionAgencyNotFoundException("Inspection agency not found with license number: " + licenseNumber);
				});
    }

}
