# Inspection Agency APIs (project-accurate)

This document covers inspection agency APIs in this project:
- Agency listing endpoint
- Agency authentication endpoints

Base URL
- Local (example): http://localhost:8080

---

A) Inspection Agency Listing API

Controller base path
- /api/v1/inspection-agencies

Endpoint
1) GET /api/v1/inspection-agencies
2) GET /api/v1/inspection-agencies/

Purpose
- Returns all inspection agencies.

Authorization
- Endpoint is authenticated (not public).
- Important security detail:
  - Security rules explicitly protect /api/v1/inspection/** for REGULAR_USER.
  - This endpoint path is /api/v1/inspection-agencies/** (hyphenated), so it does not match that specific pattern.
  - Therefore it falls under .anyRequest().authenticated() in current security config.
  - Practical effect: any authenticated user role can access it unless additional restrictions are added.

Request
- No request body

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

Example response
[
  {
    "id": 3,
    "name": "Precision Inspections Ltd",
    "address": "123 Inspection Ave",
    "city": "Toronto",
    "provinceState": "Ontario",
    "postalCode": "M5V 2T6",
    "country": "Canada",
    "phoneNumber": "4165550123",
    "contactPersonName": "Jane Doe"
  }
]

cURL example
curl -X GET "http://localhost:8080/api/v1/inspection-agencies" \
  -H "Authorization: Bearer <your-jwt-token>"

---

B) Inspection Agency Authentication APIs

Controller base path
- /api/v1/auth

These endpoints are public in current security config.

1) Register Inspection Agency

Endpoint
- POST /api/v1/auth/agency/register

Purpose
- Creates a new inspection agency account and returns a JWT.

Request
- Content-Type: application/json
- Body: InspectionAgencyRegistrationRequest

Request fields and validation
- name: required, max 150
- address: required, max 255
- city: required, max 100
- provinceState: required, max 100
- postalCode: required, regex ^[A-Za-z0-9\s-]{5,10}$
- country: required, max 100
- licenseNumber: required, min 5 max 50
- password: required, min size 8 and pattern requiring at least one uppercase and one number
- email: required, valid email
- phoneNumber: required, 10 digits
- contactPersonName: required, max 150

Example request
{
  "name": "Precision Inspections Ltd",
  "address": "123 Inspection Ave",
  "city": "Toronto",
  "provinceState": "Ontario",
  "postalCode": "M5V 2T6",
  "country": "Canada",
  "licenseNumber": "LIC-2026-12345",
  "password": "Secure123",
  "email": "agency@example.com",
  "phoneNumber": "4165550123",
  "contactPersonName": "Jane Doe"
}

Success response
- Status: 200 OK
- Body: AuthenticationResponse

Example response
{
  "token": "<jwt-token>"
}

Error responses
- 400 Bad Request (validation error)
  - Body: ValidationErrorResponse
- 409 Conflict (duplicate license number)
  - Body: ErrorResponse
  - errorType: AccountAlreadyExistsException

2) Authenticate Inspection Agency

Endpoint
- POST /api/v1/auth/agency/authenticate

Purpose
- Authenticates an inspection agency and returns JWT.

Request
- Content-Type: application/json
- Body: InspectionAgencyLoginRequest

Request fields
- licenseNumber: string
- password: string

Example request
{
  "licenseNumber": "LIC-2026-12345",
  "password": "Secure123"
}

Success response
- Status: 200 OK
- Body: AuthenticationResponse

Example response
{
  "token": "<jwt-token>"
}

Error responses
- 401 Unauthorized
  - Body: ErrorResponse
  - errorType: InvalidCredentialsException

Authentication cURL examples

Register:
curl -X POST "http://localhost:8080/api/v1/auth/agency/register" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Precision Inspections Ltd",
    "address": "123 Inspection Ave",
    "city": "Toronto",
    "provinceState": "Ontario",
    "postalCode": "M5V 2T6",
    "country": "Canada",
    "licenseNumber": "LIC-2026-12345",
    "password": "Secure123",
    "email": "agency@example.com",
    "phoneNumber": "4165550123",
    "contactPersonName": "Jane Doe"
  }'

Authenticate:
curl -X POST "http://localhost:8080/api/v1/auth/agency/authenticate" \
  -H "Content-Type: application/json" \
  -d '{
    "licenseNumber": "LIC-2026-12345",
    "password": "Secure123"
  }'

---

Common error payloads used in these APIs

ErrorResponse
- message: string
- status: number
- timestamp: date-time
- errorType: enum

ValidationErrorResponse
- timestamp: date-time
- status: 400
- errorType: VALIDATION_ERROR
- message: string
- missingRequiredFields: string[]
- fieldErrors: object of field -> array of messages

---

Implementation mapping
- Agency listing controller: ca.cars365.controllers.InspectionAgencyController
- Agency listing service: ca.cars365.services.InspectionAgencyService
- Agency listing service impl: ca.cars365.services.impl.InspectionAgencyServiceImpl
- Agency auth controller: ca.cars365.controllers.AuthenticationController
- Agency auth service logic: ca.cars365.services.AuthenticationService
- Security config: ca.cars365.config.SecurityConfig
- Global exception handling: ca.cars365.exception.GlobalExceptionHandler
