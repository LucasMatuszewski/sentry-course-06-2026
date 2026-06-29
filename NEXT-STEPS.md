# Operator notes — Sentry workshop repo

The workshop was delivered on 2026-06-29. This file is the post-workshop maintenance reference: what is wired, what manual steps remain when re-running the demo from scratch, and how to verify each leg of the pipeline.

There is no deadline associated with this document. Treat each step as something to do carefully, not quickly.

## Current wiring (verify each is true before changing anything)

- Repo: <https://github.com/LucasMatuszewski/sentry-course-06-2026>
- CI workflow: `.github/workflows/ci.yml` — PR validation, no secrets touched.
- Release workflow: `.github/workflows/release.yml` — on push to main (paths-ignore for docs), builds multi-arch images, uploads source maps + JVM source bundle to Sentry, pushes images to GHCR, pings Coolify deploy webhook with `Authorization: Bearer ${COOLIFY_API_TOKEN}`.
- Deployable images on GHCR: `ghcr.io/lucasmatuszewski/sentry-course-06-2026-api:main` and `…-web:main`. Public-visibility.
- Coolify app `cglwptpktix94oaboo23jlwv` runs the compose stack on the Hetzner Ampere/arm64 host.
- Sentry projects in DevPowers org: `android`, `apple-ios`, `java-spring-boot`, `javascript-angular`. All DSNs documented in `course-materials/sentry-onboarding-first-steps/sentry-onboarding.md` and `.env.example`.

## What needs to be set for the demo to be fully working

GitHub `training` environment secrets:

| Name | Purpose | Source |
|---|---|---|
| `SENTRY_AUTH_TOKEN` | Source map / source bundle upload during CI build | Sentry → Settings → Auth Tokens, scope `org:ci` |
| `COOLIFY_WEBHOOK_URL` | Where the release workflow POSTs to trigger redeploy | Coolify → app → Deploy Webhook (auth required) |
| `COOLIFY_API_TOKEN` | Bearer token for the webhook above | Coolify → Profile → Keys & Tokens |

GitHub `training` environment variable:

| Name | Purpose |
|---|---|
| `PUBLIC_URL` | Where the release workflow polls `/healthz` after deploying. Set to the web FQDN, e.g. `https://web-sentry.edukey.ai`. |

To set them via CLI:

```powershell
gh secret set SENTRY_AUTH_TOKEN  --env training --repo LucasMatuszewski/sentry-course-06-2026
gh secret set COOLIFY_WEBHOOK_URL --env training --repo LucasMatuszewski/sentry-course-06-2026
gh secret set COOLIFY_API_TOKEN   --env training --repo LucasMatuszewski/sentry-course-06-2026
gh variable set PUBLIC_URL        --env training --body "https://web-sentry.edukey.ai" --repo LucasMatuszewski/sentry-course-06-2026
```

## How to verify each leg of the pipeline

1. **GHCR image visibility.** `docker pull ghcr.io/lucasmatuszewski/sentry-course-06-2026-api:main` from a fresh shell must succeed without credentials. If it fails: GitHub → user-packages settings → set both packages public.
2. **CI/release.** Push a trivial commit that touches `apps/api-spring/` (so it bypasses paths-ignore). `gh run watch` it.
3. **Source artifact upload.** In the workflow's `spring-image` job, look for the `:sentryBundleSourcesJava` and `:sentryUploadSourceBundleJava` Gradle task lines. If they are absent, the Sentry Gradle plugin did not pick up the auth token.
4. **Coolify pull.** After CI finishes, Coolify webhook should fire. Check Coolify → app → Deployments. Most recent entry should match the new commit SHA.
5. **Spring runtime init.** Coolify → app → Logs → switch dropdown from "Deployment" to the `api` container. Look for `Sentry SDK X.Y.Z has started` and `DSN: https://...`. If absent: env vars (especially `SENTRY_DSN`) did not reach the container.
6. **End-to-end capture.** Hit `https://web-sentry.edukey.ai`, click **Wymuś błąd 500**. Within ~10 seconds, a new `DemoServerException` issue should appear in <https://devpowers.sentry.io/issues/?project=4511644672983120>.
7. **Trace linkage.** Same click should produce one Sentry trace spanning Angular (`POST /api/quotes`) and Spring (`http.server`). Both projects' issues should reference the same trace ID.

## Optional: pre-seed buggy + fixed releases (Exercise 04 trend data)

```powershell
./scripts/seed-releases.ps1
```

Creates two commits (buggy then fix), waits for both release workflows, hits the live stack to generate trend data on the `release trend after fix` exercise.

## Rehearsal helpers in the slides

The deck supports a `?clock=HH:MM` query parameter for testing reminder banners without waiting for real time:

- `course-materials/slides/index.html?clock=10:55#slide-15` — see the 11:00 break banner 5 minutes earlier
- `course-materials/slides/index.html?clock=13:28#slide-23` — see the survey banner 2 minutes earlier

## Where everything lives

| File | Why |
|---|---|
| `apps/angular/Dockerfile` + `nginx.conf` | Multi-stage build; nginx serves SPA + reverse-proxies `/api/` to Spring |
| `apps/api-spring/Dockerfile` | Multi-stage Temurin 17 build; `eclipse-temurin:17-jdk-noble` (Alpine variants lack arm64) |
| `compose.yaml` (root) | Two services pulling pre-built GHCR images; DSN hardcoded inline because it is public |
| `.env.example` | Documents the runtime env contract |
| `.github/workflows/release.yml` | Build + upload + push image + Coolify deploy + Sentry release |
| `.github/workflows/ci.yml` | PR validation, no secrets |
| `course-materials/guides/setup-tokens-and-deploy.md` | Token decision matrix and exact CLI commands |
| `course-materials/guides/coolify-bootstrap.md` | Coolify UI walkthrough for first-time setup |
| `scripts/seed-releases.ps1` | Buggy + fixed release seed for Exercise 04 |
