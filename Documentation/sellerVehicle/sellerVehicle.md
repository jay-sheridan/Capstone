# Seller Vehicle APIs (project-accurate)

This document covers the seller vehicle endpoint implemented in the project.

Base URL
- Local (example): http://localhost:8080
- Controller base path: /api/v1/seller/vehicles

Authorization
- Required: JWT Bearer token
- Required authority: REGULAR_USER
- Header:
  Authorization: Bearer <token>

Security behavior
- Missing/invalid token: typically 401 Unauthorized
- Valid token without REGULAR_USER authority: 403 Forbidden

---

1) Get Seller Vehicles

Endpoint
- GET /api/v1/seller/vehicles
- GET /api/v1/seller/vehicles/

Purpose
- Returns all vehicles that belong to the currently authenticated seller (current user).

Request
- No request body

Success response
- Status: 200 OK
- Body: array of SellerVehicleResponse

SellerVehicleResponse fields
- id: number
- vehicleName: string
- askingPrice: number
- vin: string
- make: string
- model: string
- modelYear: number
- photos: VehiclePhotoResponse[]
- primaryPhotoUrl: string or null
- inspectionStatus: InspectionStatus enum
- totalAppointments: number
- totalReports: number
- hasReport: boolean
- latestAppointment: AppointmentSummaryResponse
- latestReport: ReportSummaryResponse
- createdAt: date-time
- updatedAt: date-time

VehiclePhotoResponse fields
- id: number
- fileUrl: string
- primaryPhoto: boolean
- displayOrder: number

AppointmentSummaryResponse fields
- appointmentNumber: string
- appointmentDateTime: date-time
- appointmentStatus: AppointmentStatus enum

ReportSummaryResponse fields
- reportNumber: string
- overallResult: ReportResult enum
- inspectionDate: date

Example response
[
  {
    "id": 12,
    "vehicleName": "Honda Civic",
    "askingPrice": 14500.0,
    "vin": "1HGCM82633A004352",
    "make": "HONDA",
    "model": "CIVIC",
    "modelYear": 2018,
    "photos": [
      {
        "id": 101,
        "fileUrl": "https://cdn.example.com/vehicles/12/front.jpg",
        "primaryPhoto": true,
        "displayOrder": 1
      }
    ],
    "primaryPhotoUrl": "https://cdn.example.com/vehicles/12/front.jpg",
    "inspectionStatus": "APPOINTMENT_BOOKED",
    "totalAppointments": 1,
    "totalReports": 1,
    "hasReport": true,
    "latestAppointment": {
      "appointmentNumber": "REQ-1A2B3C4D",
      "appointmentDateTime": "2026-04-05T14:00:00",
      "appointmentStatus": "CONFIRMED"
    },
    "latestReport": {
      "reportNumber": "RPT-2026-0001",
      "overallResult": "PASS",
      "inspectionDate": "2026-04-06"
    },
    "createdAt": "2026-03-20T11:22:33",
    "updatedAt": "2026-03-25T10:10:10"
  }
]

Enum values used in this response
- InspectionStatus:
  DRAFT, SUBMITTED, AGENCY_ASSIGNED, APPOINTMENT_BOOKED, INSPECTION_IN_PROGRESS,
  BACKGROUND_CHECK_IN_PROGRESS, ON_HOLD, APPROVED, REJECTED, COMPLETED, CANCELLED
- AppointmentStatus:
  PENDING, CONFIRMED, RESCHEDULED, CANCELLED, COMPLETED, NO_SHOW
- ReportResult:
  PENDING, ON_HOLD, PASS, FAIL, CONDITIONAL_PASS

cURL example
curl -X GET "http://localhost:8080/api/v1/seller/vehicles" \
  -H "Authorization: Bearer <your-jwt-token>"

---

Current implementation notes
- Data source uses authenticated user context to fetch vehicles:
  vehicleRepository.findBySellerId(currentUserId)
- The mapper computes latest appointment/report using index 0 of lists. If a vehicle has no appointment or no report loaded as expected, runtime failures are possible.
- Error responses for these runtime failures are not standardized via a global generic exception handler in the current code.

Implementation mapping
- Controller: ca.cars365.controllers.SellerVehicleController
- Service: ca.cars365.services.VehicleService#getSellerVehicles
- Service implementation: ca.cars365.services.impl.VehicleServiceImpl
- DTOs:
  SellerVehicleResponse, VehiclePhotoResponse, AppointmentSummaryResponse, ReportSummaryResponse
- Security: ca.cars365.config.SecurityConfig ("/api/v1/seller/**" -> REGULAR_USER)
