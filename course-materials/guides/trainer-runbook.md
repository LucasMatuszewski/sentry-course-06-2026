# Trainer runbook

Audience: five senior enterprise Java, Angular, and mobile developers. Date: 2026-06-29. All examples use fictional PolicyLab insurance data.

## Before the day

- [ ] Confirm five named attendee accounts can sign in with least-privilege workshop roles.
- [ ] Verify `policylab-web`, `policylab-api`, `policylab-android`, and optional iOS projects.
- [ ] Seed staging events for every exercise; record stable issue/event/trace links in trainer-only notes.
- [ ] Ensure no client branding, tenant names, customer data, production URLs, or secrets appear.
- [ ] Test source-mapped Angular, Java cause chain, distributed trace, Android deobfuscation, feedback, and privacy canary.
- [ ] Route workshop alerts only to a test destination.
- [ ] Confirm Seer/MCP entitlement; keep them optional.
- [ ] Test Galaxy S24 USB and wireless ADB, Android Studio mirroring, scrcpy fallback, Teams window sharing, cable, driver, charger, Wi-Fi, and Do Not Disturb.
- [ ] Keep screenshots/exported JSON as zero-install fallbacks.
- [ ] Recheck official source links and note any changed UI labels.

## Suggested one-day flow

| Time | Module |
|---|---|
| 09:00 | Access, safety, manual UI orientation |
| 09:30 | Fundamentals; projects/environments |
| 10:15 | Exercise 1, break |
| 10:45 | Triage/grouping and hidden quote bug |
| 11:45 | Privacy and safe context |
| 12:30 | Lunch |
| 13:15 | Releases, source artifacts, CI |
| 14:00 | Tracing, logs, replay, sampling |
| 15:00 | Break |
| 15:15 | Alerts, trends, integrations |
| 16:00 | Android/mobile; Seer/MCP supplement |
| 16:40 | Final audit and review |
| 17:00 | Close and revoke temporary access |

Adjust timeboxes to the published agenda if it differs; do not edit agenda files from this workstream.

## Exercise delivery

For each exercise:

1. state the operational decision, timebox, and success criteria;
2. provide the live/manual path and zero-install artifact path;
3. let pairs investigate before hints;
4. ask for evidence, not a guessed root cause;
5. reveal the collapsed solution only after debrief;
6. reset any issue status, alert, or filter needed by the next group.

Pair attendees and rotate driver/navigator. With five people, trainer or observer joins the third pair.

## UI-change protocol

Do not improvise a click path. Say:

> This label may have changed. Use global navigation/search for the concept and verify scope from the page header.

Search for **Issues**, **Traces**, **Logs**, **Replays**, **Releases**, **Alerts**, or the setting name. Use prepared direct links only after confirming organization/project/environment.

## Demo safety

- Never show a token or CI variable value.
- Share one application window, not the full desktop.
- Keep phone notifications hidden.
- Do not ask attendees to install packages on managed machines; use zero-install paths.
- Do not send test alerts to live channels.
- Do not enable extra telemetry to rescue a demo.
- If data is questionable, stop screen sharing and switch to sanitized artifacts.

## Recovery matrix

| Failure | Recovery |
|---|---|
| Sentry login/role | Pair with trainer read-only view or sanitized screenshots |
| Seed event missing | Use saved event JSON/screenshot and query worksheet |
| UI moved | Global search, official docs, direct verified link |
| Source maps absent | Use prepared mapped/unmapped comparison |
| Trace broken | Use prepared trace waterfall and header diagram |
| Galaxy USB blocked | Wireless pairing, then trainer mirror |
| Wi-Fi blocks ADB | USB, Android Studio mirror, or prepared recording |
| Teams sharing fails | Local projector/window or screenshots |
| Seer/MCP unavailable | Continue manual workflow; no learning objective depends on AI |

## Close

- [ ] Delete or disable workshop alerts.
- [ ] Revoke temporary Sentry/MCP/CI credentials and attendee access as planned.
- [ ] Revoke Galaxy S24 debugging authorizations and disable wireless debugging.
- [ ] Remove synthetic feedback/events if policy requires.
- [ ] Record source/UI drift and exercise timing for the next delivery.
- [ ] Collect only non-sensitive feedback.

## Sources and validation

- [Sentry: Issue Details](https://docs.sentry.io/product/issues/issue-details/)
- [Sentry: Seer](https://docs.sentry.io/product/ai-in-sentry/seer/)
- [Android Developers: Run apps on a hardware device](https://developer.android.com/studio/run/device)
- [Microsoft Support: Share content in Teams meetings](https://support.microsoft.com/en-us/office/share-content-in-microsoft-teams-meetings-fcc2bf59-aecd-4481-8f99-ce55dd836ce8)

Validation: runbook dependencies and official references checked 2026-06-29; live tenant access, roles, seed IDs, and physical-device policy must be rechecked by the trainer before 09:00 on 2026-06-29.
