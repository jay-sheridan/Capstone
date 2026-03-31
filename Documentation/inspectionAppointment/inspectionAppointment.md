# Inspection Appointment APIs (project-accurate)

This document covers inspection appointment APIs implemented in the project.

Base URL
- Local (example): http://localhost:8080
- Controller base path: /api/v1/inspection/appointments

Authorization
- Required: JWT Bearer token
- Required authority: REGULAR_USER
- Header:
  Authorization: Bearer <token>

Security behavior
- Missing/invalid token: typically 401 Unauthorized
- Valid token without REGULAR_USER authority: 403 Forbidden

---

1) Create Inspection Appointment

Endpoint
- POST /api/v1/inspection/appointments

Purpose
- Creates an inspection appointment for a seller vehicle and returns appointment details.

Request
- Content-Type: application/json
- Body: InspectionAppointmentCreateRequest

Request fields
- inspectionAgencyId: number (required)
- vin: string (required)
- appointmentDateTime: date-time (required, intended to be future)

Example request
{
  "inspectionAgencyId": 3,
  "vin": "1HGCM82633A004352",
  "appointmentDateTime": "2026-04-10T13:30:00"
}

Success response
- Status: 200 OK
- Body: InspectionAppointmentResponse

InspectionAppointmentResponse fields
- id: number
- appointmentNumber: string
- inspectionAgencyId: number
- inspectionAgencyName: string
- sellerId: number
- sellerName: string
- vehicleId: number
- vehicleName: string
- appointmentDateTime: date-time
- appointmentStatus: AppointmentStatus enum
- createdAt: date-time
- updatedAt: date-time

Example response
{
  "id": 45,
  "appointmentNumber": "REQ-1A2B3C4D",
  "inspectionAgencyId": 3,
  "inspectionAgencyName": "Precision Inspections Ltd",
  "sellerId": 12,
  "sellerName": "Jane Doe",
  "vehicleId": 99,
  "vehicleName": "Honda Civic",
  "appointmentDateTime": "2026-04-10T13:30:00",
  "appointmentStatus": "CONFIRMED",
  "createdAt": "2026-03-31T09:10:11",
  "updatedAt": "2026-03-31T09:10:11"
}

AppointmentStatus enum values
- PENDING, CONFIRMED, RESCHEDULED, CANCELLED, COMPLETED, NO_SHOW

cURL example
curl -X POST "http://localhost:8080/api/v1/inspection/appointments" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d '{
    "inspectionAgencyId": 3,
    "vin": "1HGCM82633A004352",
    "appointmentDateTime": "2026-04-10T13:30:00"
  }'

---

Business side effects (current implementation)
- Appointment is created with status CONFIRMED.
- A linked inspection report is automatically created.
- A vehicle listing is automatically created for the appointment vehicle.

---

Error behavior and caveats

Validation caveat
- The request DTO has validation annotations (@NotNull, @Future), but controller method does not use @Valid on @RequestBody.
- Because of this, request validation may not be automatically enforced by Spring for this endpoint in current code.

Potential runtime failure scenarios
- Invalid inspectionAgencyId can throw runtime exception when agency is not found.
- Unknown VIN can produce null vehicle lookup and downstream failure.
- These failures may return non-standard error responses, because there is no enabled global generic exception handler.

Known global handled errors relevant to project
- Validation errors (where @Valid is used): 400 ValidationErrorResponse
- AccountAlreadyExistsException: 409 ErrorResponse
- InvalidCredentialsException: 401 ErrorResponse
- InspectionAgencyNotFoundException: 404 ErrorResponse

Implementation mapping
- Controller: ca.cars365.controllers.InspectionAppointmentController
- Service interface: ca.cars365.services.InspectionAppointmentService
- Service implementation: ca.cars365.services.impl.InspectionAppointmentServiceImpl
- Request DTO: ca.cars365.dto.request.InspectionAppointmentCreateRequest
- Response DTO: ca.cars365.dto.response.InspectionAppointmentResponse
- Security: ca.cars365.config.SecurityConfig ("/api/v1/inspection/**" -> REGULAR_USER)
