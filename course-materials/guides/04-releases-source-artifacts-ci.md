# Releases, source artifacts, and CI

## Release contract

Every deployed component sends the same immutable release identifier that CI used:

```text
policylab-web@2026.06.30+3f4c2a1
policylab-api@2026.06.30+3f4c2a1
```

A release answers “what code ran?” An environment answers “where did it run?” A deploy associates a release with an environment and time.

## Correct build order

1. Choose `SENTRY_RELEASE` once from an immutable version/commit.
2. Build production artifacts with that release embedded in SDK configuration.
3. Create/update the release in Sentry.
4. Associate commits.
5. Generate and upload source maps or native debug files from the exact binaries being deployed.
6. Finalize the release.
7. Deploy those unchanged artifacts.
8. Record the deploy.
9. Trigger a controlled synthetic error and verify release, environment, and symbolication.

Source artifacts must be uploaded before events that need processing. Uploading later may not repair previously processed stack traces.

## Web source maps

Prefer the current Sentry bundler plugin or wizard. Modern JavaScript tooling injects debug IDs and uploads artifact bundles. Produce source maps in the CI build, upload them from CI, then prevent public serving or delete them from the deployable package if policy requires.

Verification:

```powershell
sentry-cli sourcemaps explain <32-character-event-id>
```

Also check the event's raw JSON for `debug_meta`, verify every deployed chunk was processed, and use a production build rather than watch mode.

## JVM and native artifacts

- Java line numbers normally come from bytecode; upload mapping files when using R8/ProGuard.
- Android builds should use the Sentry Android Gradle plugin for mapping/native symbol/source context automation.
- Apple builds require matching dSYMs for the exact build UUIDs.
- Never rebuild after upload: the rebuilt binary may not match its debug files.

## CI security

Use a scoped organization integration token in the CI secret store. Current Sentry API guidance defines `org:ci` for CI workflows including releases, source maps, and code mappings. Do not echo tokens, pass them as command arguments visible to other processes, or write them into artifacts.

Examples in `course-materials/ci-cd/` are references only and must not be run unchanged.

## Sources and validation

- [Sentry: API Permissions and Scopes](https://docs.sentry.io/api/permissions/)
- [Sentry: Create a Release API](https://docs.sentry.io/api/releases/create-a-new-release-for-an-organization/)
- [Sentry: Troubleshooting JavaScript Source Maps](https://docs.sentry.io/platforms/javascript/guides/angular/sourcemaps/troubleshooting_js/)
- [Sentry: Android Gradle Plugin](https://docs.sentry.io/platforms/android/configuration/gradle/)
- [Sentry: iOS Debug Symbols](https://docs.sentry.io/platforms/apple/guides/ios/dsym/)

Validation: official release, source-map, symbol, and current token-scope guidance checked 2026-06-29.
