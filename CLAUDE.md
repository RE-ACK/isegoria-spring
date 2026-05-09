# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./gradlew build
./gradlew clean build

# Run
./gradlew bootRun

# Test
./gradlew test

# Single test class
./gradlew test --tests "com.isegoria.server.SomeTest"
```

## Architecture

Spring Boot 3.4.5 / Java 17 REST API backend. PostgreSQL + JPA + QueryDSL for persistence, JJWT for authentication.

**Package root:** `com.isegoria.server`

### Global Infrastructure (`global/`)

All cross-cutting concerns live here and are already in place:

- **`global/api/Api.java`** — Generic response envelope for every endpoint. Use the static factory methods `Api.ok(body)` and `Api.error(errorCode)`. Timestamps are Asia/Seoul.
- **`global/exception/GlobalExceptionHandler.java`** — `@RestControllerAdvice` that catches `ApiException` plus common JDK/Spring exceptions and maps them to `ErrorCode` values. Add new exception mappings here.
- **`global/exception/ApiException.java`** — Custom `RuntimeException` that carries an `ErrorCode`. Throw this from service/domain code instead of raw exceptions.
- **`global/error/ErrorCode.java`** — Enum of HTTP status + Korean message pairs. Add new codes here when a new error case is needed.
- **`global/message/ResponseMessage.java`** — Enum for success messages (currently empty; populate as features are added).
- **`global/constants/Constants.java`** — `@ConfigurationProperties(prefix = "app")` bean. Application-level config (appName, env) comes from `application.yml`.

### Conventions

- All controller responses must be wrapped in `Api<T>`.
- Business errors must be thrown as `ApiException(ErrorCode.SOME_CODE)` — never let raw exceptions propagate.
- New feature packages (e.g., `user/`, `post/`) should sit alongside `global/` under `com.isegoria.server`.
- Spring Security auto-configuration is excluded; custom security config will be added separately.
- `@EnableJpaAuditing` is active on the main class — JPA entities can use `@CreatedDate` / `@LastModifiedDate`.