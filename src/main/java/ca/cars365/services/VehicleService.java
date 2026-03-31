package ca.cars365.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ca.cars365.dto.response.SellerVehicleResponse;
import ca.cars365.entity.Vehicle;
import ca.cars365.entity.VinDecodeRecord;

@Service
public interface VehicleService {

	
	Vehicle saveVehicle(Vehicle vehicle);

    Vehicle getVehicleById(Long id);

    Vehicle getVehicleByVin(String vin);

    List<Vehicle> getVehiclesBySeller(Long sellerId);

    List<Vehicle> getAllVehicles();

    Vehicle updateVehicle(Long id, Vehicle updatedVehicle);

    void deleteVehicle(Long id);
	
    Vehicle createVehicleFromVin(String vin);

	List<SellerVehicleResponse> getSellerVehicles();
}
