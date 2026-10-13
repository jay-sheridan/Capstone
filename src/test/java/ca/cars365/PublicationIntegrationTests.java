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

@SpringBootTest(properties = { "spring.datasource.url=jdbc:h2:mem:publication-tests;DB_CLOSE_DELAY=-1", "JWT_SECRET_KEY=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=" })
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
                .overallResult(ReportResult.PASS).approve(true).engineCondition(ConditionStatus.GOOD).build();
        reports.createInspectionReport(request);
        em.flush(); em.clear();
        assertTrue(listings.getVisibleListings().stream().anyMatch(item -> item.getId().equals(listingId)));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(seller, null, seller.getAuthorities()));
        assertEquals(true, sellers.status(appointmentId).get("live"));
        assertEquals("PUBLISHED", sellers.status(appointmentId).get("listingStatus"));
    }
}
