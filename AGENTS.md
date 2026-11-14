# AGENTS.md

## Repo facts
- Spring Boot 3.2.5 / Java 17 backend, Gradle Kotlin DSL. Multi-tenant gym SaaS using one PostgreSQL schema per gym.
- This is a backend-only Gradle repo. The root `package.json` / `pnpm-lock.yaml` are stale Node leftovers; do not run `npm`/`pnpm` scripts here.
- Existing detailed rules live in `.cursor/project.md`, `.cursor/rules.md`, and `.cursor/anti-patterns.md`. Keep changes consistent with them.

## Commands (Windows PowerShell)
- Build + test: `.\gradlew.bat build`
- Run: `.\gradlew.bat bootRun` (port 8080)
- Tests: `.\gradlew.bat test` (plain JUnit 5 + Mockito, no DB required)
- Single test: `.\gradlew.bat test --tests "com.gym.service.GymServiceTest"`
- Unix/macOS equivalents use `./gradlew`.
- No lint, formatter, or typecheck tasks are configured; Gradle build compiles and tests.
- Config lives in `src/main/resources/application.yml` (DB and JWT). The root `.env` is empty and is not loaded by Spring.
- Environment: Gradle wrapper is 8.5, which does not support running on JDK 23. Use JDK 17 (`build.gradle.kts` declares a 17 toolchain). In IntelliJ set Project SDK and `Gradle JVM` to a JDK 17 install (e.g. `temurin-17`); for the terminal set `JAVA_HOME` to JDK 17 before invoking `gradlew`.
- IntelliJ: run the shared `GymManagementApplication` configuration (`.run/GymManagementApplication.run.xml`) instead of the Gradle `bootRun` task to stream application logs live in the Run console.
- Timezone: PostgreSQL rejects the legacy Windows default `America/Buenos_Aires`; both `bootRun` and the shared run configuration force `-Duser.timezone=America/Argentina/Buenos_Aires`.

## Architecture
- Entry point: `com.gym.GymManagementApplication`.
- Actual packages differ from README: `domain/public_/entity`, `domain/tenant/entity`, `repository/public_`, `repository/tenant`, `service/auth`, `service/gym`, `service/provisioning`, `service/tenant`, `infraestructure/flyway`.
- Tenant resolution: `JwtAuthenticationFilter` loads the user from `public.users` and sets `TenantContext` from the JWT/user record; `TenantInterceptor` does the same for requests. Hibernate's `CurrentTenantIdentifierResolver` selects the schema and `SchemaMultiTenantConnectionProvider` runs `SET search_path`.
- `SUPERADMIN` resolves to the `public` schema. Other roles resolve to their assigned gym schema.
- Tenant is derived from the authenticated user, never from a client header. README instructions about `X-Tenant-ID` are stale.
- `public` schema holds `users`, `gyms`, `refresh_tokens`, and the global `exercises` catalog. Each tenant schema holds members, trainers, products, payments, routines, workout logs, progress, and its selected exercises.
- Roles: `SUPERADMIN`, `ADMIN`, `TRAINER`, `MEMBER`; route rules are in `SecurityConfig`.

## Database and migrations
- `spring.jpa.hibernate.ddl-auto=none`: Hibernate never creates or alters tables. All DDL must be Flyway SQL.
- Public migrations in `src/main/resources/db/migration/public` run automatically at startup.
- Tenant migrations in `src/main/resources/db/migration/tenant` run during gym provisioning (`GymServiceImpl.createGym` -> `TenantProvisioningService.createTenant`) and on every application startup through `TenantMigrationRunner`, which iterates the gyms in `public.gyms` and runs Flyway for each schema. A failing tenant migration is logged and does not stop startup or the remaining tenants.
- Tenant exercises reference `public.exercises` by FK; the global catalog is shared.
- IDs use named PostgreSQL sequences (e.g. `users_seq`, `members_seq`); keep migrations and `@SequenceGenerator` names aligned.

## Conventions
- Follow Controller -> Service -> Repository. Controllers stay thin and return DTOs, never JPA entities.
- Use constructor injection, small focused services, LAZY relationships, and Spring Data derived queries.
- Never hardcode tenant schema names in queries or switch schemas manually; rely on the multi-tenant configuration.
- README is partially stale (tenancy header, DB name, Hibernate table creation, some endpoints). Trust `application.yml` and the code over README.
- CORS allows only `http://localhost:3000` with `Authorization` and `Content-Type` headers.
