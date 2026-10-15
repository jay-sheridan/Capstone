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
    @Test void reportJsonAcceptsNullableMileageAndTracksWhetherItWasProvided() {
        var json = tools.jackson.databind.json.JsonMapper.builder().build();
        var absent = json.readValue("{\"overallResult\":\"PASS\"}", InspectionReportCreateRequest.class);
        assertFalse(Boolean.TRUE.equals(absent.getOdometerReadingProvided()));
        var cleared = json.readValue("{\"overallResult\":\"PASS\",\"odometerReading\":null}", InspectionReportCreateRequest.class);
        assertNull(cleared.getOdometerReading());
        assertTrue(Boolean.TRUE.equals(cleared.getOdometerReadingProvided()));
        var entered = json.readValue("{\"overallResult\":\"PASS\",\"odometerReading\":22000}", InspectionReportCreateRequest.class);
        assertEquals(22000, entered.getOdometerReading());
    }

    @Test void agencyCanSaveEditAndClearMileageButCannotSaveNegativeMileage() {
        var reports = mock(InspectionReportRepository.class);
        var appointments = mock(InspectionAppointmentRepository.class);
        var service = new InspectionReportServiceImpl(reports, mock(VehicleListingService.class), appointments);
        var vehicle = Vehicle.builder().id(9L).vehicleName("Honda Civic").build();
        var appointment = InspectionAppointment.builder().id(3L).vehicle(vehicle).seller(Users.builder().fullName("Seller").build()).build();
        var report = InspectionReport.builder().vehicle(vehicle).inspectionAppointment(appointment).odometerReading(1000).build();
        when(reports.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(appointments.findById(3L)).thenReturn(Optional.of(appointment));
        var request = InspectionReportCreateRequest.builder().vehicleId(9L).inspectionAppointmentId(3L).overallResult(ReportResult.PASS).build();
        service.saveInspectionReport(request, report);
        assertEquals(1000, report.getOdometerReading()); // Older clients may omit mileage.
        for (Integer reading : new Integer[]{22000, 0, null}) {
            request.setOdometerReading(reading);
            service.saveInspectionReport(request, report);
            assertEquals(reading, report.getOdometerReading());
        }
        request.setOdometerReading(-1);
        clearInvocations(reports, appointments);
        assertEquals(400, assertThrows(ResponseStatusException.class, () -> service.saveInspectionReport(request, report)).getStatusCode().value());
        verifyNoInteractions(reports, appointments);
        assertNull(report.getOdometerReading());
    }

    @Test void reportListUsesAuthenticatedAgencyAndRejectsAnotherAgencyId() {
        var reports = mock(InspectionReportRepository.class);
        var agencies = mock(InspectionAgencyService.class);
        var controller = new InspectionReportController(mock(InspectionReportService.class), agencies, reports);
        when(agencies.getCurrentLoggedInAgency()).thenReturn(InspectionAgency.builder().id(42L).build());
        var report = InspectionReport.builder().id(1L).vehicle(Vehicle.builder().id(9L).build())
            .inspectionAppointment(InspectionAppointment.builder().id(3L).build()).build();
        when(reports.findByInspectionAgencyId(42L)).thenReturn(java.util.List.of(report));
        var result = controller.getCurrentAgencyReports();
        assertEquals(1, result.size());
        assertEquals(9L, result.getFirst().get("vehicleId"));
        verify(reports).findByInspectionAgencyId(42L);
        assertEquals(403, assertThrows(ResponseStatusException.class, () -> controller.getReports(7L)).getStatusCode().value());
        verify(reports, never()).findByInspectionAgencyId(7L);
    }

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

    @Test void agencyApprovalRequiresSellerPublicationButSaveAloneKeepsListingInspected() {
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
        request.setEngineCondition(ConditionStatus.GOOD); request.setTransmissionCondition(ConditionStatus.GOOD);
        request.setBrakeCondition(ConditionStatus.GOOD); request.setTireCondition(ConditionStatus.GOOD);
        request.setBodyCondition(ConditionStatus.GOOD); request.setFrameCondition(ConditionStatus.GOOD);
        service.saveInspectionReport(request, report);
        verify(listings).updateVehicleListingStatus(9L, ListingStatus.READY_TO_PUBLISH);
        verify(listings, never()).updateVehicleListingStatus(9L, ListingStatus.PUBLISHED);
        assertEquals(100, report.getScore());
        assertEquals(100, report.getFinalizedScore());
        report.setMajorIssues("Preserved finding");
        report.setWarningLightsPresent(true);
        request.setApprove(false);
        request.setBrakeCondition(ConditionStatus.POOR);
        var response = service.saveInspectionReport(request, report);
        assertEquals(80, report.getScore());
        assertNull(response.getInspectionScore());
        assertFalse(report.isCertified());
        assertEquals("Preserved finding", report.getMajorIssues());
        assertTrue(report.getWarningLightsPresent());
        assertEquals(false, response.getCondition().get("certified"));
        verify(listings, times(2)).updateVehicleListingStatus(9L, ListingStatus.INSPECTED);
    }

    @Test void incompleteApprovalDoesNotModifyExistingReport() {
        var reports = mock(InspectionReportRepository.class);
        var service = new InspectionReportServiceImpl(reports, mock(VehicleListingService.class), mock(InspectionAppointmentRepository.class));
        var report = InspectionReport.builder().summary("Existing report").score(85).build();
        var request = InspectionReportCreateRequest.builder().approve(true).overallResult(ReportResult.PASS).build();
        assertEquals(400, assertThrows(ResponseStatusException.class, () -> service.saveInspectionReport(request, report)).getStatusCode().value());
        assertEquals("Existing report", report.getSummary()); assertEquals(85, report.getScore());
        verifyNoInteractions(reports);
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
