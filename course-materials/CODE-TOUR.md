# Code tour — what to show, when

Cheat sheet for the trainer. During each module open these files and read the highlighted ranges out loud. The audience are senior devs — they will trust the slide narrative only when they see the wired-up SDK code.

GitHub base URL: `https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/`

## 09:00–09:20 Preflight (slide 6)

Just open the repo root and the deck. Don't dive into code yet.

- [Root README](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/README.md) — show the audience the layout
- [NEXT-STEPS.md](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/NEXT-STEPS.md) — only if you want them to see what setup costs
- Course materials index: [`course-materials/README.md`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/course-materials/README.md)

## 09:20–10:10 Fundamentals + triage (slides 8–10)

UI work mostly. If asked "where is an issue actually captured in our code?":

- Angular global capture: [`apps/angular/src/app/app.config.ts#L21`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/angular/src/app/app.config.ts#L21) (`Sentry.createErrorHandler` registered as Angular `ErrorHandler`)
- Spring auto-capture: nothing to show — `sentry-spring-boot-4-starter` registers a `HandlerExceptionResolver` automatically. The deliberate 500 throw is in [`QuoteController.java#L58`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/api-spring/src/main/java/com/policylab/api/quote/QuoteController.java#L58) (`throw new DemoServerException();`).

## 10:10–11:00 Projects, SDK, privacy (slide 15)

Open these in tabs and walk through:

- **Angular SDK init** — [`apps/angular/src/main.ts#L7-L24`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/angular/src/main.ts#L7-L24)
  - `dsn`, `environment`, `release`, `sendDefaultPii: false`
  - `tracesSampleRate: 1.0` (with a "in prod: 0.05–0.2" call-out)
  - `replayIntegration({ maskAllText: true, blockAllMedia: true })`
  - `tracePropagationTargets: ['localhost', /^\/api/]` — this is *why* Spring joins the trace
  - `enableLogs: true`
- **Angular DI wiring** — [`apps/angular/src/app/app.config.ts#L15-L27`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/angular/src/app/app.config.ts#L15-L27)
- **Spring properties** — [`apps/api-spring/src/main/resources/application.properties`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/api-spring/src/main/resources/application.properties)
  - all configuration is env-driven; show the `send-default-pii=false` baseline and the `max-request-body-size=none`
- **Spring Gradle plugin** — [`apps/api-spring/build.gradle.kts#L1-L5`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/api-spring/build.gradle.kts#L1-L5) and [#L33-L38](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/api-spring/build.gradle.kts#L33-L38) (the `sentry { ... }` block, source-context gate)
- **Android manual init** — [`apps/android/app/src/main/java/com/example/SentryDemoApplication.kt#L11-L48`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/android/app/src/main/java/com/example/SentryDemoApplication.kt#L11-L48)
- **Android manifest DSN** — [`apps/android/app/src/main/AndroidManifest.xml#L18-L31`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/android/app/src/main/AndroidManifest.xml#L18-L31)

## 11:15–12:00 Onboarding live (slide 20)

- Same Angular files as above.
- **Quote API client** — [`apps/angular/src/app/quote-api.service.ts`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/angular/src/app/quote-api.service.ts) (33 lines, show the whole file)
- **Spring controller** — [`apps/api-spring/src/main/java/com/policylab/api/quote/QuoteController.java`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/api-spring/src/main/java/com/policylab/api/quote/QuoteController.java) — the `@PostMapping createQuote` (line 44 onwards) shows how the scenario header drives the demo and how `LOGGER.info(...)` ends up as a Sentry log

## 12:00–13:00 Triage exercise (slide 22)

Don't show code. Drive the Sentry Issues UI.

## 13:30–14:30 Releases + CI/CD (slides 26–33)

This is the most code-heavy module.

- **CI workflow** — [`.github/workflows/ci.yml`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/.github/workflows/ci.yml)
  - path-filter jobs (lines 16-39)
  - Angular unit tests + production build (lines 40-66)
  - Spring unit tests + bootJar (lines 68-100)
- **Release workflow** — [`.github/workflows/release.yml`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/.github/workflows/release.yml)
  - meta job (line 47)
  - Angular image build + source-map upload (lines 62-130) — point out the QEMU step and the `if: env.SENTRY_AUTH_TOKEN != ''` gate
  - Spring image build (lines 131-176)
  - Coolify deploy (lines 178-213)
- **Angular Dockerfile** — [`apps/angular/Dockerfile`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/angular/Dockerfile) — show the `SENTRY_AUTH_TOKEN` as build-arg (line 14) and the source-map upload + `find -delete` block (lines 25-37)
- **Spring Dockerfile** — [`apps/api-spring/Dockerfile`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/api-spring/Dockerfile)
- **Root compose** — [`compose.yaml`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/compose.yaml) — show that NO secrets are baked in; DSN is runtime env
- **Nginx /api proxy** — [`apps/angular/nginx.conf#L19-L29`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/angular/nginx.conf#L19-L29) — *this is why* Angular `/api/quotes` reaches Spring with the trace headers intact
- **Sentry Gradle plugin block** — [`apps/api-spring/build.gradle.kts#L33-L38`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/api-spring/build.gradle.kts#L33-L38)
- **Tokens guide** — [`course-materials/guides/setup-tokens-and-deploy.md`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/course-materials/guides/setup-tokens-and-deploy.md)

## 14:30–15:30 Tracing, logs, Replay (slide 39)

- **Browser tracing + propagation** — [`apps/angular/src/main.ts#L13`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/angular/src/main.ts#L13) and [#L20](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/angular/src/main.ts#L20)
- **Custom span on the backend** — [`apps/api-spring/src/main/java/com/policylab/api/telemetry/SentryQuoteTelemetry.java`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/api-spring/src/main/java/com/policylab/api/telemetry/SentryQuoteTelemetry.java) — the slow scenario child span
- **Structured log** — [`apps/api-spring/src/main/java/com/policylab/api/quote/QuoteController.java#L68-L73`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/api-spring/src/main/java/com/policylab/api/quote/QuoteController.java#L68-L73) — `LOGGER.info("Quote created: coverage={}, ...")` — emphasize how the placeholder syntax becomes structured fields in Sentry Logs
- **Replay masking config** — [`apps/angular/src/main.ts#L14-L17`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/angular/src/main.ts#L14-L17)

## 15:40–16:15 Alerts + Bitbucket (slide 45)

Drive the Sentry UI:

- DevPowers → Alerts → Create alert
- DevPowers → Settings → Integrations → show Bitbucket / Jira install pages (don't actually install during the workshop unless the long break has time)

## 16:15–16:40 Android (slide 46)

- **Manual init** — [`apps/android/app/src/main/java/com/example/SentryDemoApplication.kt`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/android/app/src/main/java/com/example/SentryDemoApplication.kt) — open the whole 50-line file; show `setTag` examples (lines 41-44) for survey-priority "custom fields"
- **AndroidManifest meta-data** — [`apps/android/app/src/main/AndroidManifest.xml#L18-L31`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/android/app/src/main/AndroidManifest.xml#L18-L31)
- Live demo: open Sentry Android Issues, filter by `device.model:"SM-S928B"`
- **CODE_REVIEW.md** for the AI Studio app — [`apps/android/CODE_REVIEW.md`](https://github.com/LucasMatuszewski/sentry-course-06-2026/blob/main/apps/android/CODE_REVIEW.md) — shows audit thinking, useful as background

## 16:40–17:00 Wrap-up + AI

No code; just the slides + repo URL.

---

## How to use this during delivery

- Keep this file open in a second browser tab alongside the slides.
- When a trainer-panel slide mentions a file, the link is here.
- If a participant asks "where is X in the code?", the answer is one click away.
- All links use `blob/main/...` — they are permalinks to the current state of `main`. After every commit on `main`, they still resolve. If you want to lock to a specific SHA for the workshop, swap `main` with the SHA.
