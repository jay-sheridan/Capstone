package ca.cars365.dto.request;

import java.time.LocalDate;

import ca.cars365.enums.ConditionStatus;
import ca.cars365.enums.ReportResult;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionReportCreateRequest {
    private Boolean approve;

    // =========================
    // Required relation IDs
    // =========================
	
	@NotNull(message = "Report ID is required")
	private Long reportId;
	
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotNull(message = "Inspection appointment ID is required")
    private Long inspectionAppointmentId;

    // =========================
    // Main report summary
    // =========================
    private ReportResult overallResult;
    private String summary;

    // =========================
    // Verification
    // =========================
    private Boolean odometerVerified;
    private Boolean vinVerified;

    @Min(value = 0, message = "Odometer reading cannot be negative")
    private Integer odometerReading;

    // =========================
    // Engine
    // =========================
    private ConditionStatus engineCondition;
    private Boolean engineOilLeak;
    private Boolean coolantLeak;
    private Boolean engineMountConditionGood;
    private Boolean abnormalEngineNoise;

    // =========================
    // Transmission
    // =========================
    private ConditionStatus transmissionCondition;
    private Boolean transmissionLeak;
    private Boolean clutchConditionGood;
    private Boolean gearShiftSmooth;

    // =========================
    // Brakes
    // =========================
    private ConditionStatus brakeCondition;

    @Min(value = 0, message = "Front brake pad life percent cannot be less than 0")
    @Max(value = 100, message = "Front brake pad life percent cannot be more than 100")
    private Integer frontBrakePadLifePercent;

    @Min(value = 0, message = "Rear brake pad life percent cannot be less than 0")
    @Max(value = 100, message = "Rear brake pad life percent cannot be more than 100")
    private Integer rearBrakePadLifePercent;

    private Boolean brakeDiscConditionGood;
    private Boolean absWorking;

    // =========================
    // Suspension & Steering
    // =========================
    private ConditionStatus suspensionCondition;
    private ConditionStatus steeringCondition;
    private Boolean shockAbsorbersGood;
    private Boolean steeringAlignmentGood;
    private Boolean unusualSuspensionNoise;

    // =========================
    // Tires & Wheels
    // =========================
    private ConditionStatus tireCondition;

    @Min(value = 0, message = "Front left tread depth cannot be negative")
    private Integer frontLeftTreadDepth;

    @Min(value = 0, message = "Front right tread depth cannot be negative")
    private Integer frontRightTreadDepth;

    @Min(value = 0, message = "Rear left tread depth cannot be negative")
    private Integer rearLeftTreadDepth;

    @Min(value = 0, message = "Rear right tread depth cannot be negative")
    private Integer rearRightTreadDepth;

    private Boolean allTiresMatchingBrand;
    private Boolean spareTireAvailable;
    private Boolean alloyWheelDamage;

    // =========================
    // Exterior / Body
    // =========================
    private ConditionStatus bodyCondition;
    private ConditionStatus frameCondition;
    private Boolean repaintDetected;
    private Boolean rustPresent;
    private Boolean dentsPresent;
    private Boolean scratchesPresent;
    private Boolean floodDamageSigns;

    // =========================
    // Interior
    // =========================
    private ConditionStatus interiorCondition;
    private Boolean seatConditionGood;
    private Boolean dashboardConditionGood;
    private Boolean airConditioningWorking;
    private Boolean heaterWorking;
    private Boolean infotainmentWorking;
    private Boolean powerWindowsWorking;
    private Boolean centralLockWorking;

    // =========================
    // Electrical
    // =========================
    private ConditionStatus electricalCondition;
    private Boolean headlightsWorking;
    private Boolean brakeLightsWorking;
    private Boolean indicatorsWorking;
    private Boolean batteryConditionGood;
    private Boolean warningLightsPresent;
    private String warningLightDetails;

    // =========================
    // Fluids / Leaks
    // =========================
    private Boolean engineOilLevelGood;
    private Boolean coolantLevelGood;
    private Boolean brakeFluidLevelGood;
    private Boolean transmissionFluidLevelGood;

    // =========================
    // Road test
    // =========================
    private Boolean roadTestPerformed;
    private Boolean engineStartsProperly;
    private Boolean accelerationGood;
    private Boolean brakingPerformanceGood;
    private Boolean highwayStable;
    private Boolean noVibrationAtHighSpeed;

    // =========================
    // Compliance
    // =========================
    private Boolean safetyCheckPassed;
    private Boolean emissionCheckPassed;

    // =========================
    // Buyer recommendation
    // =========================
    private String recommendedRepairs;
    private String majorIssues;
    private String agencyRecommendation;

    @Min(value = 0, message = "Estimated repair cost cannot be negative")
    private Integer estimatedRepairCost;

}
