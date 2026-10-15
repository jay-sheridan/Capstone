package ca.cars365;
import ca.cars365.controllers.ListingDetailsController;
import ca.cars365.entity.*;
import ca.cars365.enums.*;
import ca.cars365.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class ListingDetailsTests {
    @Test void hiddenListingsAndPrivateDocumentsAreNotAccessibleToBuyers() {
        var listings = mock(VehicleListingRepository.class);
        var documents = mock(AppointmentDocumentRepository.class);
        var reports = mock(InspectionReportRepository.class);
        var controller = new ListingDetailsController(listings, documents, reports);
        var listing = VehicleListing.builder().id(1L).status(ListingStatus.READY_TO_PUBLISH).visibleToBuyers(false)
            .vehicle(Vehicle.builder().id(7L).build()).inspectionAgency(InspectionAgency.builder().name("Agency").build()).build();
        when(listings.findById(1L)).thenReturn(Optional.of(listing));
        assertThrows(ResponseStatusException.class, () -> controller.detail(1L));
        assertThrows(ResponseStatusException.class, () -> controller.photo(1L, "private"));
        assertThrows(ResponseStatusException.class, () -> controller.report(1L));
        listing.setStatus(ListingStatus.PUBLISHED); listing.setVisibleToBuyers(true);
        var image = new AppointmentDocument(); image.setId("photo"); image.setName("car.png"); image.setContentType("image/png"); image.setContent(new byte[]{1});
        when(documents.findByAppointmentVehicleIdAndKindOrderBySubmittedAtAsc(7L, "Vehicle photos")).thenReturn(List.of(image));
        when(reports.findByVehicleId(7L)).thenReturn(List.of(InspectionReport.builder().id(2L).overallResult(ReportResult.PASS).score(85).summary("Good condition").build()));
        assertNull(controller.detail(1L).get("inspectionScore")); // Legacy scores use a different formula.
        var scored = InspectionReport.builder().id(3L).overallResult(ReportResult.PASS).scoreVersion(1).approved(true)
            .inspectionDate(java.time.LocalDateTime.now()).engineCondition(ConditionStatus.GOOD).transmissionCondition(ConditionStatus.GOOD)
            .brakeCondition(ConditionStatus.FAIR).tireCondition(ConditionStatus.GOOD).bodyCondition(ConditionStatus.FAIR).frameCondition(ConditionStatus.GOOD)
            .summary("Good condition").build();
        scored.calculateScoreInternal();
        when(reports.findByVehicleId(7L)).thenReturn(List.of(scored));
        listing.getVehicle().setInspectionReports(List.of(scored));
        scored.setOdometerReading(22000);
        listing.getVehicle().setVinDecodeRecord(VinDecodeRecord.builder().modelYear(2020).fuelTypePrimary("Gasoline")
            .bodyClass("Sedan/Saloon").fuelTypeSecondary("Electric").electrificationLevel("PHEV").build());
        var service = new ca.cars365.services.impl.VehicleListingServiceImpl();
        org.springframework.test.util.ReflectionTestUtils.setField(service, "documents", documents);
        var card = service.mapToVehicleListingResponse(listing);
        assertEquals(22000, card.getMileageKm());
        assertEquals("Sedan/Saloon", card.getBodyType());
        assertEquals("Electric", card.getFuelTypeSecondary());
        assertEquals("PHEV", card.getElectrificationLevel());
        assertEquals(85, controller.detail(1L).get("inspectionScore"));
        assertEquals(card.getInspectionScore(), controller.detail(1L).get("inspectionScore"));
        assertEquals(card.getCondition(), controller.detail(1L).get("condition"));
        assertEquals("Very Good", card.getCondition().get("rating"));
        assertEquals(false, card.getCondition().get("certified"));
        assertThrows(ResponseStatusException.class, () -> controller.photo(1L, "private"));
        assertArrayEquals(new byte[]{1}, controller.photo(1L, "photo").getBody());
        assertFalse(controller.report(1L).getBody().containsKey("inspectionAppointment"));
        assertEquals("Good condition", controller.report(1L).getBody().get("summary"));
    }
}
