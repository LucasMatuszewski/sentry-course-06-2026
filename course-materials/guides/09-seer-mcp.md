# Seer and MCP: short, supervised use

Manual Sentry investigation is the primary workshop skill. Seer and MCP are optional accelerators after an engineer has inspected the issue, trace, logs, and relevant source context.

## Seer

Seer can assist with root-cause analysis and proposed fixes using Sentry telemetry and connected code context. Treat its output as a hypothesis:

1. write your own one-sentence hypothesis first;
2. ask Seer to explain the evidence and uncertainty;
3. compare cited frames, spans, releases, and code with the event;
4. test the proposed fix locally;
5. require normal review, tests, security checks, and deployment controls.

Do not accept generated code that broadens data collection, disables scrubbing, leaks tokens, or hides exceptions.

## Sentry MCP

MCP lets an approved AI client query Sentry through a defined tool interface. Before use:

- confirm the official server/client setup for the current environment;
- use least-privilege, short-lived organization-owned authorization;
- restrict organization/project scope;
- understand which event/code data enters the AI context;
- keep approval for writes or issue mutations;
- audit and revoke access after the workshop.

Good prompt:

> In the PolicyLab staging project, summarize evidence for issue PL-WEB-7. Separate observed facts from hypotheses. Do not include user, request-body, cookie, or authorization data.

Bad prompt:

> Dump every event and fix production automatically.

## Human verification record

Record:

- issue/event and environment;
- evidence inspected manually;
- AI suggestion;
- checks performed;
- accepted/rejected decision and reviewer.

## Two Sentry CLIs (do not confuse them)

There are now two distinct command-line tools both called `sentry`. Keep their roles clear when teaching:

| | **CI/CD CLI** (the historical one) | **Developer / Agent CLI** (new) |
|---|---|---|
| Binary | `sentry-cli` | `sentry` |
| Home | [docs.sentry.io/cli/](https://docs.sentry.io/cli/) | [cli.sentry.dev](https://cli.sentry.dev/getting-started/) |
| Built for | Release pipelines, source-map and dSYM/ProGuard upload, deploy markers | Interactive triage, dashboards, queries, ad-hoc actions from a terminal or AI agent |
| Default auth | Organization auth token scoped to `org:ci` (Source map upload, Release creation, Code mappings). User tokens are allowed but discouraged; with a user token you must also set the org slug and project id via config or env. | Stored device login (`sentry auth login`) for the operator's user. Optional `SENTRY_AUTH_TOKEN` env var, with `SENTRY_FORCE_ENV_TOKEN=1` to prefer it. |
| Surface area | Narrow and stable; safe to run unattended in CI. | Wide (issues, events, traces, releases, projects, alerts, dashboards, MCP, AI). More powerful, broader permissions, **less safe to run unattended**. |
| When to teach | Release/source-artifact module (CI examples in `course-materials/ci-cd/`). | Agenda kickoff and the AI/Seer/MCP module — show one or two reads (`sentry whoami`, `sentry project list`, `sentry issue list`). |

Rules of thumb for participants:

- Use **`sentry-cli`** (CI/CD) inside `.github/workflows/*.yml`, Bitbucket Pipelines, and Jenkins.
- Use **`sentry`** (dev/agent) on a developer machine to inspect what is happening right now. Do not bake it into a build pipeline.
- A token authorized for one tool is not automatically appropriate for the other. The CI token is intentionally narrow; the dev/agent CLI can act on the operator's full Sentry permissions.

## UI note

Seer features, entitlements, and labels vary by plan and rollout. Start from the issue's Seer panel or search Sentry documentation/navigation for **Seer**. For MCP, follow the current official setup page rather than copying workshop tokens or commands.

## Sources and validation

- [Sentry: Seer](https://docs.sentry.io/product/ai-in-sentry/seer/)
- [Sentry: Sentry MCP](https://docs.sentry.io/product/sentry-mcp/)
- [Sentry MCP official site](https://mcp.sentry.dev/)

Validation: official Seer and Sentry MCP entry points checked 2026-06-29. Availability remains tenant- and plan-dependent.
