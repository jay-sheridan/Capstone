package ca.cars365;

import ca.cars365.controllers.InspectionReportController;
import ca.cars365.dto.request.InspectionReportCreateRequest;
import ca.cars365.entity.*;
import ca.cars365.enums.*;
import ca.cars365.repositories.*;
import ca.cars365.services.*;
import ca.cars365.services.impl.*;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportPublicationTests {
    @Test void onlyAssignedAgencyCanApproveAndFailedReportsCannotPublish() {
        var reports = mock(InspectionReportRepository.class);
        var agencies = mock(InspectionAgencyService.class);
        var service = mock(InspectionReportService.class);
        var controller = new InspectionReportController(service, agencies, reports);
        var report = InspectionReport.builder().inspectionAgency(InspectionAgency.builder().id(2L).build())
                .vehicle(Vehicle.builder().id(9L).build()).build();
        when(reports.findByInspectionAppointmentId(3L)).thenReturn(Optional.of(report));
        when(agencies.getCurrentLoggedInAgency()).thenReturn(InspectionAgency.builder().id(4L).build());
        var request = InspectionReportCreateRequest.builder().inspectionAppointmentId(3L).approve(true).overallResult(ReportResult.PASS).build();
        assertEquals(403, assertThrows(ResponseStatusException.class, () -> controller.createInspectionReport(request)).getStatusCode().value());
        when(agencies.getCurrentLoggedInAgency()).thenReturn(InspectionAgency.builder().id(2L).build());
        request.setOverallResult(ReportResult.FAIL);
        assertEquals(400, assertThrows(ResponseStatusException.class, () -> controller.createInspectionReport(request)).getStatusCode().value());
        verifyNoInteractions(service);
    }

    @Test void explicitApprovalPublishesButSaveAloneKeepsListingInspected() {
        var reports = mock(InspectionReportRepository.class);
        var listings = mock(VehicleListingService.class);
        var appointments = mock(InspectionAppointmentRepository.class);
        var service = new InspectionReportServiceImpl(reports, listings, appointments);
        var seller = Users.builder().fullName("Seller").build();
        var vehicle = Vehicle.builder().id(9L).vehicleName("Honda Civic").build();
        var appointment = InspectionAppointment.builder().id(3L).seller(seller).vehicle(vehicle).build();
        var report = InspectionReport.builder().vehicle(vehicle).inspectionAppointment(appointment).build();
        when(reports.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(appointments.findById(3L)).thenReturn(Optional.of(appointment));
        var request = InspectionReportCreateRequest.builder().vehicleId(9L).inspectionAppointmentId(3L).overallResult(ReportResult.PASS).build();
        service.saveInspectionReport(request, report);
        verify(listings).updateVehicleListingStatus(9L, ListingStatus.INSPECTED);
        request.setApprove(true);
        service.saveInspectionReport(request, report);
        verify(listings).updateVehicleListingStatus(9L, ListingStatus.PUBLISHED);
    }

    @Test void publishingMakesListingVisibleToBuyers() {
        var vehicles = mock(VehicleRepository.class);
        var listings = mock(VehicleListingRepository.class);
        var service = new VehicleListingServiceImpl();
        ReflectionTestUtils.setField(service, "vehicleRepository", vehicles);
        ReflectionTestUtils.setField(service, "vehicleListingRepository", listings);
        var vehicle = Vehicle.builder().id(9L).build();
        var listing = VehicleListing.builder().vehicle(vehicle).visibleToBuyers(false).build();
        when(vehicles.findById(9L)).thenReturn(Optional.of(vehicle));
        when(listings.findByVehicleId(9L)).thenReturn(Optional.of(listing));
        service.updateVehicleListingStatus(9L, ListingStatus.PUBLISHED);
        assertTrue(listing.getVisibleToBuyers());
        assertNotNull(listing.getPublishedAt());
        assertEquals(InspectionStatus.APPROVED, vehicle.getInspectionStatus());
    }
}
