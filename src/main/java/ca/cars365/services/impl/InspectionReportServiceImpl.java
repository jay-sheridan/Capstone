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
		InspectionReport report = inspectionReportRepository.findByVehicleId(vehicleId).getFirst();
		return report.getScore();
	}
	
	private InspectionReportResponse mapToInspectionReportResponse (InspectionReportCreateRequest report, InspectionReport existedEmptyReport) {
		
		
		
		existedEmptyReport.setOverallResult(report.getOverallResult());
		existedEmptyReport.setSummary(report.getSummary());
		existedEmptyReport.setOdometerVerified(report.getOdometerVerified());
		existedEmptyReport.setVinVerified(report.getVinVerified());
		existedEmptyReport.setOdometerReading(report.getOdometerReading());
		existedEmptyReport.setEngineCondition(report.getEngineCondition());
		existedEmptyReport.setEngineOilLeak(report.getEngineOilLeak());
		existedEmptyReport.setCoolantLeak(report.getCoolantLeak());	
		existedEmptyReport.setEngineMountConditionGood(report.getEngineMountConditionGood());
		existedEmptyReport.setAbnormalEngineNoise(report.getAbnormalEngineNoise());
		existedEmptyReport.setTransmissionCondition(report.getTransmissionCondition());
		existedEmptyReport.setTransmissionLeak(report.getTransmissionLeak());
		existedEmptyReport.setClutchConditionGood(report.getClutchConditionGood());
		existedEmptyReport.setGearShiftSmooth(report.getGearShiftSmooth());
		existedEmptyReport.setBrakeCondition(report.getBrakeCondition());
		existedEmptyReport.setFrontBrakePadLifePercent(report.getFrontBrakePadLifePercent());
		existedEmptyReport.setRearBrakePadLifePercent(report.getRearBrakePadLifePercent());
		existedEmptyReport.setBrakeDiscConditionGood(report.getBrakeDiscConditionGood());
		existedEmptyReport.setAbsWorking(report.getAbsWorking());
		existedEmptyReport.setSuspensionCondition(report.getSuspensionCondition());
		existedEmptyReport.setSteeringCondition(report.getSteeringCondition());
		existedEmptyReport.setShockAbsorbersGood(report.getShockAbsorbersGood());
		existedEmptyReport.setSteeringAlignmentGood(report.getSteeringAlignmentGood());
		existedEmptyReport.setUnusualSuspensionNoise(report.getUnusualSuspensionNoise());
		existedEmptyReport.setTireCondition(report.getTireCondition());
		existedEmptyReport.setFrontLeftTreadDepth(report.getFrontLeftTreadDepth());
		existedEmptyReport.setFrontRightTreadDepth(report.getFrontRightTreadDepth());
		existedEmptyReport.setRearLeftTreadDepth(report.getRearLeftTreadDepth());
		existedEmptyReport.setRearRightTreadDepth(report.getRearRightTreadDepth());
		existedEmptyReport.setAllTiresMatchingBrand(report.getAllTiresMatchingBrand());
		existedEmptyReport.setSpareTireAvailable(report.getSpareTireAvailable());
		existedEmptyReport.setAlloyWheelDamage(report.getAlloyWheelDamage());
		existedEmptyReport.setBodyCondition(report.getBodyCondition());
		existedEmptyReport.setFrameCondition(report.getFrameCondition());
		existedEmptyReport.setRepaintDetected(report.getRepaintDetected());
		existedEmptyReport.setRustPresent(report.getRustPresent());
		existedEmptyReport.setDentsPresent(report.getDentsPresent());
		existedEmptyReport.setScratchesPresent(report.getScratchesPresent());
		existedEmptyReport.setFloodDamageSigns(report.getFloodDamageSigns());
		existedEmptyReport.setInteriorCondition(report.getInteriorCondition());
		existedEmptyReport.setSeatConditionGood(report.getSeatConditionGood());
		existedEmptyReport.setDashboardConditionGood(report.getDashboardConditionGood());
		existedEmptyReport.setAirConditioningWorking(report.getAirConditioningWorking());
		existedEmptyReport.setHeaterWorking(report.getHeaterWorking());
		existedEmptyReport.setInfotainmentWorking(report.getInfotainmentWorking());
		existedEmptyReport.setPowerWindowsWorking(report.getPowerWindowsWorking());
		existedEmptyReport.setCentralLockWorking(report.getCentralLockWorking());
		existedEmptyReport.setElectricalCondition(report.getElectricalCondition());
		existedEmptyReport.setHeadlightsWorking(report.getHeadlightsWorking());
		existedEmptyReport.setBrakeLightsWorking(report.getBrakeLightsWorking());
		existedEmptyReport.setIndicatorsWorking(report.getIndicatorsWorking());
		existedEmptyReport.setBatteryConditionGood(report.getBatteryConditionGood());
		existedEmptyReport.setWarningLightsPresent(report.getWarningLightsPresent());
		existedEmptyReport.setWarningLightDetails(report.getWarningLightDetails());
		existedEmptyReport.setEngineOilLevelGood(report.getEngineOilLevelGood());
		existedEmptyReport.setCoolantLevelGood(report.getCoolantLevelGood());
		existedEmptyReport.setBrakeFluidLevelGood(report.getBrakeFluidLevelGood());
		existedEmptyReport.setTransmissionFluidLevelGood(report.getTransmissionFluidLevelGood());
		existedEmptyReport.setRoadTestPerformed(report.getRoadTestPerformed());
		existedEmptyReport.setEngineStartsProperly(report.getEngineStartsProperly());
		existedEmptyReport.setAccelerationGood(report.getAccelerationGood());
		existedEmptyReport.setBrakingPerformanceGood(report.getBrakingPerformanceGood());
		existedEmptyReport.setHighwayStable(report.getHighwayStable());
		existedEmptyReport.setNoVibrationAtHighSpeed(report.getNoVibrationAtHighSpeed());
		existedEmptyReport.setSafetyCheckPassed(report.getSafetyCheckPassed());
		existedEmptyReport.setEmissionCheckPassed(report.getEmissionCheckPassed());
		existedEmptyReport.setRecommendedRepairs(report.getRecommendedRepairs());
		existedEmptyReport.setMajorIssues(report.getMajorIssues());
		existedEmptyReport.setAgencyRecommendation(report.getAgencyRecommendation());
		existedEmptyReport.setEstimatedRepairCost(report.getEstimatedRepairCost());
		existedEmptyReport.calculateScoreInternal();
		existedEmptyReport.setInspectionDate(LocalDateTime.now());
		existedEmptyReport.setUpdatedAt(LocalDateTime.now());
		
		InspectionReport savedReport =  inspectionReportRepository.save(existedEmptyReport);
		
		InspectionAppointment appointment = inspectionAppointmentRepository.findById(report.getInspectionAppointmentId()).orElseThrow(() -> new RuntimeException("Inspection Appointment not found with id: " + report.getInspectionAppointmentId()));

		appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);
		
		inspectionAppointmentRepository.save(appointment);
		
		 vehicleListingService.updateVehicleListingStatus(report.getVehicleId(), ListingStatus.INSPECTED);
		 
		 InspectionReportResponse response = InspectionReportResponse.builder()
				 .appointmentStatus(savedReport.getInspectionAppointment().getAppointmentStatus())
				 .vehicleName(savedReport.getVehicle().getVehicleName())
				 .sellerName(savedReport.getInspectionAppointment().getSeller().getFullName())
				 .reportNumber(savedReport.getReportNumber())
				 .inspectionScore(savedReport.getScore())
				 .build();
		return response;
	}

	@Override
	public InspectionReportResponse saveInspectionReport(InspectionReportCreateRequest report, InspectionReport existedEmptyReport) {
		return mapToInspectionReportResponse(report, existedEmptyReport);
	}
}
