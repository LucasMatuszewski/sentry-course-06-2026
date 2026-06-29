# PolicyLab — Angular client

Główna ścieżka uczestnika kursu Sentry. Angular 20 + TypeScript + pnpm + `@sentry/angular`. Wysyła zapytania o wycenę do `/api/quotes` (Spring Boot — sąsiednia aplikacja).

## Kluczowe demonstracje

| Funkcja Sentry | Gdzie w kodzie |
|---|---|
| `Sentry.init` przed bootstrapem aplikacji | [`src/main.ts`](src/main.ts) |
| `ErrorHandler` + `TraceService` (Angular DI) | [`src/app/app.config.ts`](src/app/app.config.ts) |
| Browser tracing + propagation do `/api/*` | [`src/main.ts`](src/main.ts) |
| Session Replay z `maskAllText: true` + `blockAllMedia: true` | [`src/main.ts`](src/main.ts) |
| Structured logs (`Sentry.logger.info`) | [`src/app/app.ts`](src/app/app.ts) |
| Test error button + quote API client | [`src/app/app.ts`](src/app/app.ts), [`src/app/quote-api.service.ts`](src/app/quote-api.service.ts) |

## Source maps

Buildy produkcyjne generują source maps (`ng build --configuration production --source-map`). W CI pipeline (`.github/workflows/release.yml`) source maps są uploadowane do Sentry przez `getsentry/action-release`, a następnie `.map` files są usuwane z deployable bundle (nginx zwraca 404 dla `*.map` przez `nginx.conf`).

## Uruchom lokalnie

```powershell
# Pierwsze uruchomienie
pnpm install

# Dev server (http://localhost:4200)
pnpm exec ng serve

# Unit tests (4 specs)
pnpm exec ng test --watch=false --browsers=ChromeHeadless

# Build produkcyjny z source maps
pnpm exec ng build --configuration production --source-map
```

W trybie dev Angular forwarduje `/api/*` do backendu na porcie 8080 (skonfigurowane w `angular.json` lub serwerem proxy podczas demo). Backend uruchom z `apps/api-spring`.

## Konfiguracja Sentry

DSN jest aktualnie wpisany inline w `src/main.ts` (świadomie — to materiał edukacyjny pokazujący wzorzec, nie produkcyjna aplikacja gdzie DSN miałby być w `assets/config.json` fetched at bootstrap). DSN celuje w projekt `devpowers/javascript-angular`.

Konfiguracja:

| Opcja | Wartość treningowa | Co należy zmienić w prod |
|---|---|---|
| `environment` | `training` | `production`/`staging` |
| `release` | `angular@0.1.0` (kompilacja-build-arg w Docker) | unikalny SHA commitu |
| `sendDefaultPii` | `false` | `false` (utrzymać dla EU/regulowanych branż) |
| `tracesSampleRate` | `1.0` | `0.05`–`0.20` zależnie od ruchu |
| `replaysSessionSampleRate` | `0.1` | `0.01`–`0.05` |
| `replaysOnErrorSampleRate` | `1.0` | `1.0` (zachowaj — łapie sesje przy błędzie) |
| `enableLogs` | `true` | `true` |

## Docker

```powershell
docker build -t policylab-web `
  --build-arg SENTRY_RELEASE=angular@local `
  -f apps/angular/Dockerfile apps/angular
docker run --rm -p 8080:80 policylab-web
```

Multi-stage Dockerfile: `node:24.15-alpine` → `pnpm install` → `ng build --source-map` → uploaduje source maps do Sentry (jeśli `SENTRY_AUTH_TOKEN` build-arg ustawiony) → kopiuje statyki do `nginx:1.27-alpine` z `nginx.conf` który reverse-proxy'uje `/api/` do kontenera `api:8080`.

## Powiązane materiały kursu

- [Przewodnik Angular SDK](../../course-materials/guides/setup-angular.md)
- [Module 1.5: Tracing, logs, Replay](../../course-materials/guides/05-tracing-logs-replay-sampling.md)
- [Module 1.4: Releases + source maps](../../course-materials/guides/04-releases-source-artifacts-ci.md)
- [Module 1.7: Privacy & data scrubbing](../../course-materials/guides/07-privacy-data-scrubbing.md)
- [Ćwiczenie 03: Custom context bez PII](../../course-materials/exercises/03-safe-context-privacy.md)
- [Ćwiczenie 05: Distributed trace](../../course-materials/exercises/05-distributed-trace.md)
