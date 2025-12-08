# Copilot instructions — `laundryheroes-core`

Purpose: give an AI coding agent the minimal, actionable context to be productive in this repo.

Big picture
- **Type**: Modular monolith Spring Boot 3 application (Java 21). Entry point: `src/main/java/com/laundryheroes/core/LaundryHeroesCoreApplication.java`.
- **Domains**: code is organized by domain packages under `com.laundryheroes.core` (examples: `account`, `address`, `auth`, `order`, `pickup`, `delivery`, `driver`, `servicecatalog`, `notification`, `websocket`). Follow package boundaries when adding features.
- **Runtime flow**: HTTP controllers -> services -> repositories (Spring Data JPA) -> entities. WebSocket handlers live under `websocket`. Firebase is used for push/auth-related tasks.

Key files & places to inspect
- `pom.xml` — Java 21, Spring Boot, dependencies (Postgres, Firebase, JWT, WebSocket, Lombok, springdoc). Tests are configured in the Maven build (see note below).
- `docker-compose.yml` — local stack definition; contains `db` (Postgres 16) and `app`. Environment vars: `SPRING_PROFILES_ACTIVE` and `FIREBASE_SERVICE_ACCOUNT_JSON`.
- `src/main/resources/firebase/firebase-service-account.json` — Firebase service account (sensitive).
- `src/main/resources/application*.properties` — profile-specific configuration (`application-local.properties`, `application-prod.properties`, `application-ci.properties`).
- Look at domain examples: `src/main/java/com/laundryheroes/core/address/Address.java`, `AddressRepository.java`, `AddressService.java`, `AddressController.java` to see DTO + service + repo patterns.

Build, run & test (exact commands)
- Local dev (no Docker):
  - `./mvnw spring-boot:run` (runs the app on `server.port` 8080)
  - Run unit tests: `./mvnw -DskipTests=false test` (the POM config sets `<skipTests>true</skipTests>` by default, so explicitly set `-DskipTests=false` to run tests)
  - Build jar (used by Dockerfile): `./mvnw -DskipTests package`
- Docker / full stack:
  - `make dev` (invokes `docker-compose up --build`)
  - `docker-compose up --build` — expects `FIREBASE_SERVICE_ACCOUNT_JSON` env var if you want to mount/provide the Firebase key.

Project-specific conventions
- Naming: `*Controller`, `*Service`, `*Repository`, `*Request`/`*Response` DTOs (see `EditProfileRequest`, `AddressResponse`). Keep controllers thin — push logic to services.
- Persistence: Spring Data JPA repositories under domain packages (implement interfaces extending `JpaRepository` or similar). Example file: `src/main/java/com/laundryheroes/core/address/AddressRepository.java`.
- DTOs: request/response objects are separate classes (not reusing entities). Use these DTOs for REST endpoints.
- Config/profiles: tests/ci use profile-specific properties. Set `SPRING_PROFILES_ACTIVE` when running containers.
- Secrets: Firebase key is referenced by env `FIREBASE_SERVICE_ACCOUNT_JSON` and included in `src/main/resources/firebase` in the repo tree — treat it as sensitive; avoid printing or committing real secrets.

Integration points & external systems
- Postgres (docker-compose `db` service). Connection settings come from profile properties.
- Firebase Admin SDK (`com.google.firebase:firebase-admin`) — used for notifications/auth.
- WebSocket endpoints live in `websocket` package.
- Email via Spring Boot `starter-mail`.

When making changes
- If you change controller APIs, update or add tests and confirm `springdoc` picks up the changes (OpenAPI is enabled via `springdoc-openapi-starter-webmvc-ui`).
- Use the domain package structure: add new features inside their logical domain package rather than creating cross-cutting packages.
- Run `./mvnw -DskipTests=false test` locally before raising a PR (CI may skip tests unless configured otherwise).

Examples to reference in PRs or fixes
- Adding a new repository: follow `AddressRepository.java` pattern.
- Adding REST DTOs: follow `EditProfileRequest.java` and `AddressResponse.java` structure.

Notes for the agent
- Prefer minimal, focused changes that respect package boundaries.
- Avoid adding or exposing secrets; consult the repo owner before modifying `src/main/resources/firebase/firebase-service-account.json`.
- Confirm runtime behavior by running `docker-compose up --build` with `make dev` when changes touch integrations.

If anything above is unclear or you want more examples (e.g., common controllers, security config, or a sample integration test), tell me which area to expand and I will update this file.
