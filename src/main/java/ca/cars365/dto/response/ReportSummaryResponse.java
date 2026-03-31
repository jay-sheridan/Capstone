package ca.cars365.dto.response;

import java.time.LocalDate;

import ca.cars365.enums.ReportResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportSummaryResponse {

    private String reportNumber;
    private ReportResult overallResult;
    private LocalDate inspectionDate;
}