package ca.cars365.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import ca.cars365.entity.InspectionAgency;
import ca.cars365.repositories.InspectionAgencyRepository;
import ca.cars365.services.InspectionAgencyService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InspectionAgencyServiceImpl implements InspectionAgencyService{

	private InspectionAgencyRepository inspectionAgencyRepository;
	@Override
	public InspectionAgency saveAgency(InspectionAgency agency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InspectionAgency getAgencyById(Long id) {
		
		return inspectionAgencyRepository.findById(id).orElseThrow(() -> new RuntimeException("Inspection agency not found with id: " + id));
	}

	@Override
	public InspectionAgency getAgencyByLicenseNumber(String licenseNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InspectionAgency> getAllAgencies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InspectionAgency updateAgency(Long id, InspectionAgency updatedAgency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAgency(Long id) {
		// TODO Auto-generated method stub
		
	}

}
