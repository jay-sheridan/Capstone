# Capstone

## Local setup

Install Java 21 or newer. Before starting the backend, set `JWT_SECRET_KEY` to a Base64-encoded random key of at least 32 bytes. Keep this key private and use a separate key for each deployed environment.

In PowerShell, generate a key for the current terminal session and run the backend:

```powershell
$keyBytes = New-Object byte[] 32
[System.Security.Cryptography.RandomNumberGenerator]::Fill($keyBytes)
$env:JWT_SECRET_KEY = [Convert]::ToBase64String($keyBytes)
.\mvnw.cmd spring-boot:run
```

The backend uses Neon PostgreSQL. Configure the connection below before starting it. Uploaded documents in `data/` are ignored by Git.

## Neon PostgreSQL

Create a project at https://console.neon.tech and open **Connect** to get its hostname, database, role, and password. Copy `neon-local.properties.example` to `neon-local.properties`, fill in the values, and keep `sslmode=require` in the JDBC URL. The local file is ignored by Git. Environment variables with the same names can also supply these values.

Start with Java 21 and `JWT_SECRET_KEY` configured as above:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=neon"
```

The default Neon profile creates/updates tables from the entities. Existing local database records are not transferred. Use `NEON_DDL_AUTO=validate` with managed schema migrations for a deployed database. The frontend continues to call the backend and needs no database credentials.

Database integration tests require a separate Neon test branch configured through `NEON_TEST_DATABASE_URL`, `NEON_TEST_DATABASE_USERNAME`, and `NEON_TEST_DATABASE_PASSWORD`. They are skipped when the test URL is absent. Unit tests run without a database.

## Google sign-in

Create a Google OAuth client with application type **Web application** in Google Cloud. Add `https://localhost:5173` to its authorized JavaScript origins (and add your production frontend origin when deploying). Configure the consent screen and test users if the app is in testing.

Set `$env:GOOGLE_CLIENT_ID = 'your-client-id.apps.googleusercontent.com'` in the backend terminal before running `.\mvnw.cmd spring-boot:run`. This flow does not require a client secret. `/api/v1/auth/google/config` exposes the public client ID; without configuration it returns HTTP 503.

Login and signup use Google's official Identity Services button. The backend verifies Google's signed ID token, audience, issuer, and expiry, stores the Google subject in `users.google_id`, and issues a Cars365 JWT. Existing accounts with the same email must use their original sign-in method; accounts are not automatically linked. A real Google account login must be tested after configuring the client ID.

See https://developers.google.com/identity/gsi/web/guides/get-google-api-clientid and https://developers.google.com/identity/gsi/web/guides/verify-google-id-token.

## Facebook setup

The web login and signup buttons use the Facebook JavaScript SDK. The backend verifies the token with Meta before issuing a Cars365 JWT. Facebook identities are stored separately; existing email accounts are not automatically linked.

Create a Meta developer app with Facebook Login enabled, configure its website origin for your local frontend, and set these environment variables in the backend terminal before starting it:

```powershell
$env:FACEBOOK_APP_ID = 'your-app-id'
$env:FACEBOOK_APP_SECRET = 'your-app-secret'
# Optional: use a Graph API version supported by your Meta app (default v23.0).
$env:FACEBOOK_GRAPH_VERSION = 'v23.0'
.\mvnw.cmd spring-boot:run
```

Keep the App Secret on the backend only and out of source control. Restart the backend after configuring credentials. Use a Meta app test user or app team account during development. The account must allow email access. Follow Meta's current allowed-domain and HTTPS requirements when configuring the website; use a permitted local HTTPS setup if needed.

Check `/api/v1/auth/facebook/config`: it returns only the public App ID and API version, or HTTP 503 when credentials are missing. Without a configured Meta app, the buttons show a setup message. A real Facebook login must be tested with that app before deployment.
