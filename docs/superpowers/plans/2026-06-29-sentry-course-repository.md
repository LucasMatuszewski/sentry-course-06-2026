# Sentry Enterprise Developer Workshop Repository Implementation Plan

> **Execution workflow:** use `superpowers:executing-plans`; delegate only with explicit user approval.

**Goal:** Deliver a course-ready public repository, practical PolicyLab demo system, exercises, current-source guides, and interactive HTML materials for the 2026-06-30 workshop.

**Architecture:** Angular and Spring Boot 4 form the primary hands-on path. The reviewed AI Studio Android app is the main mobile demo, official Android/iOS samples are references, and GitHub Actions plus Coolify provide the live release path.

## Fixed decisions

- English materials and terminology; Polish synthetic UI is allowed.
- Five senior/architect-level enterprise developers.
- Synthetic PolicyLab data only; no client name, branding, system, code, or data.
- Project-level Sentry write access, no participant organization administration.
- Never share or commit `SENTRY_AUTH_TOKEN`.
- GitHub is live; Bitbucket/Jenkins are documented equivalents.
- Android uses Galaxy S24 Ultra Device Mirroring, with scrcpy and AI Studio fallbacks.
- iOS is a source/reference path without local build claims.
- AI/Seer/MCP is supplementary.

## Ordered work

1. Create repository foundation, agent policy, indexes, survey report, one-day agenda, glossary, onboarding embeds, and initial commit.
2. Research current official documentation and record source-validation metadata.
3. Create module guides, setup paths, enterprise best practices, exercises, and trainer runbook.
4. Adapt the official Spring Boot 4 sample into a standalone, tested PolicyLab API.
5. Generate the Angular PolicyLab client with Sentry errors, logs, tracing, Replay, runtime configuration, and source maps.
6. Connect Angular → Spring traces and prepare buggy/fixed releases.
7. Import/review the supplied AI Studio Android app; add PolicyLab tracing and device-demo instructions.
8. Adapt the official Android sample while replacing only monorepo build dependencies.
9. Copy/document the smallest complete official iOS Swift reference and mark verification limits.
10. Add triage, context, privacy, release, artifact, trend, trace, alert, mobile, feedback, and audit exercises.
11. Add GitHub CI/release, Docker Compose/Coolify, and Bitbucket/Jenkins equivalents.
12. Complete Markdown review before final offline HTML slides with Europe/Warsaw reminders.
13. Run builds/tests, link checks, slide tests, container smoke tests, manual Sentry checks, and secret scans.
14. Commit in reviewable phases and rehearse the Teams delivery with fallbacks.

## Course schedule

- 09:00–09:20 welcome/access
- 09:20–10:10 fundamentals/triage
- 10:10–11:00 projects/configuration/privacy
- 11:00–11:15 break
- 11:15–12:00 Angular/Spring instrumentation
- 12:00–13:00 hidden-bug exercise
- 13:00–13:30 lunch
- 13:30 survey reminder and releases/source artifacts
- 14:30–15:30 tracing/logs/Replay
- 15:30–15:40 break
- 15:40–16:15 alerts/trends/integrations
- 16:15–16:40 Android
- 16:40–16:50 AI
- 16:50–17:00 checklist/Q&A

## Readiness

P0 requires the agenda/index, Spring and Angular builds, runnable PolicyLab, prepared Sentry scenarios, exercises, Android demo access, trainer runbook, CI/release example, and reviewed HTML presentation.

P1 includes executable Bitbucket/Jenkins automation, deeper official mobile samples, and extended optional modules. P1 must not delay P0.
