# Release and source-artifact contract (non-live)

> Training reference only. Validate against the current SDK/CLI and organizational release process before adoption.

## Required variables

| Name | Example | Secret |
|---|---|---|
| `SENTRY_ORG` | `policylab-workshop` | No |
| `SENTRY_PROJECT` | `policylab-web` | No |
| `SENTRY_RELEASE` | `policylab-web@3f4c2a1...` | No |
| `SENTRY_AUTH_TOKEN` | supplied by CI secret store | **Yes** |
| `APP_ENVIRONMENT` | `production` | No |

`SENTRY_RELEASE` must be generated once and consumed by both runtime configuration and upload commands. Use the full commit SHA or another immutable build identifier.

## Illustrative CLI sequence

```sh
# NON-LIVE REFERENCE: do not paste into a production shell unchanged.
set -eu
set +x

npx --yes @sentry/cli@latest releases new "$SENTRY_RELEASE"
npx --yes @sentry/cli@latest releases set-commits "$SENTRY_RELEASE" --auto --ignore-missing

# Build once with SENTRY_RELEASE available to the app build.
npm ci
npm run build -- --configuration production

# Use the bundler plugin/wizard when possible. These commands show the debug-ID flow.
npx --yes @sentry/cli@latest sourcemaps inject dist/policylab-web
npx --yes @sentry/cli@latest sourcemaps upload \
  --org "$SENTRY_ORG" \
  --project "$SENTRY_PROJECT" \
  --release "$SENTRY_RELEASE" \
  dist/policylab-web

npx --yes @sentry/cli@latest releases finalize "$SENTRY_RELEASE"
```

Do not run the following until the unchanged artifact has actually deployed:

```sh
# POST-DEPLOY REFERENCE ONLY
npx --yes @sentry/cli@latest releases deploys "$SENTRY_RELEASE" new -e "$APP_ENVIRONMENT"
```

Prefer a version-pinned, approved CLI image/package in real CI rather than `@latest`. `@latest` here signals that trainers must revalidate syntax; reproducible production pipelines pin and renovate deliberately.

## Verification gate

Fail or hold promotion when:

- build release and upload release differ;
- expected chunks/maps/mappings/symbols are absent;
- the upload used output from a different build;
- token appears in logs/artifacts;
- a synthetic event is not readable and correctly tagged;
- privacy canaries remain in stored event JSON.

For a JavaScript mapping failure:

```sh
npx --yes @sentry/cli@latest sourcemaps explain <32-character-event-id>
```

## Android and iOS

Use the current Sentry Gradle/Xcode integrations to upload mapping/native debug files from release builds. Preserve the exact archive/APK/AAB-to-debug-file relationship. Never use a web project's source-map command as a substitute for R8/ProGuard mappings or dSYMs.

## Sources and validation

- [Sentry: API Permissions and Scopes](https://docs.sentry.io/api/permissions/)
- [Sentry: Troubleshooting Source Maps](https://docs.sentry.io/platforms/javascript/guides/angular/sourcemaps/troubleshooting_js/)
- [Sentry: Android Gradle Plugin](https://docs.sentry.io/platforms/android/configuration/gradle/)
- [Sentry: iOS Debug Symbols](https://docs.sentry.io/platforms/apple/guides/ios/dsym/)

Validation: CLI concepts, debug-ID troubleshooting, CI scope, and platform artifact guidance checked 2026-06-29. Command syntax must be rechecked against the pinned CLI version before production use.
