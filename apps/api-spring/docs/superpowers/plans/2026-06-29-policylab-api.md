# PolicyLab Spring API Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:test-driven-development to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a standalone Spring Boot 4 quote API with deliberately gated Sentry training scenarios and privacy-safe telemetry.

**Architecture:** A compact MVC controller delegates deterministic pricing to a service and Sentry operations to a narrow telemetry adapter. Bean validation and one exception handler keep expected 400 responses separate from intentionally unhandled 500 demonstrations.

**Tech Stack:** Java 17, Gradle 9 wrapper, Spring Boot 4.1.0, Sentry Java 8.46.0, JUnit 5, MockMvc.

---

### Task 1: Build scaffold and failing contract tests

**Files:**
- Create: `settings.gradle.kts`
- Create: `build.gradle.kts`
- Create: `src/test/java/com/policylab/api/QuoteApiIntegrationTest.java`
- Create: `src/test/java/com/policylab/api/QuoteServiceTest.java`

- [ ] Create the minimal Gradle Java project with Spring MVC, validation, actuator, Sentry, and test dependencies.
- [ ] Write tests for a valid quote, validation errors, disabled/enabled scenarios, safe telemetry arguments, slow spans, deterministic pricing, and health.
- [ ] Run `./gradlew test` and record the expected compilation failure caused by the absent application classes.

### Task 2: Minimal quote implementation

**Files:**
- Create: `src/main/java/com/policylab/api/PolicyLabApplication.java`
- Create: `src/main/java/com/policylab/api/quote/QuoteRequest.java`
- Create: `src/main/java/com/policylab/api/quote/QuoteResponse.java`
- Create: `src/main/java/com/policylab/api/quote/QuoteService.java`
- Create: `src/main/java/com/policylab/api/quote/QuoteController.java`
- Create: `src/main/java/com/policylab/api/quote/ApiExceptionHandler.java`

- [ ] Implement records for the exact request and response fields.
- [ ] Implement deterministic premium and risk-band calculation.
- [ ] Implement `POST /api/quotes`, bean-validation handling, and gated demo scenarios.
- [ ] Run `./gradlew test` and fix only production behavior needed by the failing tests.

### Task 3: Sentry telemetry and configuration

**Files:**
- Create: `src/main/java/com/policylab/api/telemetry/QuoteTelemetry.java`
- Create: `src/main/java/com/policylab/api/telemetry/SentryQuoteTelemetry.java`
- Create: `src/main/resources/application.properties`
- Create: `src/test/java/com/policylab/api/ConfigurationSafetyTest.java`

- [ ] Test that telemetry receives only coverage, vehicle type, risk band, and participant alias.
- [ ] Test that configuration disables default PII and request-body capture while enabling logs.
- [ ] Implement safe tags and `quote.risk-evaluation` as a custom child span.
- [ ] Bind DSN, environment, release, sampling, scenario gate, and alias settings from environment variables.
- [ ] Run `./gradlew test`.

### Task 4: Packaging and provenance

**Files:**
- Create: `README.md`
- Create: `UPSTREAM.md`
- Create: `Dockerfile`
- Create: `.dockerignore`
- Create: Gradle wrapper files

- [ ] Document setup, configuration, API examples, scenarios, privacy behavior, and health checks.
- [ ] Document the exact upstream repository, commit, sample path, retained ideas, and intentionally omitted sample features.
- [ ] Generate a Gradle wrapper that can run with Java 17.
- [ ] Add a two-stage Docker image with a non-root runtime user.
- [ ] Run `./gradlew clean build` and inspect the final file list to confirm all writes remain below `apps/api-spring/`.

No commit step is included because the task explicitly forbids Git-state changes.
