# Triage and grouping

## A five-minute triage

1. **Scope:** set `environment:production`, the relevant project, and a meaningful time range.
2. **Impact:** compare events, affected users, frequency, and trend. A noisy internal retry may matter less than one failed quote per customer.
3. **Change:** check first/last seen, regression state, release, deploy, and suspect commit if configured.
4. **Evidence:** inspect in-app stack frames, exception chain, breadcrumbs, tags, request, replay, and trace.
5. **Action:** assign the owning team, choose priority, add a short evidence comment, and set the correct status.

Useful issue searches include:

```text
environment:production is:unresolved
release:policylab-web@2026.06.30*
transaction:"POST /api/quotes"
```

Use search autocomplete as the authority for fields supported by the current dataset.

## Status is a decision

- **Resolve** when the problem is fixed or intentionally closed. Prefer “resolve in next/current release” when release tracking is reliable.
- **Archive/ignore** only with a reason and expiry/review condition.
- **Assign** to a team that can change the failing component, not merely the reporter.
- A regression means a resolved issue reappeared according to Sentry's regression rules; confirm release and environment before escalating.

## Grouping

Sentry primarily uses stack-trace and exception information to group error events. Before customizing:

1. compare events inside the issue;
2. inspect grouping information and hashes;
3. check whether wrapper exceptions or unstable messages hide the real failure;
4. prefer improving stack traces and source artifacts.

Use an SDK fingerprint only for a deliberate domain boundary:

```ts
Sentry.withScope(scope => {
  scope.setFingerprint(["quote-rule", ruleCode]);
  Sentry.captureException(error);
});
```

`ruleCode` must be a small governed set, never a quote ID or free text. High-cardinality fingerprints create issue explosions. Merging issues is easy to do and hard to undo operationally; document why.

## PolicyLab hidden-bug hypothesis

If many quote failures appear as `RuntimeException: quote failed`, compare causes and breadcrumbs. A backend `NullPointerException` and a frontend `TypeError` should not be forced together simply because the visible message matches.

## UI note

Validated page names include **Issues** and **Issue Details**. Actions such as assign, resolve, archive, and grouping details can move between header and overflow menus. Use the issue action search/overflow and the official Issue Details guide if labels differ.

## Sources and validation

- [Sentry: Issue Details](https://docs.sentry.io/product/issues/issue-details/)
- [Sentry: List an Issue's Grouping Hashes](https://docs.sentry.io/api/events/list-an-issues-hashes/)
- [Sentry: Fingerprinting for Angular](https://docs.sentry.io/platforms/javascript/guides/angular/usage/sdk-fingerprinting/)

Validation: official issue-detail, grouping, and fingerprint guidance checked 2026-06-29.
