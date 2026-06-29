# Tokens, secrets, and deployment — exact configuration

This guide is the single source of truth for "what token goes where" during the workshop and for the live Coolify deploy demo. Validated 2026-06-29.

## TL;DR

| What | Where it lives | Scope | Who reads it |
|---|---|---|---|
| **Project DSNs** (4) | runtime env vars per app | public | apps at startup |
| **Sentry org auth token** | GitHub repo secret `SENTRY_AUTH_TOKEN` | `org:ci` | release workflow only |
| **GitHub `GITHUB_TOKEN`** | injected by Actions | `packages:write`, `contents:read` | the workflow itself |
| **Coolify webhook URL** | GitHub repo secret `COOLIFY_WEBHOOK_URL` | per-application | release workflow's deploy step |
| **Public deploy URL** | GitHub repo variable `PUBLIC_URL` | n/a (not secret) | release workflow's health probe |

Never put `SENTRY_AUTH_TOKEN` in `compose.yaml`, in a Docker image, in `.env` files committed to git, or in Coolify runtime env. It is only needed at *build* time to upload source artifacts.

## 1. Sentry tokens — user vs. org, when to use which

There are two ways to authenticate against Sentry programmatically.

### Organization auth token (use this for CI/CD)

- Created in Sentry → **Settings → Auth Tokens** at the *organization* level: <https://devpowers.sentry.io/settings/auth-tokens/>
- Bound to the organization, not a person. Survives staff turnover.
- Scope `org:ci` covers everything a release pipeline needs: create releases, set commits, upload source maps, upload mapping/dSYM, upload source bundles. Recommended baseline.
- This is what we set as `SENTRY_AUTH_TOKEN` in the GitHub `training` environment.
- Reference: <https://docs.sentry.io/api/permissions/>

### User auth token (use this only at a developer's desk)

- Created in Sentry → **Account → User Auth Tokens**: <https://devpowers.sentry.io/settings/account/api/auth-tokens/>
- Bound to your user; carries *your* permissions.
- Required by the new `sentry` developer/agent CLI (`cli.sentry.dev`) when you run `sentry auth login`.
- Do not paste this into a workflow, a build image, or Coolify. If a teammate uses it after you leave, your audit trail is wrong.

### Decision matrix

| Action | Token type | Lives where |
|---|---|---|
| `sentry-cli releases new …` in GitHub Actions | Org auth token, `org:ci` | `secrets.SENTRY_AUTH_TOKEN` on the `training` environment |
| `sentry-cli sourcemaps upload …` in GitHub Actions | same | same |
| Sentry Gradle plugin uploading source bundle | same | same env var, read by the plugin |
| `sentry whoami`, `sentry issue list …` at your desk | User token via `sentry auth login` | `~/.config/sentry/credentials` on your laptop |
| Running the apps locally or in Coolify | **none** | apps only need the public DSN |

## 2. Set up the GitHub secrets — step by step

You only need one environment (`training`) and one secret/variable trio.

```powershell
# 1. Create the org token in Sentry UI:
#    https://devpowers.sentry.io/settings/auth-tokens/
#    Name: "GitHub Actions — sentry-course-06-2026"
#    Scopes: org:ci   (alerts:read can be added if you want alert validation)
#    Copy the token; it is shown ONCE.

# 2. Create the training environment on the repo:
gh api -X PUT repos/LucasMatuszewski/sentry-course-06-2026/environments/training

# 3. Add the Sentry auth token as an environment-scoped secret:
gh secret set SENTRY_AUTH_TOKEN --env training
# (paste the sntrys_... token when prompted)

# 4. (Once Coolify is set up) Add the Coolify webhook URL and public URL:
gh secret set COOLIFY_WEBHOOK_URL --env training
gh variable set PUBLIC_URL --env training --body "https://policylab.your-coolify-domain"
```

Why environment-scoped instead of repo-scoped:
- The `training` environment can have a manual approval gate enabled later.
- Fork pull requests cannot read environment secrets, so the CI workflow stays safe even when external contributors open PRs.

## 3. Runtime env vars — what each app reads

### Angular (`apps/angular`)
The DSN is currently hard-coded in `src/main.ts` as documentation for the workshop. In a real deployment switch to a runtime `assets/config.json` fetched on bootstrap, or generate the bundle per-environment. For today's training that hard-coded value is fine because Angular bundles cannot read process env at runtime anyway.

| Variable | Value | Notes |
|---|---|---|
| `SENTRY_RELEASE` | `angular@<sha>` (build arg) | passed by the Dockerfile, used by `sourcemaps upload` |
| `SENTRY_AUTH_TOKEN` | org token (build arg) | only inside the build image, never the runtime image |

### Spring (`apps/api-spring`)
Reads everything from env at startup (see `src/main/resources/application.properties`).

| Variable | Value | Notes |
|---|---|---|
| `SENTRY_DSN` | `https://bf24f04f…/4511644672983120` | public, set in Coolify |
| `SENTRY_ENVIRONMENT` | `training` | matches GitHub env |
| `SENTRY_RELEASE` | `api-spring@<sha>` | passed via JVM arg by the workflow |
| `SENTRY_TRACES_SAMPLE_RATE` | `1.0` for the demo | drop to ~0.2 in real prod |
| `DEMO_SCENARIOS_ENABLED` | `true` for the demo | `false` in real prod |
| `PARTICIPANT_ALIAS` | per-participant string | safe tag, not PII |

### Android (`apps/android`)
DSN baked into `AndroidManifest.xml` and the manual init in `SentryDemoApplication.kt`. Release identifier is computed from `versionName+versionCode`. No build secrets required for the on-device demo; an upload of R8 mapping in CI would need the same `SENTRY_AUTH_TOKEN`.

## 4. Coolify deployment — exact wiring

### What we deploy

The root `compose.yaml` runs two services on an internal Docker network:

- `web` — nginx serving the Angular bundle and reverse-proxying `/api/` to `api:8080`.
- `api` — Spring Boot 4 bootJar listening on `8080` (private).

Both images come from GHCR:
- `ghcr.io/lucasmatuszewski/sentry-course-06-2026-web:<sha>`
- `ghcr.io/lucasmatuszewski/sentry-course-06-2026-api:<sha>`

These are built and pushed by `.github/workflows/release.yml` after every push to `main`. Coolify pulls them — it does *not* build from source. This saves your VPS CPU/RAM and keeps the build/secret surface on GitHub's runners.

### One-time Coolify setup (UI clicks, ~10 minutes)

1. In Coolify, open the **DevPowers** project (uuid `fwow0wc8k4k0wo00kkk40sww`).
2. **Add new resource → Docker Compose**. Paste the contents of `compose.yaml` from this repo. Point it at the `main` branch of `LucasMatuszewski/sentry-course-06-2026`.
3. **Image source**: GHCR. If the repo packages are private, add a GHCR pull token in Coolify's registries section. Public is simplest for a training run.
4. **Environment variables** (Coolify UI → Environment):
   ```
   SPRING_SENTRY_DSN=https://bf24f04f…/4511644672983120
   SENTRY_ENVIRONMENT=training
   SENTRY_TRACES_SAMPLE_RATE=1.0
   DEMO_SCENARIOS_ENABLED=true
   PARTICIPANT_ALIAS=trainer
   IMAGE_TAG=main
   ```
5. **Domain**: assign an FQDN (e.g. `policylab.your-domain`). Coolify provisions Let's Encrypt automatically.
6. **Health check path**: `/healthz` (proxied by nginx).
7. **Save → Deploy**. First deploy pulls the images and brings the stack up.
8. **Webhook**: open the deployment's Settings → Webhooks, copy the redeploy URL, paste it into the GitHub secret `COOLIFY_WEBHOOK_URL`.
9. **Public URL**: set the GitHub variable `PUBLIC_URL` to the same FQDN so the workflow's health probe knows where to poll.

### What happens on each release after that

1. Developer merges to `main`.
2. GitHub Actions builds web + api images, uploads source maps + Spring source bundle to Sentry, pushes images to GHCR.
3. Workflow POSTs to `COOLIFY_WEBHOOK_URL`.
4. Coolify pulls the new images, restarts the stack with the new `IMAGE_TAG=<sha>`.
5. Workflow polls `PUBLIC_URL/healthz` for up to 5 minutes; logs a warning if not green.
6. Sentry receives the deploy marker (the action records it). Errors after this point show the new release.

## 5. Demo script for the course

```text
13:30   Open the Coolify dashboard on screen (or just the public URL).
13:32   Push a one-line change to main (typo fix in app.html title).
13:33   Switch to https://github.com/LucasMatuszewski/sentry-course-06-2026/actions
        Show the release workflow running:
          - meta job computes "angular@<sha>" / "api-spring@<sha>"
          - angular-image job: build → upload source maps → push image
          - spring-image job:  build → upload source bundle → push image
          - deploy job: webhook → poll /healthz
13:38   Switch to https://devpowers.sentry.io/releases/
        Show the two new releases, set-commits results, and the deploy marker.
13:40   Open the deployed app. Click "Pobierz wycenę" then "Wymuś błąd 500".
13:41   Switch to https://devpowers.sentry.io/issues/?environment=training
        Show the 500 issue with full Spring stack — including JVM source context.
        Show the trace propagating from the Angular browser to Spring.
        Show the symbolicated Angular error if the demo also throws there.
13:45   Mention what would change for real prod:
          - tracesSampleRate down to ~0.2
          - environment=production
          - send-default-pii audit
          - tokens rotated, Coolify webhook re-issued at retirement.
```

## 6. Cleanup checklist (after the workshop)

- [ ] Revoke the GitHub `SENTRY_AUTH_TOKEN` (Sentry → Settings → Auth Tokens).
- [ ] Revoke the Coolify deploy webhook.
- [ ] Set `IMAGE_TAG` to a frozen SHA in Coolify if you want to keep the demo URL up.
- [ ] Disable or remove participant-created Sentry alerts.
- [ ] Remove participant memberships from the DevPowers org.
- [ ] Add a rate limit on the public DSNs if you intend to leave them live.

## Sources and validation

- [Sentry API permissions / `org:ci`](https://docs.sentry.io/api/permissions/)
- [Sentry: getsentry/action-release](https://github.com/getsentry/action-release)
- [Sentry: Spring Boot integration](https://docs.sentry.io/platforms/java/guides/spring-boot/)
- [Sentry: Angular source maps](https://docs.sentry.io/platforms/javascript/guides/angular/sourcemaps/)
- [GitHub Actions environment secrets](https://docs.github.com/en/actions/security-for-github-actions/security-guides/using-secrets-in-github-actions#using-secrets-in-an-environment)
- [Coolify docs](https://coolify.io/docs/)

Validation: configuration files and live Sentry UI checked 2026-06-29.
