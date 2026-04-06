package ca.cars365.services;

import org.springframework.stereotype.Service;

@Service
public interface VehiclePhotoService {

	String getPrimaryPhotoUrl(Long vehicleId);
}
