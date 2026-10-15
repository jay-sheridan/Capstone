package ca.cars365;

import ca.cars365.controllers.PriceSuggestionController;
import ca.cars365.entity.*;
import ca.cars365.enums.*;
import ca.cars365.repositories.VehicleListingRepository;
import ca.cars365.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.net.http.*;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;

class GeminiPriceTests {
    private InspectionReport report() {
        var r = InspectionReport.builder().id(2L).engineCondition(ConditionStatus.GOOD).transmissionCondition(ConditionStatus.GOOD)
            .brakeCondition(ConditionStatus.GOOD).tireCondition(ConditionStatus.GOOD).bodyCondition(ConditionStatus.GOOD).frameCondition(ConditionStatus.GOOD)
            .overallResult(ReportResult.PASS).scoreVersion(1).approved(true).inspectionDate(LocalDateTime.now()).build();
        r.calculateScoreInternal(); return r;
    }
    private Vehicle vehicle() {
        return Vehicle.builder().id(1L).vinDecodeRecord(VinDecodeRecord.builder().vin("PRIVATE-VIN")
            .make("Honda").model("Civic").modelYear(2020).build()).build();
    }
    @SuppressWarnings("unchecked")
    private HttpResponse<String> response(int status, String text) {
        var response = (HttpResponse<String>) mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(status); when(response.body()).thenReturn(text); return response;
    }
    private String body(String prices) {
        return "{\"candidates\":[{\"finishReason\":\"STOP\",\"content\":{\"parts\":[{\"text\":"
            + new com.fasterxml.jackson.databind.ObjectMapper().getNodeFactory().textNode(prices).toString() + "}]}}]}";
    }
    @Test void generatesValidatedCadPriceCachesItAndInvalidatesOnInspectionChange() throws Exception {
        var client = mock(HttpClient.class);
        var json = "{\"suggestedPrice\":18000,\"minimumPrice\":16000,\"maximumPrice\":20000,\"currency\":\"CAD\",\"explanation\":\"Mileage is unknown; confirm the estimate against comparable listings.\"}";
        var ok = response(200,body(json));
        when(client.send(any(HttpRequest.class), org.mockito.ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenReturn(ok);
        var service = new GeminiPriceService("test-key","gemini-3.8-flash",client);
        var vehicle = vehicle(); var report = report();
        var first = service.suggest(vehicle,report); assertEquals(18000,first.suggestedPrice());
        assertSame(first,service.suggest(vehicle,report));
        verify(client,times(1)).send(any(),org.mockito.ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
        var capture=ArgumentCaptor.forClass(HttpRequest.class);
        verify(client).send(capture.capture(),org.mockito.ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
        assertEquals("test-key",capture.getValue().headers().firstValue("x-goog-api-key").orElseThrow());
        assertFalse(capture.getValue().uri().toString().contains("test-key"));
        report.setBodyCondition(ConditionStatus.FAIR); report.calculateScoreInternal();
        service.suggest(vehicle,report);
        verify(client,times(2)).send(any(),org.mockito.ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
    }
    @Test void missingKeyAndIncompleteInspectionsDoNotCallProvider() {
        var client=mock(HttpClient.class); var report=report();
        var service=new GeminiPriceService("","gemini-3.8-flash",client);
        assertEquals(503,assertThrows(ResponseStatusException.class,()->service.suggest(vehicle(),report)).getStatusCode().value());
        report.setApproved(false);
        assertEquals(409,assertThrows(ResponseStatusException.class,()->service.suggest(vehicle(),report)).getStatusCode().value());
        verifyNoInteractions(client);
    }
    @Test void providerErrorsAndInvalidPricesNeverBecomeSuggestions() throws Exception {
        var client=mock(HttpClient.class); var service=new GeminiPriceService("test-key","gemini-3.8-flash",client);
        var limited = response(429,"private provider error");
        when(client.send(any(),org.mockito.ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenReturn(limited);
        var error=assertThrows(ResponseStatusException.class,()->service.suggest(vehicle(),report()));
        assertEquals(429,error.getStatusCode().value()); assertFalse(error.getReason().contains("private provider"));
        for(String invalid:List.of("{\"suggestedPrice\":-1}","{\"suggestedPrice\":18000,\"minimumPrice\":20000,\"maximumPrice\":16000,\"currency\":\"CAD\",\"explanation\":\"Invalid range\"}","not json")) {
            var bad = response(200,body(invalid));
            when(client.send(any(),org.mockito.ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenReturn(bad);
            assertEquals(502,assertThrows(ResponseStatusException.class,()->service.suggest(vehicle(),report())).getStatusCode().value());
        }
    }
    @Test void onlyOwnerOfApprovedListingCanRequestSuggestion() {
        var listings=mock(VehicleListingRepository.class); var users=mock(UserService.class); var service=mock(GeminiPriceService.class);
        var controller=new PriceSuggestionController(listings,users,service);
        var listing=VehicleListing.builder().seller(Users.builder().id(1L).build()).status(ListingStatus.READY_TO_PUBLISH).vehicle(vehicle()).build();
        when(listings.findById(1L)).thenReturn(Optional.of(listing));
        when(users.getCurrentUserEntity()).thenReturn(Users.builder().id(2L).build());
        assertEquals(403,assertThrows(ResponseStatusException.class,()->controller.suggest(1L)).getStatusCode().value());
        when(users.getCurrentUserEntity()).thenReturn(listing.getSeller()); listing.setStatus(ListingStatus.INSPECTED);
        assertEquals(409,assertThrows(ResponseStatusException.class,()->controller.suggest(1L)).getStatusCode().value());
        verifyNoInteractions(service);
    }
}
