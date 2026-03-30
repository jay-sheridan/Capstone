package ca.cars365.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ca.cars365.entity.InspectionAgency;

@Service
public interface InspectionAgencyService {

	InspectionAgency saveAgency(InspectionAgency agency);

    InspectionAgency getAgencyById(Long id);

    InspectionAgency getAgencyByLicenseNumber(String licenseNumber);

    List<InspectionAgency> getAllAgencies();

    InspectionAgency updateAgency(Long id, InspectionAgency updatedAgency);

    void deleteAgency(Long id);
    
}
