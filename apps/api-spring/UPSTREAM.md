# Upstream provenance

This standalone training project is based on the official Sentry Java repository:

- Repository: `https://github.com/getsentry/sentry-java`
- Pinned commit reviewed: `d8b6ce11cabd05be9a3f03a1d20fe247956d091d`
- Sample path: `sentry-samples/sentry-samples-spring-boot-4`

The pinned revision was available locally at `C:\Users\BiuroEdukey\DEV\COURSES\sentry-java-upstream` and its checked-out `HEAD` was verified before implementation.

## Exact reuse scope

No upstream Java, Kotlin, resource, Gradle, wrapper, or documentation file was copied into this project, either verbatim or with local edits. The Gradle wrapper in this directory was generated independently for Gradle 9.6.1.

The implementation was written specifically for PolicyLab after inspecting the upstream sample's `build.gradle.kts`, `src/main/resources/application.properties`, application bootstrap, and MVC controller/service examples. The only adapted scope is the public integration pattern: use the released Spring Boot 4 starter and Logback artifact, configure the SDK through Spring properties, obtain the current Sentry span and create a child span, and rely on the Spring exception resolver for uncaught controller exceptions.

## Ideas retained

- The `io.sentry:sentry-spring-boot-4-starter` integration.
- The separate Sentry Logback integration and Sentry Logs configuration.
- Environment/property-based Sentry configuration.
- A manually created child span based on `Sentry.getSpan()` and `startChild(...)`.
- Automatic Spring MVC exception-resolver capture for a deliberate training failure.
- Spring Boot actuator support.

## Deliberate differences

The upstream sample is an integration coverage application and includes many technologies. PolicyLab keeps only the minimum dependencies needed for this API and intentionally omits:

- Kafka and queue tracing
- GraphQL and subscriptions
- Quartz and scheduled jobs
- JDBC and an in-memory database
- Spring Security
- WebFlux, WebSocket, and reactive clients
- Cache tracing
- OpenTelemetry agents
- Async profiling

The upstream sample enables default PII and request-body capture for SDK system testing. PolicyLab deliberately reverses those settings: default PII is disabled, body capture is `none`, the DSN defaults to empty, and only allow-listed business dimensions are attached.

## Released dependency versions

The upstream build consumes local Sentry modules, so it is not directly reusable as a standalone project. This project instead resolves released artifacts from Maven Central:

- `org.springframework.boot` `4.1.0`
- `io.sentry:sentry-spring-boot-4-starter` `8.46.0`
- `io.sentry:sentry-logback` `8.46.0`

Maven Central metadata was queried on 2026-06-29 before these versions were selected.
