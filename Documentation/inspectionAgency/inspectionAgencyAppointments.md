# Inspection Agency Appointments API (project-accurate)

This document covers the endpoint implemented in `InspectionAgencyController`.

## Controller and Base Path
- Controller: `ca.cars365.controllers.InspectionAgencyController`
- Base path: `/api/v1/agencies`

## Endpoint
1) `GET /api/v1/agencies/inspection/appointments`

## Purpose
- Returns all inspection appointments for the currently authenticated inspection agency.

## Authorization (required)
- JWT Bearer token is required.
- Required authority/role: `INSPECTION_AGENCY`
- Header format:

```http
Authorization: Bearer <agency-jwt-token>
```

Token source
- Use agency login endpoint to get token:
  - `POST /api/v1/auth/agency/authenticate`

Security behavior (from `SecurityConfig`)
- Route matcher: `/api/v1/agencies/inspection/**` -> `INSPECTION_AGENCY`
- Missing/invalid token: usually `401 Unauthorized`
- Valid token without `INSPECTION_AGENCY` authority (for example `REGULAR_USER` token): `403 Forbidden`

## Request
- Method: `GET`
- Path params: none
- Query params: none
- Request body: none

## Success Response
- Status: `200 OK`
- Body: `AgencyInspectionAppointmentResponse[]`

### AgencyInspectionAppointmentResponse fields
- `id`: number
- `appointmentNumber`: string
- `appointmentDateTime`: date-time
- `appointmentStatus`: enum (`PENDING`, `CONFIRMED`, `RESCHEDULED`, `CANCELLED`, `COMPLETED`, `NO_SHOW`)
- `bookingNotes`: string or null
- `sellerId`: number
- `sellerName`: string
- `vehicleId`: number
- `vehicleName`: string
- `inspectionReportId`: number or null
- `reportNumber`: string or null
- `createdAt`: date-time
- `updatedAt`: date-time

## Example Response
```json
[
  {
    "id": 27,
    "appointmentNumber": "REQ-1A2B3C4D",
    "appointmentDateTime": "2026-04-10T13:30:00",
    "appointmentStatus": "CONFIRMED",
    "bookingNotes": "Customer requested evening slot",
    "sellerId": 12,
    "sellerName": "Jane Doe",
    "vehicleId": 101,
    "vehicleName": "Honda Civic",
    "inspectionReportId": 15,
    "reportNumber": "RPT-A1B2C3D4",
    "createdAt": "2026-04-05T09:10:11",
    "updatedAt": "2026-04-06T14:25:30"
  }
]
```

## cURL Example
```bash
curl -X GET "http://localhost:8080/api/v1/agencies/inspection/appointments" \
  -H "Authorization: Bearer <agency-jwt-token>"
```

## Implementation Mapping
- Controller method: `getInspectionsByAgency()`
- Service call: `inspectionAgencyService.getAllAppointmentsForAgency()`
- Response DTO: `ca.cars365.dto.response.AgencyInspectionAppointmentResponse`
- Security: `ca.cars365.config.SecurityConfig`
