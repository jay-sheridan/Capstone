package ca.cars365.services;

import org.springframework.stereotype.Service;

import ca.cars365.dto.response.VehicleVinResponse;

@Service
public interface VehicleService {

	VehicleVinResponse getVehicleInfoByVin(String vin);
	
}
