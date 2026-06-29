# Source maps / source bundles / R8 mapping — demo runbook

End-state: open any Sentry Issue → see real file names, real line numbers, and the source code around the failing frame. Achieved differently per platform but with **one** auth token.

## The one secret you need

GitHub repo → Settings → Environments → `training` → "Add secret":

- **Name:** `SENTRY_AUTH_TOKEN`
- **Value:** the org-owned auth token from [devpowers.sentry.io/settings/auth-tokens](https://devpowers.sentry.io/settings/auth-tokens/), scope **`org:ci`** (Source Map Upload, Release Creation, Code Mappings)

No other secret needed. No env var goes into Coolify. The token lives only in GitHub Actions and only at build time.

Quick CLI alternative if you prefer:

```powershell
gh secret set SENTRY_AUTH_TOKEN --env training `
  --repo LucasMatuszewski/sentry-course-06-2026
```

## Per-platform status

| Platform | Artifact | Wired in CI? | What you need to do |
|---|---|---|---|
| **Angular** | `.js.map` source maps with debug IDs | ✅ Yes (release.yml) | Just set the secret, push to main |
| **Spring** | JVM source bundle (.zip of .java) → source context in stack | ✅ Yes (Sentry Gradle plugin in release.yml) | Just set the secret, push to main |
| **Android** | R8/ProGuard mapping.txt + Kotlin source bundle | ⚠ Plugin wired in `apps/android/app/build.gradle.kts`, but CI does only an XML sanity check. Run a release build locally with the token to demo. | See "Android demo (local)" below |

## What happens on the next push to main (Angular + Spring)

`.github/workflows/release.yml`:

1. **meta** job computes `angular@<sha>` and `api-spring@<sha>`.
2. **angular-image** job:
   - `ng build --configuration production --source-map` produces `.js` + `.js.map`.
   - `getsentry/action-release@v1` uploads source maps to project `javascript-angular`.
   - `.map` files are stripped from the deployable image (nginx returns 404 for `*.map` anyway).
3. **spring-image** job:
   - `./gradlew bootJar -Dsentry.release=...`. The `io.sentry.jvm.gradle:5.5.0` plugin **detects** `SENTRY_AUTH_TOKEN` env var, enables `includeSourceContext`, zips `src/main/java`, uploads to project `java-spring-boot`.
   - `getsentry/action-release@v1` records the release with commits.
4. **deploy** job pings Coolify webhook.

## Live demo flow (mid-course, 13:30–14:30 module)

**Step 0 — show ugly stack BEFORE.**
Open an Angular Issue in Sentry. Stack frames look like:
```
at xY (chunk-FIM33FFQ.js:12345:8)
at lZ (chunk-OFBVUT6D.js:4567:21)
```

**Step 1 — set token (3 clicks, 30 sec).**
```powershell
gh secret set SENTRY_AUTH_TOKEN --env training --repo LucasMatuszewski/sentry-course-06-2026
```
(paste the `sntrys_…` token when prompted)

**Step 2 — trigger release.**
Trivial commit + push, or:
```powershell
gh workflow run release.yml --ref main `
  --repo LucasMatuszewski/sentry-course-06-2026
```

**Step 3 — explain while it builds (~13 min on arm64).**
Use the slide 28 R8/ProGuard/dSYM glossary accordion. Show the `release.yml` file on the second monitor. Mention: same SHA used as release name, image tag, and what Coolify pulls.

**Step 4 — show clean stack AFTER.**
After the deploy job finishes, hit the deployed URL → click **Wymuś błąd 500** → refresh Sentry Issues. The new event now shows:
```
at QuoteController.createQuote (QuoteController.java:58)
at QuoteService.applyAgeFactor (QuoteService.java:42)
```
…with **source code around each frame** because the source bundle is attached to release `api-spring@<sha>`.

The Angular event likewise points to `app.ts` / `quote-api.service.ts` with TypeScript line numbers.

## Android demo (local — release build on the trainer's machine)

CI currently does not build an Android APK (the AI Studio import lacks a Gradle wrapper and `assembleRelease` requires signing keys we don't keep in CI). Do the mapping demo locally during the 16:15–16:40 module:

```powershell
cd apps\android
$env:SENTRY_AUTH_TOKEN="sntrys_..."

# One-time keystore setup if you haven't already:
$env:STORE_PASSWORD="android"
$env:KEY_PASSWORD="android"

# Release build with R8 obfuscation + automatic mapping upload:
./gradlew :app:assembleRelease
```

Build output should include:
```
> Task :app:sentryUploadProguardMappingsRelease
[sentry] Uploaded ProGuard mapping for android@1.0+1
```

Install the release APK on the Galaxy S24, trigger the same Sentry sandbox error you'd trigger in debug. The Sentry event will arrive with **real Kotlin class and method names** even though the APK is obfuscated. Without the mapping upload, the event would show classes like `a.b.c`.

Compare on screen: run a `debug` build first, then a `release` build, show that the `release` event is just as readable as the `debug` event thanks to the uploaded mapping.

## What "release" identifier links everything

| Platform | Release name (auto) |
|---|---|
| Angular | `angular@<short-sha>` (set in `release.yml` `meta` job) |
| Spring | `api-spring@<short-sha>` (set the same way) |
| Android | `com.aistudio.sentrydemo.kblyzw@1.0+1` (SDK default — `applicationId@versionName+versionCode`) |

**Critical rule:** the value embedded in the SDK at runtime *must match* the value used during the artifact upload. Otherwise Sentry can't join events to the right source bundle and you see "Unknown" frames. In this repo both come from the same source: `release.yml` exports `SENTRY_RELEASE` once and passes it both to the artifact-upload step and into the Docker build arg.

## Cleanup notes

- The `org:ci` token never expires by default. Revoke it after the workshop in [Sentry → Auth Tokens](https://devpowers.sentry.io/settings/auth-tokens/).
- Source bundles in Sentry don't expire on a small org; they don't cost anything to keep.
- If you delete a Sentry release, its source bundle is deleted with it — stack traces from that release lose their source context.

## Common failures

| Symptom | Likely cause |
|---|---|
| Spring stack shows `at QuoteController.createQuote(...)` but no source code panel | `SENTRY_AUTH_TOKEN` was unset in CI, `includeSourceContext` was false, no bundle uploaded. Set the secret + repush. |
| Angular stack still minified | Build ran but `getsentry/action-release` step was skipped because token absent (you'll see a `::warning::` in the workflow log). |
| Android stack shows `a.b.c.d` instead of class names | `isMinifyEnabled=true` but the mapping wasn't uploaded (no token at build time) or upload failed (`./gradlew :app:assembleRelease --info` shows errors). |
| Source code shows the wrong lines | The `SENTRY_RELEASE` used at upload time doesn't match the runtime `release` reported by the SDK. Check both. |

## References

- [Sentry Java source context](https://docs.sentry.io/platforms/java/source-context/)
- [Sentry Android Gradle plugin](https://docs.sentry.io/platforms/android/configuration/gradle/)
- [Sentry Angular source maps](https://docs.sentry.io/platforms/javascript/guides/angular/sourcemaps/)
- [Sentry API permissions / `org:ci`](https://docs.sentry.io/api/permissions/)

Validation date: 2026-06-29.
