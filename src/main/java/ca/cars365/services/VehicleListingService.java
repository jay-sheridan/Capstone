package ca.cars365.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ca.cars365.dto.response.VehicleListingResponse;
import ca.cars365.entity.InspectionAppointment;
import ca.cars365.entity.Vehicle;
import ca.cars365.enums.ListingStatus;

@Service
public interface VehicleListingService {
	Vehicle saveVehicle(Vehicle vehicle);

    Vehicle getVehicleById(Long id);

    Vehicle getVehicleByVin(String vin);

    List<Vehicle> getVehiclesBySeller(Long sellerId);

    List<Vehicle> getAllVehicles();

    Vehicle updateVehicle(Long id, Vehicle updatedVehicle);

    void deleteVehicle(Long id);

	void createVehicleListingForVehicle(InspectionAppointment createdAppointment, Long id);

	List<VehicleListingResponse> getVisibleListings();

	void updateVehicleListingStatus(Long vehicleId, ListingStatus newStatus);
}
