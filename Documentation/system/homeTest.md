# Home Test APIs (project-accurate)

These are development/test endpoints exposed by HomeController.

Base URL
- Local (example): http://localhost:8080
- Controller base path: /api/v1/home/test

---

1) GET /api/v1/home/test/
- Returns all users from repository.
- Security config marks this exact path as permitAll.

2) GET /api/v1/home/test/user
- Returns plain text: "Hello, regular user!"
- Requires authenticated user due to security fall-through.

3) GET /api/v1/home/test/agency
- Returns plain text: "Hello, inspection agency!"
- Requires INSPECTION_AGENCY authority.

4) GET /api/v1/home/test/all
- Returns plain text: "Hello, authenticated user!"
- Requires REGULAR_USER or INSPECTION_AGENCY authority.

Implementation mapping
- Controller: ca.cars365.controllers.HomeController
- Security: ca.cars365.config.SecurityConfig
