package ca.cars365;

import ca.cars365.entity.VinDecodeRecord;
import ca.cars365.repositories.VinDecodeRecordRepository;
import ca.cars365.services.VehicleService;
import ca.cars365.services.impl.VinDecodeRecordServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VinDecodeRepeatTests {
    @Test void repeatedLookupReusesRecordWithoutCreatingDuplicateVehicle() {
        var repository = mock(VinDecodeRecordRepository.class);
        var vehicles = mock(VehicleService.class);
        var service = new VinDecodeRecordServiceImpl();
        ReflectionTestUtils.setField(service, "vinDecodeRecordRepository", repository);
        ReflectionTestUtils.setField(service, "vehicleService", vehicles);
        String vin = "2HGFB2F52FH001658";
        when(repository.findByVin(vin)).thenReturn(VinDecodeRecord.builder().vin(vin)
                .make("HONDA").model("Civic").modelYear(2015).bodyClass("Sedan/Saloon").build());
        for (int i = 0; i < 2; i++) {
            var result = service.decodeVin(vin);
            assertEquals("HONDA", result.getMake());
            assertEquals("2015", result.getModelYear());
        }
        verify(repository, never()).save(any());
        verifyNoInteractions(vehicles);
    }
}
