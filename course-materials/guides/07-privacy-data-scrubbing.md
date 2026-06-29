# Privacy and data scrubbing

## PolicyLab rule

Workshop telemetry contains only fictional insurance data. Never paste client events, credentials, customer identifiers, production URLs, or screenshots into the tenant, slides, AI tools, or chat.

Treat observability data as production data even when the DSN is public.

## Minimize before scrubbing

1. Do not collect fields that are not needed.
2. Replace identity with a stable synthetic/pseudonymous ID.
3. Use allowlisted tags and contexts.
4. Apply SDK-side filtering before transport.
5. Apply server-side organization/project scrubbing as defense in depth.
6. Verify with a canary secret and event JSON.
7. Define retention, deletion, access, and incident procedures.

Unsafe:

```ts
Sentry.setContext("policyholder", form.value);
Sentry.setTag("email", form.value.email);
```

Safer:

```ts
Sentry.setUser({ id: "user-17" });
Sentry.setTag("quote.channel", "broker");
Sentry.setContext("quote", {
  quote_ref: "Q-1042",
  product_code: "MOTOR_STANDARD",
  rule_code: "AGE_BAND"
});
```

Do not use high-cardinality values as tags unless searching/aggregation genuinely needs them and governance approves.

## Scrubbing test

Send a synthetic event containing obvious canaries:

```text
WORKSHOP_SECRET_DO_NOT_STORE
person@example.invalid
4111 1111 1111 1111
```

Check the event's rendered sections and raw JSON. Confirm values are removed or masked in exception text, request data, breadcrumbs, contexts, tags, logs, replay, and attachments. Delete the test issue after evidence is recorded according to tenant policy.

Scrubbing is not retroactive for data already stored. UI display masking does not prove ingestion/storage removal. Verify actual event payload behavior and document residual risk.

## Before-send guardrail

```ts
beforeSend(event) {
  delete event.request?.cookies;
  if (event.request?.headers) {
    delete event.request.headers.Authorization;
  }
  return event;
}
```

This is illustrative, not a complete policy. Header casing and SDK normalization vary. Prefer official scrubbers and allowlists; test the produced event.

## Replay and AI

Mask replay text and block sensitive media by default. Review Seer/MCP/other AI data flows, permissions, and retention with security and legal teams before enabling them. AI analysis does not relax data minimization.

## UI note

Search **Settings** for **Security & Privacy**, **Data Scrubbing**, **PII**, or **Replay**. Settings locations vary by role and product rollout; lack of visibility may mean insufficient permission.

## Sources and validation

- [Sentry: Security, Legal, and PII](https://docs.sentry.io/security-legal-pii/)
- [Sentry: Data Scrubbing](https://docs.sentry.io/security-legal-pii/scrubbing/)
- [Sentry: Angular Data Collected](https://docs.sentry.io/platforms/javascript/guides/angular/data-management/data-collected/)
- [Sentry: Session Replay privacy](https://docs.sentry.io/platforms/javascript/guides/angular/session-replay/privacy/)

Validation: official privacy, scrubbing, collection, and replay guidance checked 2026-06-29.
