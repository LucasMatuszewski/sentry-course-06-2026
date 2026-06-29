# Materiały szkoleniowe

Kurs jednodniowy **Sentry dla developerów** — prowadzony **dla Sages** przez Łukasza Matuszewskiego ([devpowers.com](https://devpowers.com)). Data: **2026-06-29**, 09:00–17:00 Europe/Warsaw.

Slajdy i narracja są po **polsku**. Słownictwo Sentry, nazwy SDK i polecenia CLI pozostają po angielsku — tak jak w oficjalnej dokumentacji. Polska UI w PolicyLab ilustruje, że nazwy biznesowe i komunikaty mogą być w dowolnym języku, oddzielnie od telemetrii.

## Ścieżka uczenia

1. **Przed kursem (trener)** — [Wyniki ankiety](survey-results.md) · [Program szkolenia (Polski)](Course-agenda-program-szkolenia-Sentry_2026.md) · [Skrócona agenda 1-dniowa](course-agenda-1-day.md)
2. **Onboarding** — [Pierwsze kroki w Sentry (z zrzutami ekranu)](sentry-onboarding-first-steps/sentry-onboarding.md) · [Issues tour — zwiedzanie panelu](sentry-onboarding-issues-tour/issues-tour.md)
3. **Słownik** — [glossary.md](glossary.md) (DSN, fingerprint, debug ID, dSYM, R8/ProGuard, source map, source context i inne pojęcia używane w narracji)
4. **Moduły** w katalogu [`guides/`](guides/) — pełne wyjaśnienia każdej sekcji programu:
   - [01 — Fundamentals + developer observability](guides/01-fundamentals.md)
   - [02 — Projects, environments, releases, SDK config](guides/02-projects-environments-config.md)
   - [03 — Triage, grouping, fingerprinting](guides/03-triage-grouping.md)
   - [04 — Releases, source maps, R8/ProGuard, dSYM, CI/CD](guides/04-releases-source-artifacts-ci.md)
   - [05 — Tracing, logs, Replay, sampling](guides/05-tracing-logs-replay-sampling.md)
   - [06 — Alerts, trends, Jira/Bitbucket workflow](guides/06-alerts-trends-integrations.md)
   - [07 — Privacy, data scrubbing, regulated env](guides/07-privacy-data-scrubbing.md)
   - [08 — Mobile/desktop observability](guides/08-mobile-desktop.md)
   - [09 — Seer, MCP, dev/agent CLI (krótka referencja)](guides/09-seer-mcp.md)
   - [Best practices (cross-platform)](guides/best-practices.md)
   - [Setup — Angular](guides/setup-angular.md) · [Setup — Spring Boot](guides/setup-spring.md) · [Setup — Android](guides/setup-android.md) · [Setup — iOS](guides/setup-ios.md)
   - [Tokeny + deploy (Coolify, GitHub Actions)](guides/setup-tokens-and-deploy.md)
   - [Coolify bootstrap UI — checklist 3 minut](guides/coolify-bootstrap.md)
   - [Trainer runbook](guides/trainer-runbook.md)
5. **Ćwiczenia praktyczne** w [`exercises/`](exercises/) — 8 zadań z timeboxami, podpowiedziami i kryteriami sukcesu
6. **CI/CD examples** w [`ci-cd/`](ci-cd/) — Bitbucket Pipelines i Jenkinsfile jako starting-points (annotated, wymagają ~30 min dostosowania per pipeline)
7. **Slajdy interaktywne** w [`slides/`](slides/) — pojedynczy offline HTML, 58 slajdów, Warsaw clock + reminder banners + trainer panels per moduł

## Kontrole referencyjne

- [Source validation register](source-validation.md) — z czego korzystaliśmy do walidacji treści
- [Manual trainer review gate](manual-review-checklist.md) — checklista zanim slajdy uznane za final
- [Tokens, secrets and Coolify deploy walkthrough](guides/setup-tokens-and-deploy.md)
- [Coolify bootstrap — 3-minute UI checklist](guides/coolify-bootstrap.md)

Oryginalna dwudniowa oferta dostępna jako [`Course-agenda-program-szkolenia-Sentry_2026.md`](Course-agenda-program-szkolenia-Sentry_2026.md) i PDF. Skrócona agenda 1-dniowa jest źródłem prawdy dla dostarczenia kursu.

## Ścieżki uczestnika

- **Zero install** — używaj wspólnych projektów Sentry w organizacji DevPowers i przygotowanych eventów.
- **Core local path** — uruchom lokalnie Angular i Spring Boot.
- **Mobile path** — Android Studio z urządzeniem albo wspólna aplikacja Google AI Studio.
- **iOS path** — przeczytaj referencyjny Swift sample; uruchom później na macOS z Xcode.

## Polityka źródeł

Wszystkie zalecenia techniczne mają linki do aktualnej oficjalnej dokumentacji i datę walidacji. Baseline validation date: **2026-06-29**. Żywa dokumentacja i aktualny stan oficjalnych repozytoriów ma pierwszeństwo nad cache'owanymi artykułami i pamięcią modeli.
