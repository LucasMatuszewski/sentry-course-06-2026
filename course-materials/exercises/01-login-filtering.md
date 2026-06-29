# Exercise 1: Login and filtering

**Timebox:** 15 minutes  
**Decision:** Which production issue deserves the first investigation?

## Zero-install path

Use a browser and the workshop Sentry account. If login or tenant access fails, use the trainer-provided sanitized Issues screenshot/export and write the filters you would apply.

## Task

1. Sign in without changing organization settings.
2. Open **Issues**. If the navigation label differs, use Sentry global search for “Issues”.
3. Select the PolicyLab organization and `policylab-web` plus `policylab-api`.
4. Set environment to `production` and time range to the trainer's stated window.
5. Apply `is:unresolved`.
6. Identify one issue to investigate first using affected users, frequency/trend, recency, and release—not title severity alone.
7. Copy a share-safe issue reference (short ID/link according to trainer policy) and write one sentence explaining the priority.

## Success criteria

- Correct organization/project/environment/time scope is visible.
- Query includes unresolved issues.
- Choice cites at least two impact/change signals.
- No event payload, user data, or public share link is exposed.

## Hints

1. Counts change with environment and time filters.
2. Check the page header/filter chips before trusting the result.
3. If a field is rejected, use search autocomplete.

<details>
<summary>Solution</summary>

Expected scope: PolicyLab web/API, `production`, stated time range, `is:unresolved`. A defensible answer prioritizes the issue affecting quote completion or multiple synthetic users and notes release/recency, rather than choosing the largest lifetime count. Exact issue ID depends on the trainer seed.

</details>
