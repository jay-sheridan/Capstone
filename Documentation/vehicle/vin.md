# VIN Decode API (project-accurate)

This document describes the VIN-related REST APIs implemented in this project.
It is based on the actual code under src/main/java/ca/cars365 so the paths and response shapes match current implementation.

Base URL
- Local (example): http://localhost:8080
- Controller base path: /api/v1/vin-decode

Authorization and Access
- All VIN endpoints are protected by Spring Security.
- Required authority: REGULAR_USER
- Send JWT in header:
  Authorization: Bearer <token>

Security behavior
- Missing/invalid token typically returns 401 Unauthorized.
- Authenticated user without REGULAR_USER authority returns 403 Forbidden.

External dependency
- VIN decode data is fetched from NHTSA vPIC API:
  https://vpic.nhtsa.dot.gov/api/vehicles/decodevinvalues/{vin}?format=json

---

1) Decode VIN

Endpoint
- GET /api/v1/vin-decode/{vin}

Purpose
- Decodes a VIN using NHTSA and returns selected vehicle details.

Path parameter
- vin: string
  - Expected VIN length is usually 17 characters.
  - Current code does not perform local Bean Validation on this path parameter.
  - VIN validity is effectively determined by NHTSA response ErrorCode.

Success response
- Status: 200 OK
- Body: VehicleVinResponse

Example success body
{
  "engineModel": "2.0L L4",
  "make": "HONDA",
  "model": "CIVIC",
  "modelYear": "2018",
  "fuelTypePrimary": "Gasoline",
  "vehicleType": "PASSENGER CAR",
  "plantCountry": "CANADA",
  "transmissionStyle": "CVT"
}

Possible error responses
- 400 Bad Request
  - Trigger: NHTSA returns no results, or ErrorCode is not "0".
  - Exception: InvalidVinException
  - Current response body is plain string message.

Example 400 body (string)
"Invalid VIN: 123 | Error: 1 - Check Digit (9th position) does not calculate properly"

Notes
- Service checks NHTSA field ErrorCode. Only ErrorCode == "0" is treated as valid decode.
- If NHTSA data format changes or upstream call fails unexpectedly, behavior may surface as generic server error depending on runtime exception handling.

---

2) Get VIN Decode Record By Vehicle ID

Endpoint
- GET /api/v1/vin-decode/records/{vehicleId}

Purpose
- Returns a stored VIN decode record linked to a specific vehicle.

Path parameter
- vehicleId: number (Long)

Success response (current implementation state)
- Status: 200 OK
- Body: VinDecodeRecord object when service implementation exists.

Current code status
- VinDecodeRecordServiceImpl.getByVehicleId(...) is currently not implemented and returns null.
- Therefore, current runtime response is typically 200 OK with null body.

Example body (current behavior)
null

Intended VinDecodeRecord fields (entity)
- id (Long)
- vin (String)
- make (String)
- model (String)
- modelYear (Integer)
- engineModel (String)
- transmissionStyle (String)
- fuelTypePrimary (String)
- vehicleType (String)
- plantCountry (String)
- vinDecodedAt (LocalDateTime)
- decodeSuccess (Boolean)
- vehicle (Vehicle reference)
- plus BaseEntity fields (from inheritance)

---

3) Get VIN Decode Record By VIN

Endpoint
- GET /api/v1/vin-decode/records/by-vin/{vin}

Purpose
- Returns a stored VIN decode record by VIN value.

Path parameter
- vin: string

Success response (current implementation state)
- Status: 200 OK
- Body: VinDecodeRecord object when service implementation exists.

Current code status
- VinDecodeRecordServiceImpl.getByVin(...) is currently not implemented and returns null.
- Therefore, current runtime response is typically 200 OK with null body.

Example body (current behavior)
null

---

Common Models

VehicleVinResponse
- engineModel: string
- make: string
- model: string
- modelYear: string
- fuelTypePrimary: string
- vehicleType: string
- plantCountry: string
- transmissionStyle: string

VinDecodeRecord (entity-backed response)
- Represents persisted decode metadata and linkage to Vehicle.
- Includes VIN uniqueness constraint and optional decoded attributes.

---

Error handling details

VIN-specific error
- InvalidVinException is handled by GlobalExceptionHandler.
- HTTP status: 400
- Response body: plain text message (not ErrorResponse DTO).

Other global errors
- Authentication/authorization errors are handled by Spring Security filter chain.
- No generic Exception handler is enabled in GlobalExceptionHandler at this time.

---

cURL examples

1) Decode VIN

curl -X GET "http://localhost:8080/api/v1/vin-decode/1HGCM82633A004352" \
  -H "Authorization: Bearer <your-jwt-token>"

2) Get record by vehicleId

curl -X GET "http://localhost:8080/api/v1/vin-decode/records/1001" \
  -H "Authorization: Bearer <your-jwt-token>"

3) Get record by VIN

curl -X GET "http://localhost:8080/api/v1/vin-decode/records/by-vin/1HGCM82633A004352" \
  -H "Authorization: Bearer <your-jwt-token>"

---

Implementation mapping
- Controller: ca.cars365.controllers.VinDecodeController
- Service interface: ca.cars365.services.VinDecodeRecordService
- Service implementation: ca.cars365.services.impl.VinDecodeRecordServiceImpl
- Decode response DTO: ca.cars365.dto.response.VehicleVinResponse
- Persistence entity: ca.cars365.entity.VinDecodeRecord
- Repository: ca.cars365.repositories.VinDecodeRecordRepository
- Exception class: ca.cars365.exception.InvalidVinException
- Exception handling: ca.cars365.exception.GlobalExceptionHandler
- Security rule: ca.cars365.config.SecurityConfig (requestMatchers("/api/v1/vin-decode/**").hasAuthority(REGULAR_USER))

---

Known gaps (current code)
- VehicleService is injected in VinDecodeController but not used.
- VinDecodeRecordServiceImpl has unimplemented CRUD/read methods returning null.
- vinDecodeRecordRepository field in service implementation is currently assigned null, so repository-backed persistence logic is not active.

If you want, the next step can be implementing the missing VIN record service methods and aligning error payloads to structured ErrorResponse for consistency across the API.
