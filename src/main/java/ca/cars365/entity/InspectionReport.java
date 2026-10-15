package ca.cars365.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import ca.cars365.enums.ConditionStatus;
import ca.cars365.enums.ReportResult;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String agencyRecommendation;

    private Integer estimatedRepairCost;

    private LocalDateTime inspectionDate;
    
    private Integer score;
    private String rating;
    // Nullable additions keep legacy rows readable without claiming their old score uses this formula.
    private Integer scoreVersion;
    private Boolean approved;
    @Builder.Default
    private Boolean emissionsApplicable = true;

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
        if (!hasCompleteComponents()) { score = null; rating = null; return; }
        score = points(engineCondition, 20, 10) + points(transmissionCondition, 15, 8)
            + points(brakeCondition, 20, 10) + points(tireCondition, 10, 5)
            + points(bodyCondition, 10, 5) + points(frameCondition, 25, 12);
        rating = ratingFor(score);
    }

    public static String ratingFor(int score) {
        if (score < 0 || score > 100) throw new IllegalArgumentException("Score must be between 0 and 100");
        return score >= 90 ? "Excellent" : score >= 75 ? "Very Good" : score >= 60 ? "Good" : score >= 40 ? "Fair" : "Poor";
    }

    private static boolean rated(ConditionStatus value) {
        return value == ConditionStatus.GOOD || value == ConditionStatus.FAIR || value == ConditionStatus.POOR;
    }

    private static int points(ConditionStatus value, int good, int fair) {
        return value == ConditionStatus.GOOD ? good : value == ConditionStatus.FAIR ? fair : 0;
    }

    public boolean hasCompleteComponents() {
        return rated(engineCondition) && rated(transmissionCondition) && rated(brakeCondition)
            && rated(tireCondition) && rated(bodyCondition) && rated(frameCondition);
    }

    public boolean isFinalized() {
        return Integer.valueOf(1).equals(scoreVersion) && inspectionDate != null
            && overallResult != null && overallResult != ReportResult.PENDING && hasCompleteComponents();
    }

    public Integer getFinalizedScore() {
        return isFinalized() && Boolean.TRUE.equals(approved) ? score : null;
    }

    public boolean isCertified() {
        return isFinalized() && Boolean.TRUE.equals(approved) && overallResult == ReportResult.PASS
            && Boolean.TRUE.equals(vinVerified) && Boolean.TRUE.equals(odometerVerified)
            && Boolean.TRUE.equals(safetyCheckPassed)
            && (Boolean.FALSE.equals(emissionsApplicable) || Boolean.TRUE.equals(emissionCheckPassed))
            && brakeCondition != ConditionStatus.POOR && frameCondition != ConditionStatus.POOR;
    }

    public String getVerificationStatus() {
        if (overallResult == ReportResult.FAIL) return "Inspection Failed";
        if (overallResult == ReportResult.CONDITIONAL_PASS || brakeCondition == ConditionStatus.POOR || frameCondition == ConditionStatus.POOR)
            return "Repairs Required";
        if (!isFinalized() || !Boolean.TRUE.equals(approved)) return "Inspection Pending";
        return isCertified() ? "Car365 Certified" : "Verification Incomplete";
    }

    public java.util.Map<String, Object> conditionSummary() {
        java.util.Map<String, Object> result = new java.util.LinkedHashMap<>();
        Integer visibleScore = getFinalizedScore();
        result.put("score", visibleScore);
        result.put("rating", visibleScore == null ? null : rating);
        result.put("certified", isCertified());
        result.put("status", getVerificationStatus());
        result.put("overallResult", overallResult);
        result.put("components", java.util.List.of(
            component("Engine", engineCondition, 20, 10), component("Transmission", transmissionCondition, 15, 8),
            component("Brakes", brakeCondition, 20, 10), component("Tires", tireCondition, 10, 5),
            component("Body", bodyCondition, 10, 5), component("Frame", frameCondition, 25, 12)));
        return result;
    }

    private java.util.Map<String, Object> component(String name, ConditionStatus value, int good, int fair) {
        java.util.Map<String, Object> item = new java.util.LinkedHashMap<>();
        item.put("name", name); item.put("condition", value); item.put("maximum", good);
        item.put("points", getFinalizedScore() == null || !rated(value) ? null : points(value, good, fair));
        return item;
    }
}
