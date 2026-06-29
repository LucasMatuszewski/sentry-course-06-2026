# PolicyLab API — Spring Boot

A small Spring Boot 4 API for the PolicyLab Sentry training exercises. It calculates deterministic motor-insurance quotes and includes explicitly gated error, validation, slow-transaction, and privacy demonstrations.

## Requirements

- Java 17 or newer
- No local Gradle installation is required; the Gradle wrapper is included.

The project currently pins:

- Spring Boot `4.1.0`
- Sentry Java `8.46.0`
- Gradle `9.6.1`

These were the current stable releases in their official repositories on 2026-06-29.

## Run locally

```powershell
.\gradlew.bat bootRun
```

macOS or Linux:

```bash
./gradlew bootRun
```

The service listens on port 8080. Verify it with:

```bash
curl http://localhost:8080/actuator/health
```

## Create a quote

```bash
curl -X POST http://localhost:8080/api/quotes \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleType": "car",
    "manufactureYear": 2022,
    "coverage": "comprehensive",
    "postalCode": "00-001",
    "driverAge": 38
  }'
```

Example response:

```json
{
  "quoteId": "5df04309-03ac-4bb0-9cce-ae57f128da1d",
  "annualPremium": 675.00,
  "currency": "PLN",
  "riskBand": "MEDIUM"
}
```

Supported vehicle types are `car`, `motorcycle`, and `van`. Supported coverage values are `third-party` and `comprehensive`. Drivers must be 17 through 100 years old, and manufacture years must be from 1900 through the current year.

## Sentry configuration

All configuration is supplied through environment variables:

| Variable | Default | Purpose |
| --- | --- | --- |
| `SENTRY_DSN` | empty | Project DSN; an empty value disables sending |
| `SENTRY_ENVIRONMENT` | `training` | Sentry environment |
| `SENTRY_RELEASE` | empty | Application release identifier |
| `SENTRY_SAMPLE_RATE` | `1.0` | Error-event sample rate |
| `SENTRY_TRACES_SAMPLE_RATE` | `1.0` | Transaction sample rate |
| `SENTRY_PROFILE_SESSION_SAMPLE_RATE` | `0.0` | Profile-session sample rate |
| `PARTICIPANT_ALIAS` | `anonymous` | Non-identifying workshop alias tag |
| `DEMO_SCENARIOS_ENABLED` | `false` | Enables deliberate training scenarios |

Sentry Logs are enabled. Log events and Sentry tags contain only coverage, vehicle type, risk band, and participant alias. `send-default-pii` is disabled and request-body capture is set to `none`. The postal code and serialized request body are never added to logs, tags, contexts, or explicit Sentry events.

## Training scenarios

The `X-Demo-Scenario` header accepts:

- `normal` — standard quote flow.
- `server-error` — throws one intentional exception; the Sentry Spring resolver captures it once before the API returns HTTP 500.
- `slow` — adds a 750 ms `quote.risk-evaluation` child span.
- `validation` — returns an expected HTTP 400 without an intentional Sentry capture.
- `pii` — runs the successful flow while demonstrating the same strict allow-list of telemetry tags.

All non-normal scenarios are inert unless `DEMO_SCENARIOS_ENABLED=true`. For example:

```bash
DEMO_SCENARIOS_ENABLED=true ./gradlew bootRun

curl -X POST http://localhost:8080/api/quotes \
  -H "Content-Type: application/json" \
  -H "X-Demo-Scenario: slow" \
  -d '{
    "vehicleType": "car",
    "manufactureYear": 2022,
    "coverage": "comprehensive",
    "postalCode": "00-001",
    "driverAge": 38
  }'
```

## Test and package

```powershell
.\gradlew.bat clean build
```

Build a container:

```bash
docker build -t policylab-api-spring .
docker run --rm -p 8080:8080 \
  -e SENTRY_DSN \
  -e SENTRY_ENVIRONMENT=training \
  policylab-api-spring
```

The image runs as an unprivileged `policylab` user and exposes an actuator-based health check.

See [UPSTREAM.md](UPSTREAM.md) for implementation provenance.
