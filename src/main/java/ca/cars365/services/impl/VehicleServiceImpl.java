package ca.cars365.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.cars365.dto.response.AppointmentSummaryResponse;
import ca.cars365.dto.response.ReportSummaryResponse;
import ca.cars365.dto.response.SellerVehicleResponse;
import ca.cars365.dto.response.VehiclePhotoResponse;
import ca.cars365.entity.Vehicle;
import ca.cars365.entity.VehiclePhoto;
import ca.cars365.entity.VinDecodeRecord;
import ca.cars365.repositories.VehiclePhotoRepository;
import ca.cars365.repositories.VehicleRepository;
import ca.cars365.repositories.VinDecodeRecordRepository;
import ca.cars365.services.UserService;
import ca.cars365.services.VehicleService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private VinDecodeRecordRepository vinDecodeRecordRepository;

	@Autowired
	private VehiclePhotoRepository vehiclePhotoRepository;

	@Override
	public Vehicle saveVehicle(Vehicle vehicle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vehicle getVehicleById(Long id) {
		return vehicleRepository.findById(id).orElse(null);
	}

	@Override
	public Vehicle getVehicleByVin(String vin) {

		return vehicleRepository.findByVinDecodeRecordVin(vin);
	}

	@Override
	public List<Vehicle> getVehiclesBySeller(Long sellerId) {
		return null;
	}

	@Override
	public List<Vehicle> getAllVehicles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteVehicle(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Vehicle createVehicleFromVin(String vin) {
		VinDecodeRecord vinRecord = vinDecodeRecordRepository.findByVin(vin);
		Vehicle vehicle = Vehicle.builder().vehicleName( vinRecord.getModelYear() + " " + vinRecord.getMake() + " " + vinRecord.getModel())
				.vinDecodeRecord(vinRecord).vinDecoded(true).seller(userService.getCurrentUserEntity()).build();
		
		Vehicle savedVehicle = vehicleRepository.save(vehicle);
		
		
		
		return savedVehicle;
	}

	public List<VehiclePhotoResponse> getVehiclePhotos(Long vehicleId) {

		return mapToVehiclePhotoResponses(vehicleId);

	}

	public List<VehiclePhotoResponse> mapToVehiclePhotoResponses(Long vehicleId) {

		List<VehiclePhoto> photos = vehiclePhotoRepository.findByVehicleIdOrderByDisplayOrderAsc(vehicleId);

		return photos.stream().map(photo -> VehiclePhotoResponse.builder().id(photo.getId()).fileUrl(photo.getFileUrl())
				.primaryPhoto(photo.getPrimaryPhoto()).displayOrder(photo.getDisplayOrder()).build()).toList();
	}

	public String getPrimaryPhotoUrl(Long vehicleId) {
		List<VehiclePhoto> photos = vehiclePhotoRepository.findByVehicleIdOrderByDisplayOrderAsc(vehicleId);

		return photos.stream().filter(VehiclePhoto::getPrimaryPhoto).map(VehiclePhoto::getFileUrl).findFirst()
				.orElse(null);
	}

	public AppointmentSummaryResponse getLatestAppointmentSummary(Long vehicleId) {
		Vehicle vehicle = getVehicleById(vehicleId);
		AppointmentSummaryResponse summary = AppointmentSummaryResponse.builder()
				.appointmentNumber(vehicle.getInspectionAppointment().get(0).getAppointmentNumber())
				.appointmentDateTime(vehicle.getInspectionAppointment().get(0).getAppointmentDateTime())
				.appointmentStatus(vehicle.getInspectionAppointment().get(0).getAppointmentStatus()).build();
		return summary;
	}

	public ReportSummaryResponse getLatestReportSummary(Long vehicleId) {
		Vehicle vehicle = getVehicleById(vehicleId);
		ReportSummaryResponse summary = ReportSummaryResponse.builder()
				.reportNumber(vehicle.getInspectionReports().get(0).getReportNumber())
				.overallResult(vehicle.getInspectionReports().get(0).getOverallResult())
				.inspectionDate(vehicle.getInspectionReports().get(0).getInspectionDate()).build();
		return summary;
	}

	private SellerVehicleResponse mapToVehicleResponse(Vehicle vehicle) {
		return SellerVehicleResponse.builder().id(vehicle.getId()).vehicleName(vehicle.getVehicleName())
				.askingPrice(vehicle.getAskingPrice()).vin(vehicle.getVinDecodeRecord().getVin())
				.make(vehicle.getVinDecodeRecord().getMake()).model(vehicle.getVinDecodeRecord().getModel())
				.modelYear(vehicle.getVinDecodeRecord().getModelYear()).photos(getVehiclePhotos(vehicle.getId()))
				.primaryPhotoUrl(getPrimaryPhotoUrl(vehicle.getId())).inspectionStatus(vehicle.getInspectionStatus())
				.totalAppointments(vehicle.getInspectionAppointment().size())
				.totalReports(vehicle.getInspectionReports().size())
				.hasReport(!vehicle.getInspectionReports().isEmpty())
				.latestAppointment(getLatestAppointmentSummary(vehicle.getId()))
				.latestReport(getLatestReportSummary(vehicle.getId())).createdAt(vehicle.getCreatedAt())
				.updatedAt(vehicle.getUpdatedAt()).build();
	}

	@Override
	public List<SellerVehicleResponse> getSellerVehicles() {

		List<Vehicle> vehicles = vehicleRepository.findBySellerId(userService.getCurrentUserEntity().getId());

		return vehicles.stream().map(this::mapToVehicleResponse).toList();
	}

}
