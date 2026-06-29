# Projects, environments, and configuration

## Design boundaries

Use one project per independently deployed application or service when ownership, platform, alerting, retention, or access differs. For PolicyLab:

| Project | Platform | Example environments |
|---|---|---|
| `policylab-web` | Angular | `development`, `staging`, `production` |
| `policylab-api` | Spring Boot | same controlled vocabulary |
| `policylab-android` | Android | same controlled vocabulary |
| `policylab-ios` | iOS | same controlled vocabulary |

Do not create a project per developer, branch, tenant, or environment. Those are better represented by environment, release, tags, or another governed dimension.

## Environment contract

Environment values are created when events arrive and are case-sensitive in practice. Agree the exact lower-case vocabulary before rollout. Never put customer or host identifiers in `environment`.

Suggested contract:

- `development`: local developer work; normally low-value and heavily sampled.
- `staging`: shared pre-production verification.
- `production`: customer-facing runtime.

Use a release such as `policylab-web@2026.06.30+3f4c2a1`, not a mutable label such as `latest`.

## Configuration hierarchy

Keep these concerns separate:

- **Build configuration:** SDK/plugin versions and source artifact upload.
- **Runtime configuration:** DSN, environment, release, sampling, enabled integrations.
- **Server-side project settings:** inbound filters, data scrubbing, grouping, alerts.
- **Organization settings:** members, teams, auth, integrations, audit/governance.

The DSN is a routing identifier and is normally safe to embed in clients; auth tokens are secrets and belong only in protected CI variables. Never ship `SENTRY_AUTH_TOKEN` in an app.

## Manual configuration review

For each project, record:

1. owning team and escalation channel;
2. platform and runtime;
3. environment and release conventions;
4. PII policy and server-side scrubbing;
5. error, trace, replay, and log sampling;
6. expected alert destinations;
7. source artifact/symbol upload responsibility.

Current UI example: open **Settings**, choose the project, then search settings for the feature name. If the route differs, use Settings search rather than guessing a nested menu.

## Sources and validation

- [Sentry: Create a New Project API](https://docs.sentry.io/api/projects/create-a-new-project/)
- [Sentry: List a Project's Environments](https://docs.sentry.io/api/environments/list-a-projects-environments/)
- [Sentry: Security and PII](https://docs.sentry.io/security-legal-pii/)
- [Sentry: API Permissions and Scopes](https://docs.sentry.io/api/permissions/)

Validation: official sources and project/environment behavior checked 2026-06-29.
