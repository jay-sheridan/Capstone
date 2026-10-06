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

The default H2 profile stores the local database in `data/`, which Git ignores. Uploaded documents and local database contents are not included in the repository.

## Facebook Login

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
