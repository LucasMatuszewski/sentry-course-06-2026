# Android upstream provenance and comparison guide

Validated: 2026-06-29

## Source

- Repository: <https://github.com/getsentry/sentry-java>
- Pinned commit: `d8b6ce11cabd05be9a3f03a1d20fe247956d091d`
- Commit date: 2026-06-26
- Official sample:
  `sentry-samples/sentry-samples-android/`
- Primary adapted source:
  `sentry-samples/sentry-samples-android/src/main/java/io/sentry/samples/android/MainActivity.kt`
- Official release used here: `io.sentry:sentry-android:8.46.0`, published
  2026-06-25
- License: MIT; the upstream license text is preserved in `LICENSE`.

## Why the complete official sample was not copied

The official app is a feature-dense SDK development and acceptance-test host.
Its Gradle build depends directly on these monorepo projects:

```text
sentry-android
sentry-android-fragment
sentry-android-sqlite
sentry-android-timber
sentry-compose
sentry-kotlin-extensions
sentry-okhttp
sentry-spotlight
sentry-android-distribution (debug only)
```

It also includes Compose, two Room generations, SQLDelight, CameraX, Retrofit,
OkHttp, Timber, LeakCanary, Lottie, NDK/CMake code, profiling, replay, app
distribution, custom Gradle tasks, and UI/performance test assets. Replacing
all of that with published coordinates would produce a large version-alignment
exercise rather than a small course reference.

## What this reference retains

| Upstream concept | Upstream location | Local location |
| --- | --- | --- |
| `captureMessage` | `MainActivity.kt`, “Send Message” action | `MainActivity.java`, “Capture message” |
| `captureException` | `MainActivity.kt`, “Capture Exception” action | `MainActivity.java`, “Capture handled exception” |
| breadcrumb + extras + exception | `MainActivity.kt`, “Breadcrumb” action | `MainActivity.java`, “Capture exception with context” |
| `setUser` | `MainActivity.kt`, “Set user” action | same context action |
| Sentry Logs | `MainActivity.kt`, `Sentry.logger().log(...)` | `MainActivity.java`, “Send Sentry Log (no PII demo)” |
| User Feedback | `MainActivity.kt`, `UserFeedbackScreen()` | `MainActivity.java`, “Open User Feedback (name/email hidden)” |
| manifest auto-init and tracing | `src/main/AndroidManifest.xml` | `app/src/main/AndroidManifest.xml` |
| uncaught Java crash | `MainActivity.kt`, “Crash from Java” action | `MainActivity.java`, “Trigger uncaught crash” |

The manual transaction is a compact teaching addition built only from the
public SDK API. Privacy defaults intentionally differ from the broad upstream
acceptance-test host: default PII, screenshots, and view-hierarchy attachments
are disabled here.

## How to compare or copy more upstream behavior

1. Check out the pinned commit:

   ```text
   git clone https://github.com/getsentry/sentry-java.git
   git -C sentry-java checkout d8b6ce11cabd05be9a3f03a1d20fe247956d091d
   ```

2. Compare these files first:

   ```text
   sentry-samples/sentry-samples-android/build.gradle.kts
   sentry-samples/sentry-samples-android/src/main/AndroidManifest.xml
   sentry-samples/sentry-samples-android/src/main/java/io/sentry/samples/android/MainActivity.kt
   ```

3. For each feature copied, replace `implementation(projects.<module>)` with
   the matching released `io.sentry:<artifact>:8.46.0` coordinate. Keep all
   Sentry artifacts on the same version.

4. Copy only the Activity/Fragment and the resources it directly references.
   Then add its AndroidX or third-party dependency explicitly. Do not copy the
   upstream root version catalog or build logic into this standalone project.

5. Re-run `:app:assembleDebug` after each feature. Features involving NDK,
   Room/SQLDelight code generation, Compose compiler alignment, or the
   unreleased app-distribution module should be treated as separate ports.

The current minimal app intentionally uses only `sentry-android`, Android
framework UI classes, and Java. This keeps the dependency boundary auditable.
