# LinkedAIS — Backend

An extensible Spring Boot backend for LinkedAIS providing authentication (JWT), user management, social features and education-related APIs.

This README is updated to reflect the current project technologies, how to run it locally, environment configuration, testing tips, and high-level architecture.

Checklist of changes in this update
- Update README to modern, expanded format
- Document current stack and components
- Provide development, testing, and production hints
- Add examples for common flows (auth, protected endpoints)

Table of contents
- About
- Tech stack
- Features
- Quickstart (build & run)
- Environment & configuration
- API highlights & examples
- Database console
- Tests
- Contributing & notes

---

About
-----

This repository contains the backend server for LinkedAIS. It is implemented with Spring Boot (3.x), uses JWT for stateless authentication, and exposes REST endpoints for users, posts, comments, connections, messaging, notifications, course/enrollment management and more.

Tech stack
----------

- Java 17
- Spring Boot 3.4.x (see `pom.xml`)
- Spring Web (REST controllers)
- Spring Security (JWT-based auth)
- Spring Data JPA (Hibernate)
- JJWT (io.jsonwebtoken) for JWT handling
- H2 (in-memory DB for development)
- PostgreSQL (runtime dependency; used for production setups)
- Maven build
- JUnit 5 + spring-boot-starter-test for tests

Features (high level)
---------------------

The project contains many REST controllers and features. Notable areas include:

- Authentication & authorization (register, login, JWT issuance)
- User profile & account management
- Posts, comments, likes
- Bookmarks
- Connections (follow/links) and connection status
- Courses, enrollment, degree progress
- Messaging between users
- Notifications
- File/image upload (multipart support)
- Admin controllers for course/enrollment management

Quickstart — run locally
------------------------

Prerequisites

- Java 17+ installed and JAVA_HOME set
- Maven 3.6+

Build the project (skip tests if you need a faster build):

```powershell
mvn clean package -DskipTests
```

Run using Maven (development):

```powershell
mvn spring-boot:run
```

Or run the packaged jar:

```powershell
java -jar target/linkedais-backend-0.0.1-SNAPSHOT.jar
```

By default the server starts on http://localhost:8080

Configuration & environment variables
-------------------------------------

Application configuration is in `src/main/resources/application.properties`. Key properties used by the app:

- `jwt.secret` — secret key used to sign JWTs (default present for local dev)
- `jwt.expiration-ms` — token lifetime in milliseconds
- `spring.datasource.*` — database connection (H2 by default)
- `spring.jpa.hibernate.ddl-auto` — controls schema generation (default: `update` in this project)
- `spring.h2.console.enabled` — enables H2 web console

For production, override these properties through environment variables or an external `application.yml`/properties file. Example (environment variables):

```powershell
setx SPRING_DATASOURCE_URL "jdbc:postgresql://db-host:5432/linkedais"
setx SPRING_DATASOURCE_USERNAME "your_db_user"
setx SPRING_DATASOURCE_PASSWORD "your_db_password"
setx JWT_SECRET "<secure-random-secret>"
```

Tip: keep `jwt.secret` and DB credentials out of source control. Use a secrets manager for production.

API highlights
--------------

Authentication flows follow a typical pattern — register and login endpoints return a JWT token which you must send in the `Authorization` header for protected endpoints.

Register (example)

Request:

```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "name": "John Doe"
}
```

Successful response contains an auth token and basic user info.

Login (example)

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

Use the token for protected endpoints:

```
Authorization: Bearer <token>
```

Example protected endpoint — current user

```http
GET /api/user/me
Authorization: Bearer <token>
```

Using Postman / HTTP clients
----------------------------

1. Register or login to obtain the JWT token.
2. In Postman, open the request you want to test and add an HTTP header:

Key: Authorization
Value: Bearer <token>

Database console (H2)
---------------------

When running with the default configuration (H2 in-memory), the H2 console is available at:

http://localhost:8080/h2-console

Connection parameters (default):

- JDBC URL: jdbc:h2:mem:linkedais
- Username: sa
- Password: (empty)

Testing
-------

Run unit and integration tests with Maven:

```powershell
mvn test
```

Project layout
--------------

- `src/main/java` — Java source
  - `com.linkedais.backend.controller` — REST controllers
  - `com.linkedais.backend.service` — business logic
  - `com.linkedais.backend.repository` — JPA repositories
  - `com.linkedais.backend.security` — security configuration and JWT filters
- `src/main/resources/application.properties` — default properties

Useful commands
---------------

- Build: `mvn clean package`
- Run: `mvn spring-boot:run`
- Run tests: `mvn test`

Contributing
------------

Contributions are welcome. A suggested workflow:

1. Fork the repository
2. Create a topic branch
3. Run tests and make sure the build passes
4. Open a pull request with a clear description

Notes & security
----------------

- The `jwt.secret` in `application.properties` is suitable only for local development — rotate and secure it in production.
- For a production PostgreSQL deployment, adjust `spring.jpa.hibernate.ddl-auto` (e.g. to `validate`) and manage schema migrations with Flyway or Liquibase.

If you'd like, I can also:

- Generate a Postman collection / OpenAPI (Swagger) spec from controllers
- Add a Dockerfile and docker-compose for local development (Postgres + app)
- Add GitHub Actions workflow for CI (build + tests)

---

If you want any of the optional items above (Docker, OpenAPI, CI), tell me which and I'll add them next.

