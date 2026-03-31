package ca.cars365.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import ca.cars365.entity.Vehicle;
import ca.cars365.entity.VehicleListing;
import ca.cars365.enums.ListingStatus;
import ca.cars365.repositories.VehicleListingRepository;
import ca.cars365.repositories.VehicleRepository;
import ca.cars365.services.VehicleListingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class VehicleListingServiceImpl implements VehicleListingService {

	private VehicleRepository vehicleRepository;
	private VehicleServiceImpl vehicleService;
	private VehicleListingRepository vehicleListingRepository;
	@Override
	public Vehicle saveVehicle(Vehicle vehicle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vehicle getVehicleById(Long id) {
		return vehicleRepository.findById(id).orElse(null);
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

	@Override
	public void createVehicleListingForVehicle(Long vehicleId) {
		Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
		
		if(vehicle == null) {
			throw new RuntimeException("Vehicle not found with id: " + vehicleId);
		}
		
		VehicleListing newListing = VehicleListing.builder()
				.title(vehicle.getVehicleName())
				.description("Listing for " + vehicle.getVehicleName())
				.status(ListingStatus.HIDDEN)
				.visibleToBuyers(false)
				.vehicle(vehicle)
				.inspectionAgency(vehicle.getInspectionAppointment().get(0).getInspectionAgency())
				.seller(vehicle.getSeller())
				.build();
		
		vehicleListingRepository.save(newListing);
				
	}

}
