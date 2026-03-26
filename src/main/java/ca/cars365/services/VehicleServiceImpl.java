package ca.cars365.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ca.cars365.controllers.VehicleService;
import ca.cars365.dto.response.VehicleVinResponse;
import ca.cars365.exception.InvalidVinException;


@Service
public class VehicleServiceImpl implements VehicleService {
	
	@Value("${nhtsa.api.base-url}")
	private String baseUrl;
	
	
	@Override
public VehicleVinResponse getVehicleInfoByVin(String vin) {

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
        throw new InvalidVinException(
                "Invalid VIN: " + vin + " | Error: " + errorMessage
        );
    }
    
    System.out.println(vehicleData);

    // Map to DTO
    return VehicleVinResponse.builder()
            .engineModel((String) vehicleData.get("EngineModel"))
            .make((String) vehicleData.get("Make"))
            .model((String) vehicleData.get("Model"))
            .modelYear((String) vehicleData.get("ModelYear"))
            .fuelTypePrimary((String) vehicleData.get("FuelTypePrimary"))
            .vehicleType((String) vehicleData.get("VehicleType"))
            .plantCountry((String) vehicleData.get("PlantCountry"))
            .transmissionStyle((String) vehicleData.get("TransmissionStyle"))
            .build();
	}
	
}
