package ca.cars365.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.cars365.dto.response.InspectionAgencyResponse;
import ca.cars365.services.InspectionAgencyService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/seller")
@AllArgsConstructor
public class SellerController {

	private InspectionAgencyService inspectionAgencyService;
	private ca.cars365.repositories.InspectionAppointmentRepository appointments;
	private ca.cars365.repositories.VehicleListingRepository listings;
	private ca.cars365.services.UserService users;
	@GetMapping("/listings")
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public java.util.List<java.util.Map<String, Object>> sellerListings() {
		return listings.findBySellerId(users.getCurrentUserEntity().getId()).stream().map(listing -> {
			java.util.Map<String, Object> result = new java.util.HashMap<>();
			result.put("id", listing.getId()); result.put("title", listing.getTitle());
			result.put("status", listing.getStatus()); result.put("live", Boolean.TRUE.equals(listing.getVisibleToBuyers()) && listing.getStatus() == ca.cars365.enums.ListingStatus.PUBLISHED);
			return result;
		}).toList();
	}

	@GetMapping("/appointments/{id}/status")
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public java.util.Map<String, Object> status(@org.springframework.web.bind.annotation.PathVariable Long id) {
		var appointment = appointments.findById(id).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Appointment not found."));
		if (!appointment.getSeller().getId().equals(users.getCurrentUserEntity().getId())) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "This appointment belongs to another seller.");
		var listing = listings.findByVehicleId(appointment.getVehicle().getId());
		boolean live = listing.map(item -> item.getStatus() == ca.cars365.enums.ListingStatus.PUBLISHED && Boolean.TRUE.equals(item.getVisibleToBuyers())).orElse(false);
		return java.util.Map.of("live", live, "appointmentStatus", appointment.getAppointmentStatus(), "listingStatus", listing.map(item -> item.getStatus().name()).orElse("HIDDEN"));
	}

	@GetMapping("/profile")
	public ResponseEntity<String> getSellerProfile() {
		return null;
	}

	@GetMapping("/agencies")
	public ResponseEntity<List<InspectionAgencyResponse>> getAllInspectionAgencies() {
		List<InspectionAgencyResponse> agencies = inspectionAgencyService.getAllAgencies();
		return ResponseEntity.ok(agencies);
	}
}
