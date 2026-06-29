# Android Sentry local build and R8 design

## Goal

Make `apps/android` a reproducible local Sentry training application that builds on Windows without a device, installs on a USB-connected Android device, and demonstrates readable Sentry stack traces from an R8-minified APK.

## Scope

This phase repairs the existing Android build and Sentry integration:

- reproducible Gradle command-line and Android Studio builds;
- current compatible Android and Sentry tooling;
- debug and locally installable minified builds;
- automatic R8 mapping and JVM source-bundle generation;
- authenticated upload when `SENTRY_AUTH_TOKEN` is present;
- one consistent Sentry initialization and release identity;
- automated build/artifact checks and a physical-device verification runbook.

Structured Sentry Logs, Session Replay, ANR demonstrations, screenshots or attachments, feature flags, and expanded profiling exercises are a separate second phase.

Publishing, Play Store signing, and production key management are not in scope.

## Toolchain

- Android Gradle Plugin: `9.1.1`
- Gradle wrapper: `9.3.1`, the minimum and default documented for AGP 9.1
- Build JDK: Android Studio JBR or JDK 17+
- Sentry Android Gradle plugin: `6.13.0`
- Sentry Android SDK/BOM: `8.46.0`
- Compile and target SDK: existing Android API 36 configuration

Versions were validated against official Android and Sentry release metadata on 2026-06-29.

## Build variants

`debug` remains non-minified and uses Android's standard generated debug signing key.

`demoRelease` is the trainer build:

- initialized from release defaults;
- R8 code minification and resource shrinking enabled;
- `proguard-android-optimize.txt` plus project rules;
- signed with the standard debug signing configuration;
- installable through Android Studio or `adb`;
- never represented as suitable for publishing.

The custom variant avoids private keystores while preserving a clear minified-build teaching path.

## Sentry initialization and configuration

The application uses one explicit initialization path. Manifest auto-initialization is disabled.

Runtime configuration comes from generated `BuildConfig` values backed by the existing local `.env`/`.env.example` mechanism:

- `SENTRY_DSN`;
- `SENTRY_ENVIRONMENT`;
- training sample rates where needed.

The SDK computes the release from the installed package, `versionName`, and `versionCode`; application code does not hard-code a conflicting release. `sendDefaultPii` remains false. Raw PESEL-like input is not attached to Sentry events.

The public DSN may be stored as a configurable example value. `SENTRY_AUTH_TOKEN` must never be written to `.env`, Gradle properties, source code, resources, or the APK.

## Mapping and source-context pipeline

R8 creates `app/build/outputs/mapping/demoRelease/mapping.txt`.

The Sentry Gradle plugin:

- creates a deterministic ProGuard UUID from the mapping;
- injects the UUID into `assets/sentry-debug-meta.properties`;
- creates a JVM source bundle with its own debug ID;
- injects that bundle ID into the same metadata;
- uploads both artifacts to the configured Sentry project when the token is available.

Without a token, the build still generates the local mapping, metadata, and source-bundle artifacts, while upload tasks use their non-upload path.

The exact APK and mapping must be preserved as a pair. Manual recovery uses:

```powershell
sentry-cli proguard upload --org devpowers --project android `
  .\app\build\outputs\mapping\demoRelease\mapping.txt
```

## Authentication

For one PowerShell session:

```powershell
$env:SENTRY_AUTH_TOKEN = "sntrys_REPLACE_WITH_ORG_TOKEN"
```

Build from the same terminal:

```powershell
cd C:\Users\BiuroEdukey\DEV\COURSES\sentry-course-06-2026\apps\android
.\gradlew.bat :app:assembleDemoRelease
```

Remove the token from the session afterward:

```powershell
Remove-Item Env:SENTRY_AUTH_TOKEN
```

To make Android Studio inherit the token, close Android Studio, set the variable in PowerShell, and launch Studio from that same process:

```powershell
$env:SENTRY_AUTH_TOKEN = "sntrys_REPLACE_WITH_ORG_TOKEN"
& "C:\Program Files\Android\Android Studio\bin\studio64.exe" `
  "C:\Users\BiuroEdukey\DEV\COURSES\sentry-course-06-2026\apps\android"
```

## Verification

Automated checks must prove:

1. Gradle configures successfully with no Sentry token.
2. Unit and Robolectric tests pass.
3. `assembleDebug` produces an installable debug APK.
4. `assembleDemoRelease` produces an installable minified APK.
5. `mapping.txt` exists and contains application mappings.
6. the APK contains `assets/sentry-debug-meta.properties`;
7. local Sentry source-bundle output exists;
8. CI runs the same wrapper-based checks without upload credentials.

Physical-device verification on the Galaxy S24 must prove:

1. `adb devices -l` shows the authorized phone;
2. the debug and demo builds install;
3. a deliberate demoRelease crash reaches Sentry;
4. the event reports the expected environment and release;
5. stack frames show original Kotlin class and method names;
6. source context is displayed around the failing line.

Failure of server-side upload or deobfuscation is reported explicitly; it is never inferred merely from a successful APK build.
