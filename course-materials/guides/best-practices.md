# Sentry best practices

## Instrument for decisions

- Define owner, environment, release, and privacy contracts before SDK rollout.
- Use one project per deployable/ownership boundary.
- Keep messages stable; put governed dimensions in tags or structured attributes.
- Capture exceptions once at the correct boundary and preserve the cause.
- Connect errors, traces, logs, replay, releases, and feedback instead of treating them as separate silos.

## Release discipline

- Generate one immutable release ID in CI and embed it in the deployed artifact.
- Upload source maps, mappings, and symbols from that exact build before deployment.
- Associate commits/deploys, then verify with a synthetic event.
- Roll forward/resolve in a release; do not mark fixed without a verifiable release.

## Signal and cost

- Filter health checks, browser extensions, known bots, and expected cancellations only with measured evidence.
- Sample traces/replays/logs deliberately; preserve high-value failures and new releases.
- Avoid high-cardinality tags, transaction names, fingerprints, and log templates.
- Review dropped/accepted volume, quota, and alert noise after each rollout.

## Privacy and security

- Minimize at source, scrub in SDK and server-side settings, then test raw event JSON.
- Keep auth tokens in scoped CI secret stores; a DSN is not an auth token.
- Use synthetic PolicyLab data in training.
- Mask/block replay content and govern feedback/attachments.
- Apply least privilege, SSO/MFA, team ownership, integration review, and token rotation.

## Operational workflow

- Triage with project/environment/time scope first.
- Record evidence, hypothesis, owner, priority, and next action.
- Alert only when a named responder can take an action.
- Review release trends and regressions after deploy.
- Treat Seer/MCP output as a reviewed hypothesis.

## Definition of done

An integration is ready when:

- [ ] synthetic error is searchable in the intended project/environment;
- [ ] stack trace is symbolicated/deobfuscated;
- [ ] release and deploy match the shipped artifact;
- [ ] distributed trace crosses expected services;
- [ ] logs/replay/feedback correlate where enabled;
- [ ] PII canaries are absent or scrubbed;
- [ ] sampling and volume are understood;
- [ ] alert reaches only the test destination;
- [ ] owner and runbook are linked;
- [ ] token revocation and rollback are documented.

## Sources and validation

- [Sentry: Issue Details](https://docs.sentry.io/product/issues/issue-details/)
- [Sentry: API Permissions and Scopes](https://docs.sentry.io/api/permissions/)
- [Sentry: Data Scrubbing](https://docs.sentry.io/security-legal-pii/scrubbing/)
- [Sentry: Troubleshooting Source Maps](https://docs.sentry.io/platforms/javascript/guides/angular/sourcemaps/troubleshooting_js/)
- [Sentry: Alerting best practices](https://docs.sentry.io/product/alerts/best-practices/)

Validation: official issue, security, release/source artifact, and alerting guidance checked 2026-06-29.
