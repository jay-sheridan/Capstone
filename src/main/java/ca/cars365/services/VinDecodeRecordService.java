package ca.cars365.services;

import org.springframework.stereotype.Service;

import ca.cars365.dto.response.VehicleVinResponse;
import ca.cars365.entity.VinDecodeRecord;

@Service
public interface VinDecodeRecordService {


	VehicleVinResponse decodeVin(String vin);
	
	VinDecodeRecord saveVinDecodeRecord(VinDecodeRecord record);

    VinDecodeRecord getById(Long id);

    VinDecodeRecord getByVin(String vin);

    VinDecodeRecord getByVehicleId(Long vehicleId);

    VinDecodeRecord updateVinDecodeRecord(Long id, VinDecodeRecord updatedRecord);

    void deleteVinDecodeRecord(Long id);
}
