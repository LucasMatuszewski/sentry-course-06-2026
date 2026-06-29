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

## UI note

Seer features, entitlements, and labels vary by plan and rollout. Start from the issue's Seer panel or search Sentry documentation/navigation for **Seer**. For MCP, follow the current official setup page rather than copying workshop tokens or commands.

## Sources and validation

- [Sentry: Seer](https://docs.sentry.io/product/ai-in-sentry/seer/)
- [Sentry: Sentry MCP](https://docs.sentry.io/product/sentry-mcp/)
- [Sentry MCP official site](https://mcp.sentry.dev/)

Validation: official Seer and Sentry MCP entry points checked 2026-06-29. Availability remains tenant- and plan-dependent.
