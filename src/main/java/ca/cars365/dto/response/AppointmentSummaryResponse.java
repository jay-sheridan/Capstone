package ca.cars365.dto.response;

import java.time.LocalDateTime;

import ca.cars365.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentSummaryResponse {

    private String appointmentNumber;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus appointmentStatus;
}