# Exercise 5: Follow a distributed trace

**Timebox:** 25 minutes  
**Decision:** Which span owns the quote delay/failure?

## Zero-install path

Use the trainer's sanitized trace waterfall and correlated log rows. No local services are required.

## Task

1. Start from the prepared Angular quote event.
2. Open the linked trace (use global search for “Traces” if needed).
3. Follow browser navigation/action to `POST /api/quotes`, Spring server work, and downstream pricing/database spans.
4. Identify the critical path and first failing span.
5. Find one correlated structured log and verify trace/release/environment alignment.
6. Explain whether replay adds useful interaction evidence.
7. Propose one instrumentation improvement without collecting more personal data.

## Success criteria

- Client/server spans share a trace and plausible parent-child chain.
- Critical path is based on duration/status, not visual position alone.
- Logs match trace, release, and environment.
- Proposed instrumentation uses a stable operation/name and safe attributes.

## Hints

1. A long parent span may merely contain a slow child.
2. Look for the first error status, not only the final error.
3. Missing browser-to-API linkage may mean propagation target or CORS configuration.

<details>
<summary>Solution</summary>

Expected critical span: the Spring pricing-rule evaluation/downstream lookup, which dominates duration and fails before the API returns an error to Angular. A useful improvement is a `quote.rule` span with governed `rule_code`, not quote form data. Replay can confirm the synthetic user's submit action and lack of duplicate clicks but does not establish backend cause.

</details>
