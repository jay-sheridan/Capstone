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