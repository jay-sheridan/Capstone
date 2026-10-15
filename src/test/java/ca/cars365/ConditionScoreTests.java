package ca.cars365;

import ca.cars365.entity.InspectionReport;
import ca.cars365.enums.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ConditionScoreTests {
    private InspectionReport complete(ConditionStatus condition) {
        var r = InspectionReport.builder().engineCondition(condition).transmissionCondition(condition)
            .brakeCondition(condition).tireCondition(condition).bodyCondition(condition).frameCondition(condition)
            .overallResult(ReportResult.PASS).vinVerified(true).odometerVerified(true).safetyCheckPassed(true)
            .emissionCheckPassed(true).approved(true).scoreVersion(1).inspectionDate(LocalDateTime.now()).build();
        r.calculateScoreInternal(); return r;
    }

    @Test void exactWeightsAndAll729CombinationsStayInRange() {
        var values = new ConditionStatus[]{ConditionStatus.GOOD, ConditionStatus.FAIR, ConditionStatus.POOR};
        int[][] weights = {{20,10,0},{15,8,0},{20,10,0},{10,5,0},{10,5,0},{25,12,0}};
        for (int combination = 0; combination < 729; combination++) {
            int code = combination, expected = 0;
            ConditionStatus[] selected = new ConditionStatus[6];
            for (int i=0; i<6; i++) { int choice=code%3; code/=3; selected[i]=values[choice]; expected+=weights[i][choice]; }
            var r=complete(ConditionStatus.GOOD);
            r.setEngineCondition(selected[0]); r.setTransmissionCondition(selected[1]); r.setBrakeCondition(selected[2]);
            r.setTireCondition(selected[3]); r.setBodyCondition(selected[4]); r.setFrameCondition(selected[5]);
            r.calculateScoreInternal();
            assertEquals(expected,r.getScore()); assertTrue(r.getScore()>=0 && r.getScore()<=100);
            assertEquals(InspectionReport.ratingFor(expected), r.getRating());
        }
        assertEquals(100,complete(ConditionStatus.GOOD).getScore());
        assertEquals(50,complete(ConditionStatus.FAIR).getScore());
        assertEquals(0,complete(ConditionStatus.POOR).getScore());
        var example=complete(ConditionStatus.GOOD); example.setBrakeCondition(ConditionStatus.FAIR); example.setBodyCondition(ConditionStatus.FAIR);
        example.calculateScoreInternal(); assertEquals(85,example.getScore());
    }

    @Test void everyRatingBoundary() {
        int[] scores={0,39,40,59,60,74,75,89,90,100};
        String[] ratings={"Poor","Poor","Fair","Fair","Good","Good","Very Good","Very Good","Excellent","Excellent"};
        for(int i=0;i<scores.length;i++) assertEquals(ratings[i],InspectionReport.ratingFor(scores[i]));
        assertThrows(IllegalArgumentException.class,()->InspectionReport.ratingFor(-1));
        assertThrows(IllegalArgumentException.class,()->InspectionReport.ratingFor(101));
    }

    @Test void incompleteUnsupportedAndLegacyReportsAreNotRated() {
        var r=complete(ConditionStatus.GOOD);
        r.setFrameCondition(null); r.onUpdate(); assertNull(r.getScore()); assertNull(r.getRating()); assertFalse(r.isCertified());
        r.setFrameCondition(ConditionStatus.NOT_CHECKED); r.onUpdate(); assertNull(r.getScore());
        r.setFrameCondition(ConditionStatus.EXCELLENT); r.onUpdate(); assertNull(r.getScore());
        r=complete(ConditionStatus.GOOD); r.setScoreVersion(null); assertNull(r.getFinalizedScore()); assertFalse(r.isCertified());
        r.setScoreVersion(1); r.setApproved(false); assertNull(r.getFinalizedScore()); assertFalse(r.isCertified());
        r.setApproved(true); r.setInspectionDate(null); assertNull(r.getFinalizedScore());
    }

    @Test void highScoreNeverOverridesFailureOrMissingVerification() {
        var r=complete(ConditionStatus.GOOD); assertTrue(r.isCertified());
        r.setOverallResult(ReportResult.FAIL); r.calculateScoreInternal();
        assertEquals(100,r.getScore()); assertFalse(r.isCertified()); assertEquals("Inspection Failed",r.getVerificationStatus());
        r.setOverallResult(ReportResult.CONDITIONAL_PASS); assertEquals("Repairs Required",r.getVerificationStatus());
        r.setOverallResult(ReportResult.PASS);
        r.setSafetyCheckPassed(false); assertFalse(r.isCertified()); assertEquals("Verification Incomplete",r.getVerificationStatus());
        r.setSafetyCheckPassed(true); r.setVinVerified(false); assertFalse(r.isCertified());
        r.setVinVerified(true); r.setOdometerVerified(false); assertFalse(r.isCertified());
        r.setOdometerVerified(true); r.setEmissionCheckPassed(false); assertFalse(r.isCertified());
        r.setEmissionsApplicable(false); assertTrue(r.isCertified());
        r.setEmissionsApplicable(null); assertFalse(r.isCertified());
        r.setEmissionCheckPassed(true); r.setBrakeCondition(ConditionStatus.POOR); assertFalse(r.isCertified());
        r.setBrakeCondition(ConditionStatus.GOOD); r.setFrameCondition(ConditionStatus.POOR); assertFalse(r.isCertified());
    }

    @Test void persistenceRecomputesClientSuppliedScoresAndRatings() {
        var r=complete(ConditionStatus.FAIR); r.setScore(100); r.setRating("Excellent"); r.onUpdate();
        assertEquals(50,r.getScore()); assertEquals("Fair",r.getRating());
        assertEquals(r.getFinalizedScore(),r.conditionSummary().get("score"));
        r.setApproved(false); assertNull(r.conditionSummary().get("score")); assertEquals(false,r.conditionSummary().get("certified"));
    }
}
