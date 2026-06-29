# PolicyLab Spring API Design

## Goal

Provide a standalone Spring Boot 4 training API that calculates deterministic insurance quotes and demonstrates safe Sentry error, performance, and log telemetry.

## Architecture

The application uses a compact MVC slice:

- `QuoteController` owns the HTTP contract and selects an explicitly enabled demo scenario.
- `QuoteService` validates no transport concerns and calculates deterministic quote values from a validated request.
- `QuoteTelemetry` is a narrow interface. Its Sentry implementation adds only allow-listed tags and creates the custom slow-operation span.
- `ApiExceptionHandler` translates bean-validation failures to HTTP 400 without intentionally capturing them.

The application is stateless. Quote identifiers are generated UUIDs; no request or quote data is persisted.

## HTTP contract

`POST /api/quotes` accepts:

```json
{
  "vehicleType": "car",
  "manufactureYear": 2022,
  "coverage": "comprehensive",
  "postalCode": "SW1A 1AA",
  "driverAge": 38
}
```

It returns HTTP 200:

```json
{
  "quoteId": "5df04309-03ac-4bb0-9cce-ae57f128da1d",
  "annualPremium": 612.00,
  "currency": "GBP",
  "riskBand": "MEDIUM"
}
```

Validation rejects blank or unsupported vehicle/coverage values, manufacture years outside 1900 through the current year, blank postal codes, and driver ages outside 17 through 100.

`X-Demo-Scenario` accepts `normal`, `server-error`, `slow`, `validation`, and `pii`. Values other than `normal` have an effect only when `DEMO_SCENARIOS_ENABLED=true`; otherwise they are treated as `normal`. Field validation and the validation scenario return expected 400 response bodies directly, without throwing through Sentry's exception resolver. The server-error scenario throws one exception; the automatic Sentry Spring resolver captures it once before the API maps it to HTTP 500. The slow scenario sleeps briefly inside a custom `quote.risk-evaluation` Sentry child span. The PII scenario demonstrates that only allow-listed metadata is attached.

## Privacy and telemetry

Sentry receives the safe tags `coverage`, `vehicle_type`, `risk_band`, and `participant_alias`. The alias comes from `PARTICIPANT_ALIAS` and defaults to `anonymous`. Postal codes and serialized request bodies are never logged or attached as tags or contexts.

Sentry configuration is environment-driven. `SENTRY_DSN` may be empty to disable sending, `SENTRY_ENVIRONMENT` defaults to `training`, `SENTRY_RELEASE` is optional, trace and profile sampling use environment variables, logs are enabled, default PII is disabled, and request-body capture is disabled.

## Dependencies and provenance

The standalone build uses Spring Boot `4.1.0` and Sentry Java `8.46.0`, the current releases reported by Maven Central on 2026-06-29. Java 17 is the minimum toolchain.

The integration structure is derived from the official `getsentry/sentry-java` Spring Boot 4 sample at commit `d8b6ce11cabd05be9a3f03a1d20fe247956d091d`; only the minimal MVC, actuator, validation, test, Sentry Spring Boot 4 starter, and Sentry Logback dependencies are retained.

## Verification

MockMvc integration tests cover the response contract, invalid payloads, the scenario gate, server-error behavior, slow-span delegation, safe telemetry metadata, and actuator health. Unit tests cover deterministic pricing/risk classification. Configuration tests assert privacy-safe defaults.
