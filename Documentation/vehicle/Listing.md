# Vehicle Listing APIs (project-accurate)

This document describes the REST API currently implemented by VehicleListingController.
It is based on the code under src/main/java/ca/cars365 so paths and response fields match the current implementation.

Base URL
- Local (example): http://localhost:8080
- Controller base path: /api/v1/listings

Authorization
- Required: JWT Bearer token
- Required authority: REGULAR_USER
- Header:
  Authorization: Bearer <token>

Security behavior
- Missing/invalid token: typically 401 Unauthorized
- Valid token without REGULAR_USER authority: 403 Forbidden

---

1) Get Visible Vehicle Listings

Endpoint
- GET /api/v1/listings
- GET /api/v1/listings/

Purpose
- Returns listings that are marked as visible to buyers.

Request
- No request body

Data filter in current code
- Uses repository query: findByVisibleToBuyersTrue()

Success response
- Status: 200 OK
- Body: array of VehicleListingResponse

VehicleListingResponse fields
- id: number
- title: string
- description: string or null
- listingPrice: number
- status: ListingStatus enum
- inspectionAgencyName: string
- imageUrl: string or null
- inspectionScore: number or null
- publishedAt: date-time
- createdAt: date-time
- updatedAt: date-time

Example response
[
  {
    "id": 55,
    "title": "Honda Civic 2018",
    "description": "Listing for Honda Civic 2018",
    "listingPrice": 14500.0,
    "status": "HIDDEN",
    "inspectionAgencyName": "Sheridan Auto Inspect",
    "imageUrl": null,
    "inspectionScore": 87,
    "publishedAt": "2026-04-01T12:00:00",
    "createdAt": "2026-04-01T12:00:00",
    "updatedAt": "2026-04-03T09:30:00"
  }
]

Enum values used in response
- ListingStatus:
  DRAFT, PENDING_CERTIFICATION, READY_TO_PUBLISH, PUBLISHED, HIDDEN, REJECTED, SOLD

cURL example
curl -X GET "http://localhost:8080/api/v1/listings" \
  -H "Authorization: Bearer <your-jwt-token>"

---

Current implementation notes
- VehicleListingController currently contains one endpoint only: getVisibleListings().
- imageUrl is currently hardcoded to null in the mapping logic.
- listingPrice defaults to 0.0 at entity level if not set.
- publishedAt is auto-populated on insert when null via @PrePersist.
- createVehicleListingForVehicle(...) creates listings with status HIDDEN and visibleToBuyers false, so newly created listings are not returned by this endpoint until visibility is changed.

Implementation mapping
- Controller: ca.cars365.controllers.VehicleListingController
- Service interface: ca.cars365.services.VehicleListingService
- Service implementation: ca.cars365.services.impl.VehicleListingServiceImpl
- Response DTO: ca.cars365.dto.response.VehicleListingResponse
- Repository: ca.cars365.repositories.VehicleListingRepository
- Entity: ca.cars365.entity.VehicleListing
- Security rule: ca.cars365.config.SecurityConfig ("/api/v1/listings/**" -> REGULAR_USER)
