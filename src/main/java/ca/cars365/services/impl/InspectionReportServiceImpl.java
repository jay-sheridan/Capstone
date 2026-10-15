package ca.cars365.services.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import ca.cars365.dto.request.InspectionReportCreateRequest;
import ca.cars365.dto.response.InspectionReportResponse;
import ca.cars365.entity.InspectionAppointment;
import ca.cars365.entity.InspectionReport;
import ca.cars365.enums.AppointmentStatus;
import ca.cars365.enums.ListingStatus;
import ca.cars365.repositories.InspectionAppointmentRepository;
import ca.cars365.repositories.InspectionReportRepository;
import ca.cars365.services.InspectionReportService;
import ca.cars365.services.VehicleListingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class InspectionReportServiceImpl implements InspectionReportService {

	private InspectionReportRepository inspectionReportRepository;
	private VehicleListingService vehicleListingService;
	private InspectionAppointmentRepository inspectionAppointmentRepository;

	@Override
	public InspectionReport getInspectionReportById(Long id) {
		return inspectionReportRepository.findById(id).orElseThrow(() -> new RuntimeException("Inspection Report not found with id: " + id));
	}

	@Override
	public InspectionReport getInspectionReportByRequestId(Long inspectionRequestId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InspectionReport getInspectionReportByVehicleId(Long vehicleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InspectionReport updateInspectionReport(Long id, InspectionReport updatedReport) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteInspectionReport(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public InspectionReport createInspectionReportForAppointment(InspectionAppointment appointment) {

		InspectionReport newReport = InspectionReport.builder()

				.vehicle(appointment.getVehicle())
				.inspectionAgency(appointment.getInspectionAgency())
				.inspectionAppointment(appointment)
				.build();
		System.out.println("Creating at : " + newReport.getCreatedAt());
		 return inspectionReportRepository.save(newReport);
	}

	@Override
	public Integer getInspectionScoreForVehicle(Long vehicleId) {
		return inspectionReportRepository.findByVehicleId(vehicleId).stream().max(java.util.Comparator.comparing(InspectionReport::getId)).map(InspectionReport::getFinalizedScore).orElse(null);
	}

	private InspectionReportResponse mapToInspectionReportResponse (InspectionReportCreateRequest report, InspectionReport existedEmptyReport) {



        if (Boolean.TRUE.equals(report.getApprove()) && (report.getOverallResult() != ca.cars365.enums.ReportResult.PASS
            || !completeRequest(report))) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,
                "Approval requires PASS and all six component ratings (Good, Fair or Poor).");
        }
        existedEmptyReport.setApproved(Boolean.TRUE.equals(report.getApprove()));
        existedEmptyReport.setScoreVersion(1);
        existedEmptyReport.setEmissionsApplicable(!Boolean.FALSE.equals(report.getEmissionsApplicable()));
		existedEmptyReport.setOverallResult(report.getOverallResult());
		existedEmptyReport.setSummary(report.getSummary());
		existedEmptyReport.setOdometerVerified(report.getOdometerVerified());
		existedEmptyReport.setVinVerified(report.getVinVerified());
		if (report.getOdometerReading() != null || Boolean.TRUE.equals(report.getOdometerReadingProvided())) existedEmptyReport.setOdometerReading(report.getOdometerReading());
		existedEmptyReport.setEngineCondition(report.getEngineCondition());
		if (report.getEngineOilLeak() != null) existedEmptyReport.setEngineOilLeak(report.getEngineOilLeak());
		if (report.getCoolantLeak() != null) existedEmptyReport.setCoolantLeak(report.getCoolantLeak());
		if (report.getEngineMountConditionGood() != null) existedEmptyReport.setEngineMountConditionGood(report.getEngineMountConditionGood());
		if (report.getAbnormalEngineNoise() != null) existedEmptyReport.setAbnormalEngineNoise(report.getAbnormalEngineNoise());
		existedEmptyReport.setTransmissionCondition(report.getTransmissionCondition());
		if (report.getTransmissionLeak() != null) existedEmptyReport.setTransmissionLeak(report.getTransmissionLeak());
		if (report.getClutchConditionGood() != null) existedEmptyReport.setClutchConditionGood(report.getClutchConditionGood());
		if (report.getGearShiftSmooth() != null) existedEmptyReport.setGearShiftSmooth(report.getGearShiftSmooth());
		existedEmptyReport.setBrakeCondition(report.getBrakeCondition());
		if (report.getFrontBrakePadLifePercent() != null) existedEmptyReport.setFrontBrakePadLifePercent(report.getFrontBrakePadLifePercent());
		if (report.getRearBrakePadLifePercent() != null) existedEmptyReport.setRearBrakePadLifePercent(report.getRearBrakePadLifePercent());
		if (report.getBrakeDiscConditionGood() != null) existedEmptyReport.setBrakeDiscConditionGood(report.getBrakeDiscConditionGood());
		if (report.getAbsWorking() != null) existedEmptyReport.setAbsWorking(report.getAbsWorking());
		if (report.getSuspensionCondition() != null) existedEmptyReport.setSuspensionCondition(report.getSuspensionCondition());
		if (report.getSteeringCondition() != null) existedEmptyReport.setSteeringCondition(report.getSteeringCondition());
		if (report.getShockAbsorbersGood() != null) existedEmptyReport.setShockAbsorbersGood(report.getShockAbsorbersGood());
		if (report.getSteeringAlignmentGood() != null) existedEmptyReport.setSteeringAlignmentGood(report.getSteeringAlignmentGood());
		if (report.getUnusualSuspensionNoise() != null) existedEmptyReport.setUnusualSuspensionNoise(report.getUnusualSuspensionNoise());
		existedEmptyReport.setTireCondition(report.getTireCondition());
		if (report.getFrontLeftTreadDepth() != null) existedEmptyReport.setFrontLeftTreadDepth(report.getFrontLeftTreadDepth());
		if (report.getFrontRightTreadDepth() != null) existedEmptyReport.setFrontRightTreadDepth(report.getFrontRightTreadDepth());
		if (report.getRearLeftTreadDepth() != null) existedEmptyReport.setRearLeftTreadDepth(report.getRearLeftTreadDepth());
		if (report.getRearRightTreadDepth() != null) existedEmptyReport.setRearRightTreadDepth(report.getRearRightTreadDepth());
		if (report.getAllTiresMatchingBrand() != null) existedEmptyReport.setAllTiresMatchingBrand(report.getAllTiresMatchingBrand());
		if (report.getSpareTireAvailable() != null) existedEmptyReport.setSpareTireAvailable(report.getSpareTireAvailable());
		if (report.getAlloyWheelDamage() != null) existedEmptyReport.setAlloyWheelDamage(report.getAlloyWheelDamage());
		existedEmptyReport.setBodyCondition(report.getBodyCondition());
		existedEmptyReport.setFrameCondition(report.getFrameCondition());
		if (report.getRepaintDetected() != null) existedEmptyReport.setRepaintDetected(report.getRepaintDetected());
		if (report.getRustPresent() != null) existedEmptyReport.setRustPresent(report.getRustPresent());
		if (report.getDentsPresent() != null) existedEmptyReport.setDentsPresent(report.getDentsPresent());
		if (report.getScratchesPresent() != null) existedEmptyReport.setScratchesPresent(report.getScratchesPresent());
		if (report.getFloodDamageSigns() != null) existedEmptyReport.setFloodDamageSigns(report.getFloodDamageSigns());
		if (report.getInteriorCondition() != null) existedEmptyReport.setInteriorCondition(report.getInteriorCondition());
		if (report.getSeatConditionGood() != null) existedEmptyReport.setSeatConditionGood(report.getSeatConditionGood());
		if (report.getDashboardConditionGood() != null) existedEmptyReport.setDashboardConditionGood(report.getDashboardConditionGood());
		if (report.getAirConditioningWorking() != null) existedEmptyReport.setAirConditioningWorking(report.getAirConditioningWorking());
		if (report.getHeaterWorking() != null) existedEmptyReport.setHeaterWorking(report.getHeaterWorking());
		if (report.getInfotainmentWorking() != null) existedEmptyReport.setInfotainmentWorking(report.getInfotainmentWorking());
		if (report.getPowerWindowsWorking() != null) existedEmptyReport.setPowerWindowsWorking(report.getPowerWindowsWorking());
		if (report.getCentralLockWorking() != null) existedEmptyReport.setCentralLockWorking(report.getCentralLockWorking());
		if (report.getElectricalCondition() != null) existedEmptyReport.setElectricalCondition(report.getElectricalCondition());
		if (report.getHeadlightsWorking() != null) existedEmptyReport.setHeadlightsWorking(report.getHeadlightsWorking());
		if (report.getBrakeLightsWorking() != null) existedEmptyReport.setBrakeLightsWorking(report.getBrakeLightsWorking());
		if (report.getIndicatorsWorking() != null) existedEmptyReport.setIndicatorsWorking(report.getIndicatorsWorking());
		if (report.getBatteryConditionGood() != null) existedEmptyReport.setBatteryConditionGood(report.getBatteryConditionGood());
		if (report.getWarningLightsPresent() != null) existedEmptyReport.setWarningLightsPresent(report.getWarningLightsPresent());
		if (report.getWarningLightDetails() != null) existedEmptyReport.setWarningLightDetails(report.getWarningLightDetails());
		if (report.getEngineOilLevelGood() != null) existedEmptyReport.setEngineOilLevelGood(report.getEngineOilLevelGood());
		if (report.getCoolantLevelGood() != null) existedEmptyReport.setCoolantLevelGood(report.getCoolantLevelGood());
		if (report.getBrakeFluidLevelGood() != null) existedEmptyReport.setBrakeFluidLevelGood(report.getBrakeFluidLevelGood());
		if (report.getTransmissionFluidLevelGood() != null) existedEmptyReport.setTransmissionFluidLevelGood(report.getTransmissionFluidLevelGood());
		if (report.getRoadTestPerformed() != null) existedEmptyReport.setRoadTestPerformed(report.getRoadTestPerformed());
		if (report.getEngineStartsProperly() != null) existedEmptyReport.setEngineStartsProperly(report.getEngineStartsProperly());
		if (report.getAccelerationGood() != null) existedEmptyReport.setAccelerationGood(report.getAccelerationGood());
		if (report.getBrakingPerformanceGood() != null) existedEmptyReport.setBrakingPerformanceGood(report.getBrakingPerformanceGood());
		if (report.getHighwayStable() != null) existedEmptyReport.setHighwayStable(report.getHighwayStable());
		if (report.getNoVibrationAtHighSpeed() != null) existedEmptyReport.setNoVibrationAtHighSpeed(report.getNoVibrationAtHighSpeed());
		existedEmptyReport.setSafetyCheckPassed(report.getSafetyCheckPassed());
		existedEmptyReport.setEmissionCheckPassed(report.getEmissionCheckPassed());
		existedEmptyReport.setRecommendedRepairs(report.getRecommendedRepairs());
		if (report.getMajorIssues() != null) existedEmptyReport.setMajorIssues(report.getMajorIssues());
		if (report.getAgencyRecommendation() != null) existedEmptyReport.setAgencyRecommendation(report.getAgencyRecommendation());
		if (report.getEstimatedRepairCost() != null) existedEmptyReport.setEstimatedRepairCost(report.getEstimatedRepairCost());
		existedEmptyReport.calculateScoreInternal();
		existedEmptyReport.setInspectionDate(report.getInspectionDate() == null ? LocalDateTime.now() : report.getInspectionDate().atStartOfDay());
		existedEmptyReport.setUpdatedAt(LocalDateTime.now());

		InspectionReport savedReport =  inspectionReportRepository.save(existedEmptyReport);

		InspectionAppointment appointment = inspectionAppointmentRepository.findById(report.getInspectionAppointmentId()).orElseThrow(() -> new RuntimeException("Inspection Appointment not found with id: " + report.getInspectionAppointmentId()));

		appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);

		inspectionAppointmentRepository.save(appointment);

		 vehicleListingService.updateVehicleListingStatus(report.getVehicleId(), Boolean.TRUE.equals(report.getApprove()) && report.getOverallResult() == ca.cars365.enums.ReportResult.PASS ? ListingStatus.READY_TO_PUBLISH : ListingStatus.INSPECTED);

		 InspectionReportResponse response = InspectionReportResponse.builder()
				 .appointmentStatus(savedReport.getInspectionAppointment().getAppointmentStatus())
				 .vehicleName(savedReport.getVehicle().getVehicleName())
				 .sellerName(savedReport.getInspectionAppointment().getSeller().getFullName())
				 .reportNumber(savedReport.getReportNumber())
				 .inspectionScore(savedReport.getFinalizedScore())
                 .condition(savedReport.conditionSummary())
				 .build();
		return response;
	}

    private boolean completeRequest(InspectionReportCreateRequest r) {
        return java.util.stream.Stream.of(r.getEngineCondition(), r.getTransmissionCondition(), r.getBrakeCondition(),
            r.getTireCondition(), r.getBodyCondition(), r.getFrameCondition()).allMatch(v ->
                v == ca.cars365.enums.ConditionStatus.GOOD || v == ca.cars365.enums.ConditionStatus.FAIR || v == ca.cars365.enums.ConditionStatus.POOR);
    }

	@Override
	public InspectionReportResponse saveInspectionReport(InspectionReportCreateRequest report, InspectionReport existedEmptyReport) {
        if (report.getOdometerReading() != null && report.getOdometerReading() < 0) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Mileage cannot be negative.");
        }
		return mapToInspectionReportResponse(report, existedEmptyReport);
	}
}
