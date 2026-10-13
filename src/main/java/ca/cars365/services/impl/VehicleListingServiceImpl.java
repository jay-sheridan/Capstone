package ca.cars365.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.cars365.dto.response.VehicleListingResponse;
import ca.cars365.entity.InspectionAppointment;
import ca.cars365.entity.Vehicle;
import ca.cars365.entity.VehicleListing;
import ca.cars365.enums.InspectionStatus;
import ca.cars365.enums.ListingStatus;
import ca.cars365.repositories.VehicleListingRepository;
import ca.cars365.repositories.VehicleRepository;
import ca.cars365.services.VehicleListingService;
import ca.cars365.services.VehiclePhotoService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class VehicleListingServiceImpl implements VehicleListingService {

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private VehicleListingRepository vehicleListingRepository;
	@Autowired
	private VehiclePhotoService vehiclePhotoService;
	
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
	public void createVehicleListingForVehicle(InspectionAppointment createdAppointment,Long vehicleId) {
		
		Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);

		if (vehicle == null) {
			throw new RuntimeException("Vehicle not found with id: " + vehicleId);
		}

		VehicleListing newListing = VehicleListing.builder().title(vehicle.getVehicleName())
				.description("Listing for " + vehicle.getVehicleName()).status(ListingStatus.HIDDEN)
				.visibleToBuyers(false).vehicle(vehicle)
				.inspectionAgency(createdAppointment.getInspectionAgency())
				.seller(vehicle.getSeller()).build();

		vehicleListingRepository.save(newListing);

	}
	
	public VehicleListingResponse mapToVehicleListingResponse(VehicleListing listing) {
		return VehicleListingResponse.builder()
				.id(listing.getId())
				.title(listing.getTitle())
				.description(listing.getDescription())
				.listingPrice(listing.getListingPrice())
				.status(listing.getStatus())
				.inspectionAgencyName(listing.getInspectionAgency().getName())
//				.imageUrl(vehiclePhotoService.getPrimaryPhotoUrl(listing.getVehicle().getId()))
				.imageUrl(null) // Placeholder for image URL, implement as needed
				.publishedAt(listing.getPublishedAt())
				.inspectionScore(vehicleRepository.findById(listing.getVehicle().getId()).get().getInspectionReports().get(0).getScore())
				.createdAt(listing.getCreatedAt())
				.updatedAt(listing.getUpdatedAt())
				.build();
	}

	@Override
	public List<VehicleListingResponse> getVisibleListings() {

		List<VehicleListing> listings = vehicleListingRepository.findByVisibleToBuyersTrue();
		
		return listings.stream().map(this::mapToVehicleListingResponse).toList();
	}

	@Override
	public void updateVehicleListingStatus(Long vehicleId, ListingStatus newStatus) {
		Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
		
		if (vehicle == null) {
			throw new RuntimeException("Vehicle not found with id: " + vehicleId);
		}
		vehicle.setInspectionStatus(newStatus == ListingStatus.PUBLISHED ? InspectionStatus.APPROVED : InspectionStatus.COMPLETED);
		vehicleRepository.save(vehicle);
		VehicleListing listing = vehicleListingRepository.findByVehicleId(vehicleId).orElse(null);
		
		if (listing == null) {
			throw new RuntimeException("Vehicle Listing not found for vehicle id: " + vehicleId);
		}
		
		listing.setStatus(newStatus);
		
		if (newStatus == ListingStatus.HIDDEN) {
		
			listing.setVisibleToBuyers(false);
		}
			
		if (newStatus == ListingStatus.PUBLISHED) {
			listing.setVisibleToBuyers(true);
			listing.setPublishedAt(java.time.LocalDateTime.now());
		} else {
			listing.setVisibleToBuyers(false);
			listing.setPublishedAt(null);
		}
		
		vehicleListingRepository.save(listing);
		
	}

}
