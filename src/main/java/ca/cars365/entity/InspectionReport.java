package ca.cars365.entity;

import java.time.LocalDate;
import java.util.UUID;

import ca.cars365.enums.ConditionStatus;
import ca.cars365.enums.ReportResult;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "inspection_reports")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InspectionReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_report_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String reportNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private ReportResult overallResult = ReportResult.PENDING;

    @Column(columnDefinition = "TEXT")
    private String summary;

    // Verification
    @Builder.Default
    private Boolean odometerVerified = false;

    @Builder.Default
    private Boolean vinVerified = false;

    private Integer odometerReading;


    // Engine
    @Enumerated(EnumType.STRING)
    private ConditionStatus engineCondition;

    @Builder.Default
    private Boolean engineOilLeak = false;

    @Builder.Default
    private Boolean coolantLeak = false;

    @Builder.Default
    private Boolean engineMountConditionGood = false;

    @Builder.Default
    private Boolean abnormalEngineNoise = false;

    // Transmission
    @Enumerated(EnumType.STRING)
    private ConditionStatus transmissionCondition;

    @Builder.Default
    private Boolean transmissionLeak = false;

    @Builder.Default
    private Boolean clutchConditionGood = false; // useful for manual cars

    @Builder.Default
    private Boolean gearShiftSmooth = false;

    // Brakes
    @Enumerated(EnumType.STRING)
    private ConditionStatus brakeCondition;

    private Integer frontBrakePadLifePercent;
    private Integer rearBrakePadLifePercent;

    @Builder.Default
    private Boolean brakeDiscConditionGood = false;

    @Builder.Default
    private Boolean absWorking = false;

    // Suspension & steering
    @Enumerated(EnumType.STRING)
    private ConditionStatus suspensionCondition;

    @Enumerated(EnumType.STRING)
    private ConditionStatus steeringCondition;

    @Builder.Default
    private Boolean shockAbsorbersGood = false;

    @Builder.Default
    private Boolean steeringAlignmentGood = false;

    @Builder.Default
    private Boolean unusualSuspensionNoise = false;

    // Tires & wheels
    @Enumerated(EnumType.STRING)
    private ConditionStatus tireCondition;

    private Integer frontLeftTreadDepth;
    private Integer frontRightTreadDepth;
    private Integer rearLeftTreadDepth;
    private Integer rearRightTreadDepth;

    @Builder.Default
    private Boolean allTiresMatchingBrand = false;

    @Builder.Default
    private Boolean spareTireAvailable = false;

    @Builder.Default
    private Boolean alloyWheelDamage = false;

    // Exterior / body
    @Enumerated(EnumType.STRING)
    private ConditionStatus bodyCondition;

    @Enumerated(EnumType.STRING)
    private ConditionStatus frameCondition;

    @Builder.Default
    private Boolean repaintDetected = false;

    @Builder.Default
    private Boolean rustPresent = false;

    @Builder.Default
    private Boolean dentsPresent = false;

    @Builder.Default
    private Boolean scratchesPresent = false;

    @Builder.Default
    private Boolean floodDamageSigns = false;

    // Interior
    @Enumerated(EnumType.STRING)
    private ConditionStatus interiorCondition;

    @Builder.Default
    private Boolean seatConditionGood = false;

    @Builder.Default
    private Boolean dashboardConditionGood = false;

    @Builder.Default
    private Boolean airConditioningWorking = false;

    @Builder.Default
    private Boolean heaterWorking = false;

    @Builder.Default
    private Boolean infotainmentWorking = false;

    @Builder.Default
    private Boolean powerWindowsWorking = false;

    @Builder.Default
    private Boolean centralLockWorking = false;

    // Electrical
    @Enumerated(EnumType.STRING)
    private ConditionStatus electricalCondition;

    @Builder.Default
    private Boolean headlightsWorking = false;

    @Builder.Default
    private Boolean brakeLightsWorking = false;

    @Builder.Default
    private Boolean indicatorsWorking = false;

    @Builder.Default
    private Boolean batteryConditionGood = false;

    @Builder.Default
    private Boolean warningLightsPresent = false;

    @Column(columnDefinition = "TEXT")
    private String warningLightDetails;

    // Fluids / leaks
    @Builder.Default
    private Boolean engineOilLevelGood = false;

    @Builder.Default
    private Boolean coolantLevelGood = false;

    @Builder.Default
    private Boolean brakeFluidLevelGood = false;

    @Builder.Default
    private Boolean transmissionFluidLevelGood = false;

    // Road test
    @Builder.Default
    private Boolean roadTestPerformed = false;

    @Builder.Default
    private Boolean engineStartsProperly = false;

    @Builder.Default
    private Boolean accelerationGood = false;

    @Builder.Default
    private Boolean brakingPerformanceGood = false;

    @Builder.Default
    private Boolean highwayStable = false;

    @Builder.Default
    private Boolean noVibrationAtHighSpeed = false;

    // Compliance
    @Builder.Default
    private Boolean safetyCheckPassed = false;

    @Builder.Default
    private Boolean emissionCheckPassed = false;

    // Buyer recommendation
    @Column(columnDefinition = "TEXT")
    private String recommendedRepairs;

    @Column(columnDefinition = "TEXT")
    private String majorIssues;

    @Column(columnDefinition = "TEXT")
    private String buyerRecommendation;

    private Integer estimatedRepairCost;

    private LocalDate inspectionDate;
    
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_agency_id", nullable = false)
    private InspectionAgency inspectionAgency;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_appointment_id", nullable = false, unique = true)
    private InspectionAppointment inspectionAppointment;
    
    @PrePersist
    public void onCreate() {
        generateReportNumberInternal();
        calculateScoreInternal();
        super.createdAt = LocalDate.now().atStartOfDay();
        super.updatedAt = LocalDate.now().atStartOfDay();
    }

    @PreUpdate
    public void onUpdate() {
        calculateScoreInternal();
    }
    

    private void generateReportNumberInternal() {
        if (this.reportNumber == null || this.reportNumber.isBlank()) {
            this.reportNumber = "RPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }
    
    public void calculateScoreInternal() {
        int total = 100;

        // =========================
        // 1. Major system conditions
        // =========================
        total -= getConditionPenalty(engineCondition, 12);
        total -= getConditionPenalty(transmissionCondition, 12);
        total -= getConditionPenalty(brakeCondition, 10);
        total -= getConditionPenalty(suspensionCondition, 8);
        total -= getConditionPenalty(steeringCondition, 8);
        total -= getConditionPenalty(tireCondition, 8);
        total -= getConditionPenalty(bodyCondition, 6);
        total -= getConditionPenalty(frameCondition, 12);
        total -= getConditionPenalty(interiorCondition, 4);
        total -= getConditionPenalty(electricalCondition, 8);

        // =========================
        // 2. Verification
        // =========================
        if (!Boolean.TRUE.equals(odometerVerified)) total -= 5;
        if (!Boolean.TRUE.equals(vinVerified)) total -= 8;

        // =========================
        // 3. Engine
        // =========================
        if (Boolean.TRUE.equals(engineOilLeak)) total -= 8;
        if (Boolean.TRUE.equals(coolantLeak)) total -= 6;
        if (!Boolean.TRUE.equals(engineMountConditionGood)) total -= 3;
        if (Boolean.TRUE.equals(abnormalEngineNoise)) total -= 8;

        // =========================
        // 4. Transmission
        // =========================
        if (Boolean.TRUE.equals(transmissionLeak)) total -= 8;
        if (!Boolean.TRUE.equals(clutchConditionGood)) total -= 3;
        if (!Boolean.TRUE.equals(gearShiftSmooth)) total -= 4;

        // =========================
        // 5. Brakes
        // =========================
        total -= getBrakePadPenalty(frontBrakePadLifePercent);
        total -= getBrakePadPenalty(rearBrakePadLifePercent);

        if (!Boolean.TRUE.equals(brakeDiscConditionGood)) total -= 5;
        if (!Boolean.TRUE.equals(absWorking)) total -= 8;

        // =========================
        // 6. Suspension / Steering
        // =========================
        if (!Boolean.TRUE.equals(shockAbsorbersGood)) total -= 4;
        if (!Boolean.TRUE.equals(steeringAlignmentGood)) total -= 4;
        if (Boolean.TRUE.equals(unusualSuspensionNoise)) total -= 5;

        // =========================
        // 7. Tires / Wheels
        // =========================
        total -= getTreadPenalty(frontLeftTreadDepth);
        total -= getTreadPenalty(frontRightTreadDepth);
        total -= getTreadPenalty(rearLeftTreadDepth);
        total -= getTreadPenalty(rearRightTreadDepth);

        if (!Boolean.TRUE.equals(allTiresMatchingBrand)) total -= 2;
        if (!Boolean.TRUE.equals(spareTireAvailable)) total -= 1;
        if (Boolean.TRUE.equals(alloyWheelDamage)) total -= 2;

        // =========================
        // 8. Exterior / Body
        // =========================
        if (Boolean.TRUE.equals(repaintDetected)) total -= 2;
        if (Boolean.TRUE.equals(rustPresent)) total -= 6;
        if (Boolean.TRUE.equals(dentsPresent)) total -= 2;
        if (Boolean.TRUE.equals(scratchesPresent)) total -= 1;
        if (Boolean.TRUE.equals(floodDamageSigns)) total -= 15;

        // =========================
        // 9. Interior
        // =========================
        if (!Boolean.TRUE.equals(seatConditionGood)) total -= 2;
        if (!Boolean.TRUE.equals(dashboardConditionGood)) total -= 2;
        if (!Boolean.TRUE.equals(airConditioningWorking)) total -= 3;
        if (!Boolean.TRUE.equals(heaterWorking)) total -= 2;
        if (!Boolean.TRUE.equals(infotainmentWorking)) total -= 1;
        if (!Boolean.TRUE.equals(powerWindowsWorking)) total -= 2;
        if (!Boolean.TRUE.equals(centralLockWorking)) total -= 2;

        // =========================
        // 10. Electrical
        // =========================
        if (!Boolean.TRUE.equals(headlightsWorking)) total -= 3;
        if (!Boolean.TRUE.equals(brakeLightsWorking)) total -= 4;
        if (!Boolean.TRUE.equals(indicatorsWorking)) total -= 3;
        if (!Boolean.TRUE.equals(batteryConditionGood)) total -= 4;
        if (Boolean.TRUE.equals(warningLightsPresent)) total -= 10;

        // =========================
        // 11. Fluids
        // =========================
        if (!Boolean.TRUE.equals(engineOilLevelGood)) total -= 3;
        if (!Boolean.TRUE.equals(coolantLevelGood)) total -= 3;
        if (!Boolean.TRUE.equals(brakeFluidLevelGood)) total -= 4;
        if (!Boolean.TRUE.equals(transmissionFluidLevelGood)) total -= 4;

        // =========================
        // 12. Road test
        // =========================
        if (!Boolean.TRUE.equals(roadTestPerformed)) {
            total -= 10; // because full confidence is not possible
        } else {
            if (!Boolean.TRUE.equals(engineStartsProperly)) total -= 6;
            if (!Boolean.TRUE.equals(accelerationGood)) total -= 4;
            if (!Boolean.TRUE.equals(brakingPerformanceGood)) total -= 6;
            if (!Boolean.TRUE.equals(highwayStable)) total -= 5;
            if (!Boolean.TRUE.equals(noVibrationAtHighSpeed)) total -= 5;
        }

        // =========================
        // 13. Compliance
        // =========================
        if (!Boolean.TRUE.equals(safetyCheckPassed)) total -= 15;
        if (!Boolean.TRUE.equals(emissionCheckPassed)) total -= 8;

        // =========================
        // 14. Repair cost impact
        // =========================
        if (estimatedRepairCost != null) {
            if (estimatedRepairCost > 5000) total -= 12;
            else if (estimatedRepairCost > 3000) total -= 8;
            else if (estimatedRepairCost > 1000) total -= 4;
            else if (estimatedRepairCost > 500) total -= 2;
        }

        // =========================
        // 15. Overall report result
        // =========================
        if (overallResult != null) {
            switch (overallResult) {
                case FAIL -> total -= 20;
                case CONDITIONAL_PASS -> total -= 8;
                case PASS -> total += 0;
                case PENDING -> total -= 5;
                default -> { }
            }
        }

        // final safe range
        if (total < 0) total = 0;
        if (total > 100) total = 100;

        this.score = total;
    }

    private int getConditionPenalty(ConditionStatus status, int maxPenalty) {
        if (status == null) return maxPenalty / 2;

        String name = status.name().toUpperCase();

        if (name.contains("EXCELLENT")) return 0;
        if (name.contains("GOOD")) return maxPenalty / 4;
        if (name.contains("FAIR")) return maxPenalty / 2;
        if (name.contains("POOR")) return (maxPenalty * 3) / 4;
        if (name.contains("BAD") || name.contains("FAILED") || name.contains("CRITICAL")) return maxPenalty;

        return maxPenalty / 2;
    }

    private int getBrakePadPenalty(Integer percent) {
        if (percent == null) return 2;
        if (percent >= 70) return 0;
        if (percent >= 50) return 2;
        if (percent >= 30) return 4;
        if (percent >= 15) return 6;
        return 8;
    }

    private int getTreadPenalty(Integer depth) {
        if (depth == null) return 2;
        if (depth >= 7) return 0;
        if (depth >= 5) return 1;
        if (depth >= 3) return 3;
        if (depth >= 2) return 6;
        return 10;
    }
    
    
}