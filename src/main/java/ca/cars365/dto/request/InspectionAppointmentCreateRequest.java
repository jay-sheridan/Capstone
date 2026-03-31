package ca.cars365.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InspectionAppointmentCreateRequest {
	@NotNull(message = "Inspection agency id is required")
    private Long inspectionAgencyId;

    @NotNull(message = "Vehicle id is required")
    private String vin;

    @NotNull(message = "Appointment date and time is required")
    @Future(message = "Appointment date and time must be in the future")
    private LocalDateTime appointmentDateTime;
}
