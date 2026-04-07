# Update Seller Vehicle API (project-accurate)

This document covers the update seller vehicle endpoint implemented in the project.

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

1) Update Seller Vehicle

Endpoint
- PUT /api/v1/seller/vehicles/{vehicleId}

Purpose
- Updates listing details (asking price and listing status) for a vehicle owned by the currently authenticated seller.

Path parameter
- vehicleId: number (required)

Request
- Content-Type: application/json
- Body: SellerVehicleUpdateRequest

Request body fields
- askingPrice: number
- listingStatus: ListingStatus enum

ListingStatus enum values
- DRAFT
- PENDING_CERTIFICATION
- INSPECTED
- READY_TO_PUBLISH
- PUBLISHED
- HIDDEN
- REJECTED
- SOLD

Example request
{
  "askingPrice": 17999.99,
  "listingStatus": "PUBLISHED"
}

cURL example
curl -X PUT "http://localhost:8080/api/v1/seller/vehicles/12" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "askingPrice": 17999.99,
    "listingStatus": "PUBLISHED"
  }'

---

Success response
- Status: 200 OK
- Body: SellerVehicleResponse

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
- listingStatus: ListingStatus enum
- inspectionStatus: InspectionStatus enum
- totalAppointments: number
- totalReports: number
- hasReport: boolean
- inspectionScore: number or null
- latestAppointment: AppointmentSummaryResponse
- latestReport: ReportSummaryResponse
- createdAt: date-time
- updatedAt: date-time

Example response
{
  "id": 12,
  "vehicleName": "Honda Civic",
  "askingPrice": 17999.99,
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
  "listingStatus": "PUBLISHED",
  "inspectionStatus": "COMPLETED",
  "totalAppointments": 1,
  "totalReports": 1,
  "hasReport": true,
  "inspectionScore": 89,
  "latestAppointment": {
    "appointmentNumber": "REQ-1A2B3C4D",
    "appointmentDateTime": "2026-04-05T14:00:00",
    "appointmentStatus": "COMPLETED"
  },
  "latestReport": {
    "reportNumber": "RPT-2026-0001",
    "overallResult": "PASS",
    "inspectionDate": "2026-04-06T00:00:00"
  },
  "createdAt": "2026-03-20T11:22:33",
  "updatedAt": "2026-04-06T10:20:30"
}

---

Current implementation notes
- Ownership check is enforced: seller can only update their own vehicle.
- If vehicleId does not exist, runtime exception is thrown.
- Business side effects by listingStatus:
  - If listingStatus = PUBLISHED:
    - visibleToBuyers is set to true
    - publishedAt is set to current timestamp
  - If listingStatus = HIDDEN:
    - visibleToBuyers is set to false

Common error cases
- 401 Unauthorized: Missing or invalid token.
- 403 Forbidden: Token does not have REGULAR_USER authority.
- Runtime exception (typically 500):
  - Vehicle not found with id
  - Unauthorized to update this vehicle

Implementation mapping
- Controller: ca.cars365.controllers.SellerVehicleController
- Service: ca.cars365.services.VehicleService#updateVehicle
- Service implementation: ca.cars365.services.impl.VehicleServiceImpl#updateVehicle
- Request DTO: ca.cars365.dto.request.SellerVehicleUpdateRequest
- Response DTO: ca.cars365.dto.response.SellerVehicleResponse
- Security: ca.cars365.config.SecurityConfig ("/api/v1/seller/**" -> REGULAR_USER)
