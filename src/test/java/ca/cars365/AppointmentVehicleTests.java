package ca.cars365;

import java.time.LocalDateTime;
import ca.cars365.dto.request.InspectionAppointmentCreateRequest;
import ca.cars365.entity.*;
import ca.cars365.repositories.InspectionAppointmentRepository;
import ca.cars365.services.*;
import ca.cars365.services.impl.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AppointmentVehicleTests {
    @Test void bookingCreatesMissingVehicleAndKeepsSelectedDate() {
        var appointments = mock(InspectionAppointmentRepository.class);
        var users = mock(UserService.class);
        var vehicles = mock(VehicleServiceImpl.class);
        var listings = mock(VehicleListingService.class);
        var service = new InspectionAppointmentServiceImpl(appointments, users, vehicles, listings);
        var seller = Users.builder().id(7L).fullName("Seller").build();
        var agency = InspectionAgency.builder().id(2L).name("Agency").build();
        var vehicle = Vehicle.builder().id(9L).vehicleName("2015 Honda Civic").seller(seller).build();
        var date = LocalDateTime.of(2026, 10, 8, 9, 0);
        when(users.getCurrentUserEntity()).thenReturn(seller);
        when(vehicles.createVehicleFromVin("VIN")).thenReturn(vehicle);
        when(appointments.save(any())).thenAnswer(invocation -> {
            InspectionAppointment appointment = invocation.getArgument(0);
            assertSame(vehicle, appointment.getVehicle());
            appointment.setId(3L);
            return appointment;
        });
        var result = service.saveAppointment(InspectionAppointmentCreateRequest.builder()
                .vin("VIN").inspectionAgencyId(2L).appointmentDateTime(date).build(), agency);
        assertEquals(date, result.getAppointmentDateTime());
        assertEquals(9L, result.getVehicleId());
        verify(vehicles).createVehicleFromVin("VIN");
        verify(listings).createVehicleListingForVehicle(any(), eq(9L));
    }
}
