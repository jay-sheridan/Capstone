# Create Inspection Report API

This document explains how to send the POST API request to create (finalize) an inspection report.

## Endpoint
- Method: `POST`
- URL: `/api/v1/inspection/reports`
- Full local URL example: `http://localhost:8080/api/v1/inspection/reports`

## Authorization
- Required: `Bearer` JWT token
- Required role/authority: `INSPECTION_AGENCY`
- Header:

```
Authorization: Bearer <your-jwt-token>
Content-Type: application/json
```

## Important flow note
A blank report is created automatically when an inspection appointment is created.
This API updates that existing report and marks appointment/listing statuses.

Because of that, you must send a valid existing `reportId`.

## Required request body fields
These are mandatory:
- `reportId` (number)
- `vehicleId` (number)
- `inspectionAppointmentId` (number)

## Optional request body fields
You can send any additional report fields below.

### Summary
- `overallResult` (enum: `PENDING`, `ON_HOLD`, `PASS`, `FAIL`, `CONDITIONAL_PASS`)
- `summary` (string)

### Verification
- `odometerVerified` (boolean)
- `vinVerified` (boolean)
- `odometerReading` (number, min 0)

### Condition enums
For all condition fields, use one of:
- `EXCELLENT`, `GOOD`, `FAIR`, `POOR`, `NEEDS_REPAIR`, `NOT_CHECKED`

Condition fields:
- `engineCondition`
- `transmissionCondition`
- `brakeCondition`
- `suspensionCondition`
- `steeringCondition`
- `tireCondition`
- `bodyCondition`
- `frameCondition`
- `interiorCondition`
- `electricalCondition`

### Boolean and numeric detail fields
- `engineOilLeak`, `coolantLeak`, `engineMountConditionGood`, `abnormalEngineNoise`
- `transmissionLeak`, `clutchConditionGood`, `gearShiftSmooth`
- `frontBrakePadLifePercent` (0-100), `rearBrakePadLifePercent` (0-100)
- `brakeDiscConditionGood`, `absWorking`
- `shockAbsorbersGood`, `steeringAlignmentGood`, `unusualSuspensionNoise`
- `frontLeftTreadDepth` (min 0), `frontRightTreadDepth` (min 0)
- `rearLeftTreadDepth` (min 0), `rearRightTreadDepth` (min 0)
- `allTiresMatchingBrand`, `spareTireAvailable`, `alloyWheelDamage`
- `repaintDetected`, `rustPresent`, `dentsPresent`, `scratchesPresent`, `floodDamageSigns`
- `seatConditionGood`, `dashboardConditionGood`, `airConditioningWorking`, `heaterWorking`
- `infotainmentWorking`, `powerWindowsWorking`, `centralLockWorking`
- `headlightsWorking`, `brakeLightsWorking`, `indicatorsWorking`, `batteryConditionGood`
- `warningLightsPresent`, `warningLightDetails` (string)
- `engineOilLevelGood`, `coolantLevelGood`, `brakeFluidLevelGood`, `transmissionFluidLevelGood`
- `roadTestPerformed`, `engineStartsProperly`, `accelerationGood`, `brakingPerformanceGood`
- `highwayStable`, `noVibrationAtHighSpeed`
- `safetyCheckPassed`, `emissionCheckPassed`
- `recommendedRepairs` (string), `majorIssues` (string), `agencyRecommendation` (string)
- `estimatedRepairCost` (number, min 0)

## Minimal request example

```json
{
  "reportId": 15,
  "vehicleId": 101,
  "inspectionAppointmentId": 27
}
```

## Full request example

```json
{
  "reportId": 15,
  "vehicleId": 101,
  "inspectionAppointmentId": 27,
  "overallResult": "PASS",
  "summary": "Vehicle condition is good with minor cosmetic wear.",
  "odometerVerified": true,
  "vinVerified": true,
  "odometerReading": 84500,
  "engineCondition": "GOOD",
  "engineOilLeak": false,
  "coolantLeak": false,
  "engineMountConditionGood": true,
  "abnormalEngineNoise": false,
  "transmissionCondition": "GOOD",
  "transmissionLeak": false,
  "clutchConditionGood": true,
  "gearShiftSmooth": true,
  "brakeCondition": "GOOD",
  "frontBrakePadLifePercent": 70,
  "rearBrakePadLifePercent": 65,
  "brakeDiscConditionGood": true,
  "absWorking": true,
  "suspensionCondition": "GOOD",
  "steeringCondition": "GOOD",
  "shockAbsorbersGood": true,
  "steeringAlignmentGood": true,
  "unusualSuspensionNoise": false,
  "tireCondition": "FAIR",
  "frontLeftTreadDepth": 6,
  "frontRightTreadDepth": 6,
  "rearLeftTreadDepth": 5,
  "rearRightTreadDepth": 5,
  "allTiresMatchingBrand": true,
  "spareTireAvailable": true,
  "alloyWheelDamage": false,
  "bodyCondition": "FAIR",
  "frameCondition": "GOOD",
  "repaintDetected": true,
  "rustPresent": false,
  "dentsPresent": false,
  "scratchesPresent": true,
  "floodDamageSigns": false,
  "interiorCondition": "GOOD",
  "seatConditionGood": true,
  "dashboardConditionGood": true,
  "airConditioningWorking": true,
  "heaterWorking": true,
  "infotainmentWorking": true,
  "powerWindowsWorking": true,
  "centralLockWorking": true,
  "electricalCondition": "GOOD",
  "headlightsWorking": true,
  "brakeLightsWorking": true,
  "indicatorsWorking": true,
  "batteryConditionGood": true,
  "warningLightsPresent": false,
  "warningLightDetails": "",
  "engineOilLevelGood": true,
  "coolantLevelGood": true,
  "brakeFluidLevelGood": true,
  "transmissionFluidLevelGood": true,
  "roadTestPerformed": true,
  "engineStartsProperly": true,
  "accelerationGood": true,
  "brakingPerformanceGood": true,
  "highwayStable": true,
  "noVibrationAtHighSpeed": true,
  "safetyCheckPassed": true,
  "emissionCheckPassed": true,
  "recommendedRepairs": "Replace rear tires within next 5,000 km.",
  "majorIssues": "No major issues found.",
  "agencyRecommendation": "Recommended for purchase after tire replacement.",
  "estimatedRepairCost": 450
}
```

## cURL example

```bash
curl -X POST "http://localhost:8080/api/v1/inspection/reports" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "reportId": 15,
    "vehicleId": 101,
    "inspectionAppointmentId": 27,
    "overallResult": "PASS",
    "summary": "Vehicle condition is good with minor cosmetic wear."
  }'
```

## Success response
- Status: `200 OK`
- Body fields:
  - `appointmentStatus`
  - `vehicleName`
  - `sellerName`
  - `reportNumber`
  - `inspectionScore`

Example:

```json
{
  "appointmentStatus": "COMPLETED",
  "vehicleName": "Honda Civic",
  "sellerName": "Jane Doe",
  "reportNumber": "RPT-A1B2C3D4",
  "inspectionScore": 89
}
```

## Common error cases
- `401 Unauthorized`: Missing or invalid token.
- `403 Forbidden`: Token does not have `INSPECTION_AGENCY` authority.
- `500 RuntimeException`: Invalid/non-existing `reportId` or agency does not own that report.
