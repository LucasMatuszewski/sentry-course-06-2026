# Pre-flight checklist for 2026-06-29 09:00

Estimated time: 15–25 minutes of clicking + 10 minutes of waiting. Do it as
soon as you wake up. Course starts at 09:00 Europe/Warsaw.

## Status when you fell asleep

- Repo pushed: `https://github.com/LucasMatuszewski/sentry-course-06-2026`
- CI: green ✓  Release workflow: green ✓
- Docker images pushed to GHCR: `*-api:main` and `*-web:main`
- Slides, guides, exercises: complete and committed
- Sentry projects: 4 (`android`, `apple-ios`, `java-spring-boot`,
  `javascript-angular`) all with correct DSNs everywhere

## What is left for you to do (you, not me — these need your hand)

### 1. Flip GHCR packages to public (2 min, 2 clicks)

Without this Coolify cannot pull the images.

- <https://github.com/users/LucasMatuszewski/packages/container/sentry-course-06-2026-api/settings> → Danger Zone → Change visibility → Public
- <https://github.com/users/LucasMatuszewski/packages/container/sentry-course-06-2026-web/settings> → Danger Zone → Change visibility → Public

### 2. Create the Coolify app (3 min)

Follow `course-materials/guides/coolify-bootstrap.md`. Copy-paste env vars are in step 3.

Output: the FQDN, e.g. `https://policylab.edukey.ai`, and a deploy webhook URL.

### 3. Wire the GitHub secrets (2 min)

```powershell
# Create org auth token at https://devpowers.sentry.io/settings/auth-tokens/
# Name: "GitHub Actions — sentry-course-06-2026", scope: org:ci
gh secret set SENTRY_AUTH_TOKEN --env training --repo LucasMatuszewski/sentry-course-06-2026
# (paste the sntrys_... token)

gh secret set COOLIFY_WEBHOOK_URL --env training --repo LucasMatuszewski/sentry-course-06-2026
# (paste the deploy webhook URL from step 2)

gh variable set PUBLIC_URL --env training --body "https://policylab.edukey.ai" --repo LucasMatuszewski/sentry-course-06-2026
```

### 4. Replace the placeholder URL in slides (30 sec)

```powershell
$url = "https://policylab.edukey.ai"   # whatever Coolify gave you
(Get-Content course-materials/slides/index.html) `
  -replace 'https://policylab\.edukey\.ai', $url `
  | Set-Content course-materials/slides/index.html
git commit -am "docs(slides): point to live training URL"
git push
```

### 5. Trigger a release to verify end-to-end (3 min)

```powershell
# The push from step 4 will trigger the Release workflow automatically.
gh run watch --repo LucasMatuszewski/sentry-course-06-2026
```

After workflow finishes:
- Open the FQDN; click **Pobierz wycenę** → expect JSON
- Click **Wymuś błąd 500** → expect "Błąd: 500"
- Check <https://devpowers.sentry.io/issues/?environment=training> — fresh issue with the new release tag

### 6. (Optional, ~10 min) Pre-seed buggy + fixed releases for Exercise 04

```powershell
./scripts/seed-releases.ps1
```

This creates two commits (buggy then fix), waits for both Release workflows, and pings the live stack to generate trend data. If you skip it, you can still run exercise 04 against historical events by adjusting the trainer runbook.

## If you only have 10 minutes

Skip step 6 and step 2 (Coolify deploy). The workshop still works fully — Angular and Spring run locally on each participant's laptop via `pnpm exec ng serve` + `./gradlew bootRun`. The deploy demo at 13:30 becomes an explanation rather than a live deploy.

The slides have a `?clock=HH:MM` override so you can rehearse banner timing without waiting:

- `course-materials/slides/index.html?clock=10:55#slide-15` — see the 11:00 break banner pop in 5 minutes
- `course-materials/slides/index.html?clock=13:28#slide-23` — see the survey banner pop in 2 minutes

## Files added during the night

| File | Why |
|---|---|
| `apps/angular/Dockerfile` + `nginx.conf` | Multi-stage build; nginx + /api proxy |
| `apps/api-spring/Dockerfile` | Multi-stage Temurin build, healthcheck |
| `compose.yaml` (root) | Two services, GHCR images, internal network |
| `.env.example` | Documents the contract |
| `.github/workflows/release.yml` (rewritten) | Builds both images, uploads source maps, deploys |
| `.github/workflows/ci.yml` (fixed) | PR validation, no secrets, Linux-runner-safe |
| `course-materials/guides/setup-tokens-and-deploy.md` | Token decision matrix |
| `course-materials/guides/coolify-bootstrap.md` | 3-minute Coolify checklist |
| `scripts/seed-releases.ps1` | Buggy + fixed release seed for Exercise 04 |

Good luck. The deck is ready, the code is ready, the CI is ready. The remaining 15 minutes are mostly clicks.
