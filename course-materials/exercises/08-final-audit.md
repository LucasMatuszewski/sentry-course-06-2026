# Exercise 8: Final observability audit

**Timebox:** 25 minutes  
**Decision:** Is PolicyLab ready for a controlled production rollout?

## Zero-install path

Use the trainer's project configuration cards, sanitized events, CI excerpts, alert definition, and release trend. No writes or installations are required.

## Task

Audit `policylab-web`, `policylab-api`, and `policylab-android`:

- project ownership and access;
- environment/release consistency;
- source maps/mapping/symbol evidence;
- error grouping and triage workflow;
- distributed trace and structured-log correlation;
- replay/feedback privacy;
- sampling and volume;
- alert ownership/runbook;
- CI token handling;
- AI/Seer/MCP governance.

Return:

1. **Go**, **conditional go**, or **no-go**;
2. the three highest risks;
3. one owner and verification step per risk;
4. evidence already sufficient for approval.

## Success criteria

- Decision is evidence-based and covers all three projects.
- Privacy/security can block rollout.
- Risks are prioritized, owned, and testable.
- No invented UI path or unsupported certainty appears.
- AI remains supplemental and reviewed.

## Hints

1. Use the definition of done in the best-practices guide.
2. Separate missing evidence from known failure.
3. A successful test event is necessary but not sufficient.

<details>
<summary>Solution</summary>

A typical result is **conditional go**: web/API trace and release linkage are good, but one web chunk lacks source mapping, Android mapping verification is incomplete, and the alert still points to a workshop destination. Owners: web CI fixes artifact upload and proves `sourcemaps explain`; mobile CI proves mapping for the shipped build; observability owner replaces/disables the test alert and runs a synthetic notification. Privacy canary must pass before any go decision.

</details>
