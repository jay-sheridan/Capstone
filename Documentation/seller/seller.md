# Seller APIs (project-accurate)

Base URL
- Local (example): http://localhost:8080
- Controller base path: /api/v1/seller

Authorization
- Required: JWT Bearer token
- Required authority: REGULAR_USER

---

1) Get Inspection Agencies For Seller

Endpoint
- GET /api/v1/seller/agencies

Purpose
- Returns inspection agencies for seller flows (currently backed by all agencies service method).

Success response
- Status: 200 OK
- Body: array of InspectionAgencyResponse

InspectionAgencyResponse fields
- id: number
- name: string
- address: string
- city: string
- provinceState: string
- postalCode: string
- country: string
- phoneNumber: string
- contactPersonName: string

Example cURL
curl -X GET "http://localhost:8080/api/v1/seller/agencies" \
  -H "Authorization: Bearer <your-jwt-token>"

Implementation mapping
- Controller: ca.cars365.controllers.SellerController
- Service: ca.cars365.services.InspectionAgencyService#getAllAgencies
- Security: ca.cars365.config.SecurityConfig ("/api/v1/seller/**" -> REGULAR_USER)
