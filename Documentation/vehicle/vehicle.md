# Vehicle APIs (project-accurate)

Base URL
- Local (example): http://localhost:8080
- Controller base path: /api/v1/vehicles

Authorization
- No specific matcher exists for `/api/v1/vehicles/**` in security config.
- Access falls under `.anyRequest().authenticated()`.
- Practical effect: any authenticated user role can access.

---

1) Get Vehicle By ID

Endpoint
- GET /api/v1/vehicles/{vehicleId}

Path parameter
- vehicleId: number (Long)

Current implementation status
- Method currently returns `null` in controller.
- Endpoint exists but does not currently return vehicle data.

Implementation mapping
- Controller: ca.cars365.controllers.VehicleController
- Security: ca.cars365.config.SecurityConfig
