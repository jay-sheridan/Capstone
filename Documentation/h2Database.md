H2 Database (Development) — Access & Notes

Purpose
-------
The embedded H2 database is provided for local development and to help frontend developers test the backend APIs quickly. It is intended for development and testing only — do not use it in production.

How to access the H2 Console
---------------------------
- Console URL (default for this project): http://localhost:8080/h2-console
- Web path is configured by: spring.h2.console.path=/h2-console (see `src/main/resources/application-h2.properties`)

JDBC connection details (pre-configured for this project)
- JDBC URL: jdbc:h2:mem:testdb
- Driver class: org.h2.Driver
- Username: sa
- Password: (leave blank)

When you open the H2 console in your browser:
1. Visit http://localhost:8080/h2-console
2. Enter the JDBC URL: jdbc:h2:mem:testdb
3. Username: sa
4. Leave Password empty
5. Click "Connect"

Important behavior & limitations
--------------------------------
- Ephemeral data: The project uses an in-memory H2 database. All data stored in the H2 instance will be lost when the application restarts (or the JVM exits).
- Purpose: This database is provided so frontend developers can call and test backend endpoints and inspect data during development. It is not meant as persistent storage.

Persisting initial data
-----------------------
If you want the application to pre-load data for development each time it starts, add SQL insert statements to the project data file:

- File to edit: `src/main/resources/data-h2.sql`
- The Spring profile / properties currently configure this file with:
  spring.sql.init.data-locations=classpath:data-h2.sql

Note: Because the database is in-memory and `spring.jpa.hibernate.ddl-auto` is set to `create-drop` in the H2 profile, the schema will be recreated on each run and then `data-h2.sql` will be executed to re-seed data.

Security note
-------------
- The H2 console is enabled for convenience in development (see `application-h2.properties`). Do NOT enable or expose the H2 console in production environments. If you need, we can gate it behind a Spring Profile so it only loads for `dev` or `h2` profiles.

Where to look in the codebase
----------------------------
- H2 properties: `src/main/resources/application-h2.properties`
- Console path: `spring.h2.console.path=/h2-console`
- Seed SQL location: `src/main/resources/data-h2.sql` (create this file if it does not exist)
