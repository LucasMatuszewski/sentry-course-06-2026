# Exercise 2: Triage the hidden quote bug

**Timebox:** 25 minutes  
**Decision:** What failed, who owns it, and what evidence supports that conclusion?

## Zero-install path

Use the trainer's sanitized Issue Details screenshots/event JSON if the live seed issue is unavailable. No repository checkout or IDE is required.

## Task

Open the seeded quote issue and:

1. confirm `production`, release, first/last seen, events, and affected users;
2. compare recommended, first, and latest events;
3. inspect exception chain and in-app frames;
4. inspect breadcrumbs, tags/contexts, request summary, and linked trace;
5. decide whether events belong in one group;
6. write a triage note with observed facts, hypothesis, owner (`web` or `api`), priority, and next check.

Do not resolve, merge, or archive unless the trainer explicitly authorizes the workshop mutation.

## Success criteria

- At least three observations are separated from hypotheses.
- Root cause points to the hidden failing layer, not merely the visible “quote failed” message.
- Ownership follows the changeable component.
- Grouping decision cites stack/cause evidence.
- Note contains no customer/secret data.

## Hints

1. Expand chained causes and inspect the first application-owned frame.
2. A frontend error may be a consequence of an API failure.
3. Compare trace span status and the last meaningful breadcrumb.

<details>
<summary>Solution</summary>

The expected hypothesis is that the quote UI surfaces a generic failure while the linked API span/event reveals a pricing-rule null/invalid configuration in the Spring service. Ownership belongs to the API/pricing team. Keep truly identical backend events grouped; do not fingerprint by quote ID. The UI symptom is supporting evidence, not the root cause.

</details>
