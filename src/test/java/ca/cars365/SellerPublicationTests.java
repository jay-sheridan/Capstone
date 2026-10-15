package ca.cars365;

import ca.cars365.controllers.SellerController;
import ca.cars365.entity.*;
import ca.cars365.enums.ListingStatus;
import ca.cars365.repositories.*;
import ca.cars365.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SellerPublicationTests {
    @Test void sellerMustOwnListingAndProvidePriceAndConsentAfterInspection() {
        var listings = mock(VehicleListingRepository.class);
        var users = mock(UserService.class);
        var controller = new SellerController(mock(InspectionAgencyService.class), mock(InspectionAppointmentRepository.class), listings, users);
        var owner = Users.builder().id(1L).build();
        var listing = VehicleListing.builder().id(9L).seller(owner).vehicle(Vehicle.builder().build())
            .status(ListingStatus.READY_TO_PUBLISH).visibleToBuyers(false).build();
        when(listings.findById(9L)).thenReturn(Optional.of(listing));
        when(users.getCurrentUserEntity()).thenReturn(Users.builder().id(2L).build());
        assertEquals(403, assertThrows(ResponseStatusException.class, () -> controller.publish(9L, new SellerController.PublishRequest(10000.0, true))).getStatusCode().value());
        when(users.getCurrentUserEntity()).thenReturn(owner);
        for (var request : java.util.List.of(new SellerController.PublishRequest(0.0, true), new SellerController.PublishRequest(Double.NaN, true), new SellerController.PublishRequest(10000.0, false))) {
            assertEquals(400, assertThrows(ResponseStatusException.class, () -> controller.publish(9L, request)).getStatusCode().value());
            assertFalse(listing.getVisibleToBuyers());
        }
        listing.setStatus(ListingStatus.INSPECTED);
        assertEquals(409, assertThrows(ResponseStatusException.class, () -> controller.publish(9L, new SellerController.PublishRequest(10000.0, true))).getStatusCode().value());
        listing.setStatus(ListingStatus.READY_TO_PUBLISH);
        controller.publish(9L, new SellerController.PublishRequest(10000.0, true));
        assertTrue(listing.getVisibleToBuyers());
        assertEquals(ListingStatus.PUBLISHED, listing.getStatus());
        assertEquals(10000.0, listing.getListingPrice());
        assertNotNull(listing.getPublishedAt());
        verify(listings).save(listing);
    }
}
