package ca.cars365.services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ca.cars365.dto.response.VehicleVinResponse;
import ca.cars365.entity.Vehicle;
import ca.cars365.exception.InvalidVinException;
import ca.cars365.services.VehicleService;

@Service
public class VehicleServiceImpl implements VehicleService {

	@Override
	public Vehicle saveVehicle(Vehicle vehicle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vehicle getVehicleById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vehicle getVehicleByVin(String vin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vehicle> getVehiclesBySeller(Long sellerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vehicle> getAllVehicles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteVehicle(Long id) {
		// TODO Auto-generated method stub
		
	}

}
