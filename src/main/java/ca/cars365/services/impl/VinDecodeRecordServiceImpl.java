package ca.cars365.services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ca.cars365.dto.response.VehicleVinResponse;
import ca.cars365.entity.VinDecodeRecord;
import ca.cars365.exception.InvalidVinException;
import ca.cars365.repositories.VinDecodeRecordRepository;
import ca.cars365.services.VinDecodeRecordService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VinDecodeRecordServiceImpl implements VinDecodeRecordService {
	
	
	private final VinDecodeRecordRepository vinDecodeRecordRepository = null;

//	@Value("${nhtsa.api.base-url}")
	private String baseUrl =  "https://vpic.nhtsa.dot.gov/api/vehicles/decodevinvalues";
	
	@Override
	public VinDecodeRecord saveVinDecodeRecord(VinDecodeRecord record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VinDecodeRecord getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VinDecodeRecord getByVin(String vin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VinDecodeRecord getByVehicleId(Long vehicleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VinDecodeRecord updateVinDecodeRecord(Long id, VinDecodeRecord updatedRecord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteVinDecodeRecord(Long id) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public VehicleVinResponse decodeVin(String vin) {

		String url = baseUrl + "/" + vin + "?format=json";

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

		Map body = response.getBody();

		List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("Results");

		if (results == null || results.isEmpty()) {
			throw new InvalidVinException("No data found for VIN: " + vin);
		}

		Map<String, Object> vehicleData = results.get(0);

		// 🔥 IMPORTANT: Check ErrorCode
		String errorCode = (String) vehicleData.get("ErrorCode");

		if (errorCode == null || !errorCode.equals("0")) {
			String errorMessage = (String) vehicleData.get("ErrorText");
			throw new InvalidVinException("Invalid VIN: " + vin + " | Error: " + errorMessage);
		}

	

		// Map to DTO
		return VehicleVinResponse.builder().engineModel((String) vehicleData.get("EngineModel"))
				.make((String) vehicleData.get("Make")).model((String) vehicleData.get("Model"))
				.modelYear((String) vehicleData.get("ModelYear"))
				.fuelTypePrimary((String) vehicleData.get("FuelTypePrimary"))
				.vehicleType((String) vehicleData.get("VehicleType"))
				.plantCountry((String) vehicleData.get("PlantCountry"))
				.transmissionStyle((String) vehicleData.get("TransmissionStyle")).build();
	}

}
