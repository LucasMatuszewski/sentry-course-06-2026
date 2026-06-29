# Sentry fundamentals

**Workshop outcome:** turn telemetry into an evidence-based debugging decision, not another inbox.

## Mental model

- An **event** is one captured occurrence: an exception, log, span, feedback item, or replay segment.
- An **issue** groups related error events so a team can triage one problem rather than thousands of copies.
- A **project** is an ingestion and configuration boundary, normally aligned with one deployable application or service.
- A **release** identifies code shipped together. An **environment** identifies where it ran.
- A **trace** links work across services by trace/span identifiers.
- Tags are indexed dimensions for searching and trends; contexts are richer diagnostic objects.

PolicyLab examples use fictitious quote IDs such as `Q-1042`, synthetic users such as `user-17`, and no real policyholder data.

## Manual-first investigation loop

1. Set project, environment, and time range before interpreting counts.
2. Open an issue and check impact: users, events, first/last seen, and release.
3. Inspect the exception and in-app frames, then breadcrumbs, tags, request, and trace.
4. Compare the first, latest, and recommended events; one event is not a trend.
5. Form a hypothesis and record the evidence.
6. Assign an owner and status. Resolve in a release when the fix has a known release.
7. Verify after deployment using release/environment filters and recurrence.

## Good telemetry test

For a failed PolicyLab quote submission, an engineer should be able to answer:

- What operation failed?
- Which deployable component and release ran it?
- Was it production, staging, or development?
- How many synthetic users were affected?
- What happened immediately before it?
- Which downstream call was slow or failed?
- Can we reproduce without exposing customer data?

If not, improve instrumentation rather than adding arbitrary payloads.

## UI note

Labels and navigation vary by Sentry plan and rollout. Paths in this course describe the UI validated on the date below. If a named page is absent, use Sentry's global navigation/search for the concept (for example, **Issues**, **Traces**, or **Releases**) and confirm the current official page.

## Sources and validation

- [Sentry: Issue Details](https://docs.sentry.io/product/issues/issue-details/)
- [Sentry: Events and Issues API overview](https://docs.sentry.io/api/events/)
- [Sentry: Retrieve a Trace](https://docs.sentry.io/api/discover/retrieve-a-trace/)

Validation: official sources and terminology checked 2026-06-29.
