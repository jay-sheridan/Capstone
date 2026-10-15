package ca.cars365.controllers;

import ca.cars365.entity.InspectionReport;
import ca.cars365.enums.ListingStatus;
import ca.cars365.repositories.VehicleListingRepository;
import ca.cars365.services.GeminiPriceService;
import ca.cars365.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/seller/listings")
@RequiredArgsConstructor
public class PriceSuggestionController {
    private final VehicleListingRepository listings;
    private final UserService users;
    private final GeminiPriceService pricing;

    @PostMapping("/{id}/price-suggestion")
    @Transactional(readOnly = true)
    public GeminiPriceService.Suggestion suggest(@PathVariable Long id) {
        var listing = listings.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Listing not found."));
        if (!listing.getSeller().getId().equals(users.getCurrentUserEntity().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Only the owner can request a price suggestion.");
        if (listing.getStatus() != ListingStatus.READY_TO_PUBLISH)
            throw new ResponseStatusException(HttpStatus.CONFLICT,"The agency must approve the inspection first.");
        var report = listing.getVehicle().getInspectionReports().stream().max(java.util.Comparator.comparing(InspectionReport::getId)).orElse(null);
        return pricing.suggest(listing.getVehicle(), report);
    }
}
