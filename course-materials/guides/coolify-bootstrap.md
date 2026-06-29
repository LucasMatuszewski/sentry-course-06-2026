# Coolify bootstrap — 3-minute checklist

Read-only MCP cannot create resources, so this is the manual path. Open <https://cool.edukey.ai/> (or your own Coolify URL).

## 1. Make GHCR packages public (one-time)

Coolify needs to pull `ghcr.io/lucasmatuszewski/sentry-course-06-2026-{web,api}` without a token. The CI workflow already pushed both images; their visibility defaults to private.

- <https://github.com/users/LucasMatuszewski/packages/container/sentry-course-06-2026-api/settings>
- <https://github.com/users/LucasMatuszewski/packages/container/sentry-course-06-2026-web/settings>

Scroll to **Danger Zone → Change visibility → Public** on both pages.

Verify:

```powershell
docker pull ghcr.io/lucasmatuszewski/sentry-course-06-2026-api:main
docker pull ghcr.io/lucasmatuszewski/sentry-course-06-2026-web:main
```

## 2. Create the Coolify application

In Coolify UI:

1. Open project **DevPowers** (uuid `fwow0wc8k4k0wo00kkk40sww`) → environment **production**.
2. **+ Add new resource → Public Repository**.
3. Repository URL: `https://github.com/LucasMatuszewski/sentry-course-06-2026`
4. Branch: `main`
5. Build Pack: **Docker Compose**
6. Base directory: `/`
7. Docker Compose Location: `compose.yaml`
8. Name: `policylab-training`
9. **Save**. Coolify will read the compose file and detect two services (`web`, `api`).

## 3. Environment variables

In the new app → **Environment Variables**, paste this block:

```env
SPRING_SENTRY_DSN=https://bf24f04f280201f59caae185d83a97eb@o4511643784511488.ingest.de.sentry.io/4511644672983120
SENTRY_ENVIRONMENT=training
SENTRY_TRACES_SAMPLE_RATE=1.0
DEMO_SCENARIOS_ENABLED=true
PARTICIPANT_ALIAS=training-shared
IMAGE_TAG=main
```

All `is_runtime=true`, none are `is_buildtime` (Coolify will pull pre-built images, not build).

## 4. Domain

In the **web** service settings, **Domains** → set FQDN, e.g. `policylab.edukey.ai`. Coolify provisions Let's Encrypt automatically.

## 5. Health check

The compose already declares healthchecks. Confirm in Coolify the path is `/healthz` for `web` and `/actuator/health` for `api`.

## 6. Deploy

Click **Deploy**. First deploy pulls both images and brings the stack up. Watch logs in Coolify; expect ~30 seconds.

## 7. Webhook for future auto-deploys

In the new app → **Settings → Webhooks** → copy the **Deploy webhook URL**.

```powershell
# Save it as a GitHub training-environment secret:
gh secret set COOLIFY_WEBHOOK_URL --env training
# (paste the webhook URL when prompted)

# And the public URL so the workflow's health-probe knows where to poll:
gh variable set PUBLIC_URL --env training --body "https://policylab.edukey.ai"
```

## 8. Tell the slides about the URL

```powershell
# After you have the real FQDN, run this from the repo root to swap the placeholder:
$url = "https://policylab.edukey.ai"
(Get-Content course-materials/slides/index.html) `
  -replace 'https://policylab\.edukey\.ai', $url `
  | Set-Content course-materials/slides/index.html
git commit -am "docs(slides): point to live training URL $url"
git push
```

## 9. Verify end-to-end

1. Browse to the FQDN, click **Pobierz wycenę** → expect a JSON quote in the status line.
2. Click **Wymuś błąd 500** → expect "Błąd: 500".
3. Open <https://devpowers.sentry.io/issues/?project=4511644672983120&environment=training> → expect a fresh issue.
4. Open <https://devpowers.sentry.io/explore/traces/?project=4511646810570832&project=4511644672983120> → expect a trace crossing Angular and Spring.

## Rollback / cleanup after the course

- Coolify app → **Stop**, then **Delete** (with volumes).
- `gh secret remove SENTRY_AUTH_TOKEN --env training` (after revoking the token in Sentry).
- `gh secret remove COOLIFY_WEBHOOK_URL --env training`.
- Make GHCR packages private again if you don't want them lingering public.
