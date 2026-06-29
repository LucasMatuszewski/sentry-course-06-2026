# Alerts, trends, and integrations

## Alert from a decision

An alert needs:

- a condition representing user or service harm;
- project/environment scope;
- an evaluation window and threshold;
- an owner and destination;
- deduplication/cooldown behavior;
- a runbook link;
- an explicit recovery or review path.

Start with outcomes, not every exception. PolicyLab examples:

- a new high-priority production quote issue;
- failed quote spans exceed an agreed rate for ten minutes;
- p95 quote latency crosses the service objective after a release.

Keep workshop alerts pointed at a designated test destination. Do not notify real incident channels.

## Issue versus metric-style detection

- Use issue-based detection for new, regressed, or escalating grouped problems.
- Use metric/threshold detection for rates, counts, latency, and sustained behavior across events or spans.
- Use scheduled queries or dashboards for trends that do not require interruption.

Product terminology and alert creation flows change. In the current UI, begin from **Alerts** or search settings/navigation for **Alerts** or **Monitors**, then verify the dataset, environment, interval, and action preview. Do not rely on an unverified click path.

## Trend review

For each release:

1. compare pre/post release windows;
2. filter to `production`;
3. inspect new/regressed issues;
4. compare error rate, affected users, and p95/p99 latency;
5. segment by project, transaction, release, device/OS, or governed business tag;
6. annotate the decision: proceed, investigate, or rollback.

Counts on sampled datasets may be estimated. Compare like-for-like sample policies and prefer rates over raw counts when traffic changes.

## Integration checklist

- Use organization-owned integration credentials, not a trainer's personal token.
- Apply least privilege and restrict project/repository scope.
- Map Sentry teams to delivery ownership.
- Test a synthetic notification and confirm links do not disclose sensitive event data.
- Define who may create, edit, mute, and delete alerts.
- Review unused integrations and webhook destinations.

For Jira/Bitbucket/Teams or another enterprise tool, verify availability and current setup in the organization's **Integrations** catalog. A product integration being documented does not mean it is enabled for the workshop tenant.

## Sources and validation

- [Sentry: Alerts API](https://docs.sentry.io/api/monitors/fetch-alerts/)
- [Sentry: Explore timeseries query behavior](https://docs.sentry.io/api/explore/query-explore-events-in-timeseries-format/)
- [Sentry: Organization Integrations](https://docs.sentry.io/organization/integrations/)
- [Sentry: Alerting best practices](https://docs.sentry.io/product/alerts/best-practices/)

Validation: official alert, Explore, and integration guidance checked 2026-06-29. UI routes are intentionally qualified.
