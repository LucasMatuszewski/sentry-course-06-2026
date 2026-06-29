# Sentry Enterprise Developer Workshop

This public repository supports a one-day, hands-on Sentry workshop for experienced Java, Angular, Android/Kotlin, and Swift developers.

The examples use **PolicyLab**, a fictional insurance-domain system with synthetic data. They demonstrate error monitoring, logs, tracing, releases, source artifacts, privacy controls, issue triage, and alerting without referring to any client brand or production system.

## Repository map

| Path | Purpose | Workshop role |
|---|---|---|
| `apps/angular` | PolicyLab Angular client | Primary participant path |
| `apps/api-spring` | PolicyLab Spring Boot 4 API | Primary participant path |
| `apps/android` | Polish Android event laboratory | Trainer demo / optional path |
| `apps/android-sentry-reference` | Adapted official Sentry Android sample | Reference |
| `apps/ios-sentry-reference` | Official-style iOS/Swift sample | Conceptual reference |
| `course-materials` | Agenda, guides, exercises, glossary, and slides | Main learning path |

## Fast paths

- [Course materials](course-materials/README.md)
- [One-day agenda](course-materials/course-agenda-1-day.md)
- [Sentry glossary](course-materials/glossary.md)
- [Onboarding walkthrough](course-materials/sentry-onboarding-first-steps/sentry-onboarding.md)
- [Google AI Studio Android app](https://ai.studio/apps/f3f80ee8-2b62-41a3-84bb-79d5f97f8558) — optional no-install mobile path

## Prerequisites

Core exercises require a browser and access to the DevPowers training organization in Sentry. Local application work additionally uses Git, Node.js with npm/pnpm, Java 17+, and an IDE. Docker and Android Studio are optional. Xcode on macOS is required only to run the iOS reference.

Every timed exercise has a zero-install alternative based on prepared Sentry events.

The repository pins Node.js 24.15.0 LTS in `mise.toml`. With [Mise](https://mise.jdx.dev/):

```powershell
mise install
mise exec -- node --version
```

## Sentry project mapping

| Application | Sentry project |
|---|---|
| Angular | `javascript-angular` |
| Spring Boot | `java-spring-boot` |
| Primary Android app | `android` |
| iOS reference | `apple-ios` |

The course uses the `training` environment. Participant-created configuration should use a `[TRAINING participant-N]` prefix.

## Security

`SENTRY_AUTH_TOKEN` is a secret. Never commit or share it. Store CI tokens only in protected CI/deployment secret stores.

A DSN is a public ingestion endpoint rather than an administrative credential. Applications still read it from configuration for rotation and environment management. Public training DSNs should be rate-limited and rotated after the course if abused.

Only synthetic data belongs in this repository or the DevPowers Sentry organization.
