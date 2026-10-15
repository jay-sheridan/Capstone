package ca.cars365;

import ca.cars365.entity.VinDecodeRecord;
import ca.cars365.repositories.VinDecodeRecordRepository;
import ca.cars365.services.VehicleService;
import ca.cars365.services.impl.VinDecodeRecordServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VinCatalogMetadataTests {
    @Test void reusedLegacyVinIsEnrichedWithoutCreatingAnotherVehicle() {
        var repository=mock(VinDecodeRecordRepository.class); var vehicles=mock(VehicleService.class); var http=mock(RestTemplate.class);
        var service=new VinDecodeRecordServiceImpl();
        ReflectionTestUtils.setField(service,"vinDecodeRecordRepository",repository);
        ReflectionTestUtils.setField(service,"vehicleService",vehicles);
        ReflectionTestUtils.setField(service,"restTemplate",http);
        var existing=VinDecodeRecord.builder().vin("TESTVIN1234567890").make("HONDA").model("Accord").modelYear(2020).build();
        when(repository.findByVin(existing.getVin())).thenReturn(existing);
        when(http.getForEntity(anyString(),eq(Map.class))).thenReturn(ResponseEntity.ok(Map.of("Results",List.of(Map.of("ErrorCode","0","BodyClass","Sedan/Saloon","FuelTypeSecondary","Electric","ElectrificationLevel","HEV")))));
        var result=service.decodeVin(existing.getVin());
        assertEquals("Sedan/Saloon",result.getBodyClass()); assertEquals("HEV",result.getElectrificationLevel());
        verify(repository).save(existing); verifyNoInteractions(vehicles);
    }

    @Test void newlyDecodedVehicleRetainsBodyClassAndHybridMetadataForListingFilters() {
        var repository = mock(VinDecodeRecordRepository.class);
        var vehicles = mock(VehicleService.class);
        var http = mock(RestTemplate.class);
        var service = new VinDecodeRecordServiceImpl();
        ReflectionTestUtils.setField(service,"vinDecodeRecordRepository",repository);
        ReflectionTestUtils.setField(service,"vehicleService",vehicles);
        ReflectionTestUtils.setField(service,"restTemplate",http);
        var result = Map.of("ErrorCode","0","Make","TOYOTA","Model","RAV4","ModelYear","2023",
            "FuelTypePrimary","Gasoline","FuelTypeSecondary","Electric","ElectrificationLevel","PHEV (Plug-in Hybrid Electric Vehicle)",
            "BodyClass","Sport Utility Vehicle (SUV)/Multi-Purpose Vehicle (MPV)");
        when(http.getForEntity(anyString(),eq(Map.class))).thenReturn(ResponseEntity.ok(Map.of("Results",List.of(result))));
        when(repository.save(any())).thenAnswer(call -> call.getArgument(0));
        var decoded=service.decodeVin("TESTVIN1234567890");
        assertEquals(result.get("BodyClass"),decoded.getBodyClass());
        assertEquals("Electric",decoded.getFuelTypeSecondary());
        var captured=org.mockito.ArgumentCaptor.forClass(VinDecodeRecord.class);
        verify(repository).save(captured.capture());
        assertEquals(result.get("BodyClass"),captured.getValue().getBodyClass());
        assertEquals(result.get("ElectrificationLevel"),captured.getValue().getElectrificationLevel());
    }
}
