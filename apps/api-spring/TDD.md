# TDD execution record

## RED

Command run on 2026-06-29 before any production Java classes existed:

```powershell
.\gradlew.bat compileTestJava
```

Result: `BUILD FAILED` with 12 compiler errors, all caused by the deliberately absent PolicyLab types. Representative output:

```text
error: package com.policylab.api.telemetry does not exist
import com.policylab.api.telemetry.QuoteTelemetry;

error: cannot find symbol
  private final QuoteService quoteService =
                ^
  symbol:   class QuoteService

error: package com.policylab.api.quote does not exist
import com.policylab.api.quote.QuoteRequest;
```

This was the expected failure: the test contract compiled far enough to resolve Spring Boot 4 test infrastructure, then failed because `QuoteRequest`, `QuoteService`, and `QuoteTelemetry` had not been implemented.

## GREEN

The green command and result are recorded after a fresh full build succeeds.

## Resolved issue — Spring Boot 4.1 + Sentry incompatibility

`@SpringBootTest` integration tests and the runtime container both failed
on Spring Boot 4.1 + `sentry-spring-boot-4-starter:8.46.0`. Coolify
container logs surfaced the root cause:

```
SentrySpringVersionChecker: !Incompatible Spring Boot Version detected!
ClassNotFoundException: org.springframework.boot.web.client.RestClientCustomizer
```

The Sentry artifact named `-4-starter` is in practice the SB3-jakarta
auto-config rebadged; it references SB3-era classes that were relocated
when Spring Boot 4 split its autoconfigure module. Sentry has not yet
shipped a true SB4 starter.

**Resolution:** downgraded the build to Spring Boot 3.5.0 and switched
to `io.sentry:sentry-spring-boot-jakarta-starter:8.46.0`. CI now runs
the full test suite (no `--tests` filter). Runtime boots cleanly.
