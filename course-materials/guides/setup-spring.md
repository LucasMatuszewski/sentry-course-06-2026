# Setup: Spring Boot

Choose the integration matching the application:

- Spring Boot 3+: Jakarta starter.
- Older Spring Boot: non-Jakarta starter.

Use the dependency/version currently shown by the official setup page or project dependency management; do not copy an unverified version from workshop notes.

Gradle shape for Spring Boot 3+:

```groovy
dependencies {
  implementation("io.sentry:sentry-spring-boot-starter-jakarta:<approved-version>")
}
```

## Configuration

```properties
sentry.dsn=${SENTRY_DSN:}
sentry.environment=${APP_ENVIRONMENT:development}
sentry.release=${APP_RELEASE:policylab-api@local}
sentry.traces-sample-rate=0.10
sentry.send-default-pii=false
```

An empty DSN can disable sending in local/test contexts; verify behavior for the installed SDK. Do not put an auth token in application properties. Prefer environment-specific configuration supplied by the deployment platform.

## Safe enrichment

```java
Sentry.configureScope(scope -> {
  scope.setUser(new User() {{ setId("user-17"); }});
  scope.setTag("quote.channel", "broker");
  scope.setContexts("quote", Map.of(
      "quote_ref", "Q-1042",
      "product_code", "MOTOR_STANDARD"));
});
```

The anonymous initializer is concise workshop notation; normal production code should construct the `User` explicitly. Never attach a request DTO, entity, authorization header, or policyholder object.

Prefer automatic exception capture. Capture manually only at a boundary where the exception would otherwise be handled:

```java
try {
  pricingService.calculate(request);
} catch (PricingRuleException ex) {
  Sentry.captureException(ex);
  throw ex;
}
```

Avoid double capture from both manual code and framework handling.

## Tracing

Automatic Spring MVC/WebFlux/JDBC support depends on libraries and SDK version. Verify the generated server transaction, downstream HTTP spans, and propagation to/from the Angular client. Add custom spans only for meaningful domain work:

```java
var span = Sentry.getSpan() == null
    ? null
    : Sentry.getSpan().startChild("quote.rule", "Evaluate AGE_BAND");
try {
  evaluateRule();
} finally {
  if (span != null) span.finish();
}
```

Use the current SDK's recommended structured span API if it differs.

## Verification

1. send a synthetic handled exception in `staging`;
2. verify exception cause, in-app frame, release, environment, and safe context;
3. submit an Angular quote and confirm one distributed trace;
4. verify health checks are excluded or sampled appropriately;
5. inspect event JSON for PII.

## Zero-install workshop path

Use trainer-provided Spring event/trace JSON and identify starter choice, runtime properties, capture boundary, trace propagation, and unsafe fields.

## Sources and validation

- [Sentry for Spring Boot](https://docs.sentry.io/platforms/java/guides/spring-boot/)
- [Spring Boot configuration](https://docs.sentry.io/platforms/java/guides/spring-boot/configuration/)
- [Spring Boot tracing](https://docs.sentry.io/platforms/java/guides/spring-boot/tracing/)

Validation: official Spring Boot SDK pages checked 2026-06-29. Exact dependency version and supported auto-instrumentation must be taken from the current setup page.
