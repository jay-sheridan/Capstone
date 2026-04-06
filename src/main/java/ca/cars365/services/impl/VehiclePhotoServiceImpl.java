package ca.cars365.services.impl;

import org.springframework.stereotype.Service;

import ca.cars365.entity.VehiclePhoto;
import ca.cars365.repositories.VehiclePhotoRepository;
import ca.cars365.services.VehiclePhotoService;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class VehiclePhotoServiceImpl implements VehiclePhotoService {

	private VehiclePhotoRepository vehiclePhotoRepository;
	
	@Override
	public String getPrimaryPhotoUrl(Long vehicleId) {

		VehiclePhoto primaryPhoto = vehiclePhotoRepository.findFirstByVehicleIdOrderByDisplayOrderAsc(vehicleId);
		return primaryPhoto.getFileUrl();
	}

}
