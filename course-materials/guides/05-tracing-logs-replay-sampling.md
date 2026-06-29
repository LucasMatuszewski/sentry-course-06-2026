# Tracing, logs, replay, and sampling

## One evidence chain

For PolicyLab, the ideal chain is:

```text
Angular quote click
  -> POST /api/quotes client span
  -> Spring HTTP server span
  -> pricing-rule span / database span
  -> error event linked to the trace
  -> structured logs carrying trace context
  -> replay showing the synthetic user's interaction
```

Trace propagation requires compatible headers and trust boundaries. Configure browser trace propagation only to known PolicyLab API origins. CORS must allow the relevant Sentry trace headers. Never propagate tracing headers indiscriminately to third parties.

## Investigation

1. Start from an issue or slow transaction.
2. Open the linked trace and find the first failing or dominant-duration span.
3. Compare span status, operation, duration, and attributes.
4. Pivot to correlated logs around the trace time/ID.
5. Use replay to understand user interaction, not to replace stack/trace evidence.
6. Verify the same release and environment across services.

## Structured logs

Log stable, searchable fields:

```json
{
  "event": "quote.calculation.failed",
  "quote_ref": "Q-1042",
  "rule_code": "AGE_BAND",
  "environment": "staging"
}
```

Avoid secrets, raw request bodies, email, names, addresses, full policy data, or dynamic text as the only message. Let the SDK add trace linkage where supported. Keep the message template stable and values structured.

## Replay privacy

Session Replay is sampling plus data capture, not screen recording consent by itself. Mask text and block sensitive media by default; verify behavior in the actual app, including custom components, shadow DOM, and mobile/web views. Use synthetic workshop data only.

## Sampling strategy

Sampling is a budget and signal decision:

- errors: normally retain actionable errors, then filter known noise deliberately;
- traces: choose `tracesSampleRate` or a sampler with higher rates for failures/new releases and lower rates for health checks;
- replay: keep a small baseline and a higher on-error rate, subject to privacy approval;
- logs: capture structured diagnostic logs at useful levels; avoid debug floods.

Example logic, not production policy:

```ts
tracesSampler: context => {
  if (context.name?.includes("/health")) return 0;
  if (context.name?.includes("/api/quotes")) return 0.25;
  return 0.05;
}
```

Measure accepted, sampled, and dropped volume after rollout. Sampling decisions must be consistent enough to preserve distributed traces.

## UI note

Current product navigation may expose telemetry under **Explore**, **Traces**, **Logs**, and **Replays**. Use global navigation/search when a label is unavailable, and confirm dataset/time/project/environment before comparing counts.

## Sources and validation

- [Sentry: Retrieve a Trace](https://docs.sentry.io/api/discover/retrieve-a-trace/)
- [Sentry: Logs for Angular](https://docs.sentry.io/platforms/javascript/guides/angular/logs/)
- [Sentry: Session Replay for Angular](https://docs.sentry.io/platforms/javascript/guides/angular/session-replay/)
- [Sentry: Sampling for Angular](https://docs.sentry.io/platforms/javascript/guides/angular/configuration/sampling/)
- [Sentry: Spring Boot Tracing](https://docs.sentry.io/platforms/java/guides/spring-boot/tracing/)

Validation: official tracing, logs, replay, and sampling guidance checked 2026-06-29.
