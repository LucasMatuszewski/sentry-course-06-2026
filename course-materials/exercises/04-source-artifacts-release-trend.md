# Exercise 4: Source artifacts and release trend

**Timebox:** 25 minutes  
**Decision:** Is release `policylab-web@2026.06.30+3f4c2a1` safe to continue?

## Zero-install path

Use the trainer's before/after stack screenshots, release comparison table, and `sentry-cli sourcemaps explain` sample output. No CLI execution is required.

## Task

1. Open the seeded Angular issue and assess whether frames are source mapped.
2. Confirm release and environment.
3. Identify one likely artifact mismatch from the evidence.
4. In **Releases** or the equivalent current page, compare the target release with the previous production release.
5. Record new/regressed issues, affected users, error rate, and quote latency trend.
6. Recommend proceed, investigate, or rollback, including confidence and missing evidence.

If the page label moved, use global search for “Releases”; do not guess a nested path.

## Success criteria

- Release/environment match is checked before trend comparison.
- Source mapping assessment uses original frames/debug metadata or CLI evidence.
- Comparison uses equivalent windows and production scope.
- Recommendation cites at least three signals and acknowledges sampling/traffic.

## Hints

1. Artifacts must come from the exact deployed build.
2. A release value alone does not prove source-map upload succeeded.
3. Raw counts are misleading when traffic or sampling changed.

<details>
<summary>Solution</summary>

The seeded evidence should show one chunk without matching debug metadata/artifact upload, while release trend shows a new quote failure and worse p95 latency. Recommend investigate/hold or rollback according to trainer thresholds. Fix CI ordering: choose release, production build, inject/upload source artifacts, deploy unchanged output, record deploy, then verify.

</details>
