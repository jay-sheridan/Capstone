package ca.cars365;

import ca.cars365.controllers.*;
import ca.cars365.dto.request.InspectionReportCreateRequest;
import ca.cars365.entity.*;
import ca.cars365.enums.*;
import ca.cars365.services.VehicleListingService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable(named = "NEON_TEST_DATABASE_URL", matches = ".+")
@SpringBootTest(properties = {
    "spring.datasource.url=${NEON_TEST_DATABASE_URL}",
    "spring.datasource.username=${NEON_TEST_DATABASE_USERNAME}",
    "spring.datasource.password=${NEON_TEST_DATABASE_PASSWORD}",
    "JWT_SECRET_KEY=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
})
@Transactional
class PublicationIntegrationTests {
    @Autowired EntityManager em;
    @Autowired InspectionReportController reports;
    @Autowired SellerController sellers;
    @Autowired VehicleListingService listings;
    @AfterEach void clearAuthentication() { SecurityContextHolder.clearContext(); }

    @Test void agencyApprovalPublishesForBuyersAndSeller() {
        var seller = Users.builder().email("publication-test@example.com").fullName("Test Seller").passwordHash("test").build();
        em.persist(seller);
        var agency = InspectionAgency.builder().name("Test Agency").licenseNumber("TEST-PUBLICATION").passwordHash("test").build();
        em.persist(agency);
        var vehicle = Vehicle.builder().vehicleName("Test Vehicle").seller(seller).build();
        em.persist(vehicle);
        var appointment = InspectionAppointment.builder().vehicle(vehicle).seller(seller).inspectionAgency(agency)
                .appointmentDateTime(java.time.LocalDateTime.now().plusDays(1)).build();
        em.persist(appointment);
        var listing = VehicleListing.builder().title("Test Vehicle").vehicle(vehicle).seller(seller)
                .inspectionAgency(agency).status(ListingStatus.HIDDEN).visibleToBuyers(false).build();
        em.persist(listing);
        var report = InspectionReport.builder().vehicle(vehicle).inspectionAgency(agency).inspectionAppointment(appointment).build();
        em.persist(report);
        em.flush();
        Long appointmentId = appointment.getId();
        Long listingId = listing.getId();
        em.clear();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(agency, null, agency.getAuthorities()));
        var request = InspectionReportCreateRequest.builder().inspectionAppointmentId(appointmentId)
                .overallResult(ReportResult.PASS).approve(true).engineCondition(ConditionStatus.GOOD).transmissionCondition(ConditionStatus.GOOD)
                .brakeCondition(ConditionStatus.GOOD).tireCondition(ConditionStatus.GOOD).bodyCondition(ConditionStatus.GOOD).frameCondition(ConditionStatus.GOOD).build();
        reports.createInspectionReport(request);
        em.flush(); em.clear();
        assertFalse(listings.getVisibleListings().stream().anyMatch(item -> item.getId().equals(listingId)));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(seller, null, seller.getAuthorities()));
        assertEquals(false, sellers.status(appointmentId).get("live"));
        assertEquals("READY_TO_PUBLISH", sellers.status(appointmentId).get("listingStatus"));
        sellers.publish(listingId, new SellerController.PublishRequest(15000.0, true));
        em.flush(); em.clear();
        assertTrue(listings.getVisibleListings().stream().anyMatch(item -> item.getId().equals(listingId)));
        assertEquals(true, sellers.status(appointmentId).get("live"));
        assertEquals("PUBLISHED", sellers.status(appointmentId).get("listingStatus"));
    }
}
