# Exercise 6: Design a useful alert

**Timebox:** 20 minutes  
**Decision:** When should PolicyLab interrupt an on-call engineer?

## Zero-install path

Design the alert on paper using the prepared trend chart and destination card. Live alert creation is optional and trainer-controlled.

## Task

Design one alert for production quote failure:

1. choose issue-based or metric/threshold detection;
2. specify project, environment, query/dataset, threshold, window, and cooldown;
3. name the owner and test destination;
4. write a two-step runbook;
5. define recovery and weekly review criteria;
6. explain how sampling affects interpretation.

If authorized, configure it from **Alerts** or current alert search result, preview scope, and send only a synthetic test. Do not target a live channel.

## Success criteria

- Condition maps to user/service harm.
- Scope excludes development/staging and health checks.
- Threshold/window avoids one-event noise.
- Destination has an owner and runbook.
- Recovery/review and sampling caveat are explicit.

## Hints

1. “Every exception” is rarely an actionable policy.
2. A failure rate often handles traffic variation better than a raw count.
3. Ask what action the recipient takes within five minutes.

<details>
<summary>Solution</summary>

Example: alert the PolicyLab API on-call test destination when the production quote transaction failure rate exceeds the agreed threshold for ten minutes, with cooldown and recovery after two healthy windows. Runbook: open filtered trace trend, then compare release and failing span. Exact threshold depends on seeded baseline; the important result is a justified, scoped, actionable detector.

</details>
