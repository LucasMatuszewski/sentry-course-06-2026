# Exercise 3: Safe context and privacy

**Timebox:** 20 minutes  
**Decision:** Which diagnostic context may PolicyLab collect?

## Zero-install path

Review the snippets and trainer-provided sanitized raw-event JSON. No SDK, build, or tenant write access is needed.

## Task

Classify each field as **keep**, **transform**, or **drop**:

```text
quote_id=Q-1042
product_code=MOTOR_STANDARD
rule_code=AGE_BAND
policyholder_name=Jamie Example
email=jamie@example.invalid
authorization=Bearer workshop-secret
postal_code=00-001
premium_band=200_300
request_body=<entire quote form>
```

Then:

1. design tags and a `quote` context using only necessary synthetic data;
2. propose SDK-side filtering and server-side scrubbing;
3. state where a canary must be checked in the final event;
4. explain why UI masking alone is insufficient.

## Success criteria

- Authorization and full request body are dropped.
- Direct identity is dropped; quote reference is synthetic/pseudonymous and kept as context only if needed.
- Low-cardinality business codes are distinguished from sensitive/high-cardinality values.
- Defense in depth and raw-event verification are included.

## Hints

1. Tags are indexed; context can carry richer non-indexed diagnostics.
2. Ask whether an on-call engineer needs the value to act.
3. Scrubbing after storage cannot undo prior exposure.

<details>
<summary>Solution</summary>

Keep governed codes such as `product_code`, `rule_code`, and perhaps `premium_band` as tags. Keep synthetic `quote_ref` in context only if correlation is necessary. Drop name, email, authorization, postal code, and request body. Minimize before send, remove sensitive headers/fields in the SDK, apply server-side scrubbing, and check rendered sections plus raw JSON, breadcrumbs, logs, replay, and attachments with a synthetic canary.

</details>
