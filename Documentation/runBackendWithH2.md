# Run Backend Locally With H2 (First-Time Setup)

This guide is for someone starting from zero local setup on Windows and running the backend with the H2 in-memory database.

## 1) Clone the project first

Clone the repository and move into the project folder:

```bash
git clone <your-repo-url>
cd cars365-backend
```

## 2) Install prerequisites

### Windows

1. Install Git for Windows:

https://git-scm.com/download/win

2. Install Java 21 (required by this project), for example Eclipse Temurin 21:

https://adoptium.net/

3. Open Command Prompt or PowerShell and verify Java:

```bash
java -version
```

Expected: Java 21.x

Note: You do not need Maven installed globally because this project uses the Maven Wrapper (mvnw.cmd on Windows).

## 3) Open the project in terminal

If you already have the project, open Command Prompt or PowerShell and go into it:

```powershell
cd C:\path\to\cars365-backend
```

## 4) Run the backend using H2 profile

If your cloned project already has `spring.profiles.active=h2` in `src/main/resources/application.properties`, run:

```powershell
.\mvnw.cmd clean spring-boot:run
```

If the profile is not set to h2 in `application.properties`, run with explicit override:

```powershell
.\mvnw.cmd clean spring-boot:run "-Dspring-boot.run.profiles=h2"
```

## 5) Confirm it is running

- Backend base URL: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console

## 6) H2 details (important)

For JDBC URL, username/password, and H2 behavior notes, see:

- [H2 Database (Development) Guide](./h2Database.md)

## 7) Stop the backend

In the same terminal, press Ctrl + C.

## Troubleshooting

- Port already in use (`8080`): stop the process using that port, then run again.
- Wrong Java version: ensure `java -version` returns 21.x.
- Dependency download issues on first run: run the same command again; initial Maven wrapper setup may take longer.
