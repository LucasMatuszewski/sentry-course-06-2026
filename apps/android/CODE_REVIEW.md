# AI Studio Android app — initial review

**Snapshot commit:** `f374841`  
**Review date:** 2026-06-29  
**Status:** imported successfully; not yet approved as the course source of truth

## Strengths

- Polish, scenario-driven UI suitable for a live mobile demonstration
- Handled and unhandled errors
- Breadcrumbs and scoped event context
- User Feedback flow
- Manual transactions/spans
- OkHttp and Room examples
- Unit, Robolectric, screenshot, and instrumented-test structure
- No committed bearer token, private key, or populated `.env`

## Required before course use

### Sentry lifecycle and versions

- Upgrade the current Sentry dependency from 7.14.0 to the current reviewed 8.x release.
- Resolve duplicate initialization: the manifest contains a DSN while `SentryDemoApplication` also calls `SentryAndroid.init`.
- Keep one explicit initialization path and configure `io.sentry.auto-init=false` when manual initialization is used.
- Use current logging, profiling, Replay, and feedback APIs rather than treating captured messages as structured Sentry Logs.

### Configuration and CI

- Replace hard-coded DSNs, environment, and release values with build/runtime configuration.
- Keep `SENTRY_AUTH_TOKEN` only in CI; never expose it in the APK or repository.
- Add the Sentry Android Gradle plugin for release mapping/source-context upload.
- Enable R8/minification in the training release variant and verify readable Sentry stack traces.
- Add a reproducible Gradle wrapper.
- Make the training signing approach explicit without presenting a debug key as production signing.

### Privacy

- Remove the PESEL-like value from tags and event context; use a boolean validation result or synthetic category.
- Remove organization/company labels that could be mistaken for a real insurer; use PolicyLab.
- Default `sendDefaultPii` to false.
- Review feedback name/email fields and clearly request synthetic values.
- Review screenshots, view hierarchy, Replay masking, request bodies, database values, and attachments before enabling them.
- Ensure diagnostic Logcat output and 100% sampling are training/debug settings only.

### Course integration

- Add a PolicyLab `/api/quotes` request through the Sentry OkHttp integration.
- Verify Android → Spring trace propagation.
- Add searchable safe tags for device/demo configuration.
- Add instructions for filtering by device model, OS, app release, environment, and participant alias.
- Keep the Google AI Studio URL as the zero-install fallback.

## Comparison baseline

- [Official Sentry Android sample](https://github.com/getsentry/sentry-java/tree/main/sentry-samples/sentry-samples-android)
- [Current Sentry Android setup](https://docs.sentry.io/platforms/android/)
- [Android Gradle plugin](https://docs.sentry.io/platforms/android/configuration/gradle/)
- [Data collected by the Android SDK](https://docs.sentry.io/platforms/android/data-management/data-collected/)
