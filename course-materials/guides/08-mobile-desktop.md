# Mobile and desktop observability

## Why mobile is different

Mobile failures depend on app version/build, OS, device model, network, lifecycle, and symbol files. Clients may be offline and may send queued events later. A single “latest” release is not representative because old versions remain installed.

Minimum mobile dimensions:

- release plus build/distribution;
- environment;
- OS name/version;
- device family/model, kept only when operationally justified;
- app foreground/background and connectivity state;
- handled versus unhandled/crash;
- trace and user-feedback linkage where supported.

## Release health and symbolication

Before triage, confirm the event has the expected release/build and is symbolicated/deobfuscated:

- Android: upload R8/ProGuard mapping and native symbols for the exact build.
- iOS: upload matching dSYMs and verify UUIDs.
- Desktop native apps: upload platform debug files and preserve the build-to-symbol mapping.
- Electron: combine JavaScript source maps with native crash artifact handling as documented for Electron.

Never regenerate artifacts after shipping and assume they match.

## Mobile triage

1. Filter project, production environment, release/build, OS, and device.
2. Compare first/last seen and affected users.
3. Check crash status, exception/thread, breadcrumbs, app lifecycle, network, and device context.
4. Confirm symbolication/deobfuscation before assigning a code owner.
5. Check distribution: one device model may indicate a vendor/OS issue; all models may indicate application code.
6. Correlate feedback with the event ID and trace when available.

## Desktop considerations

- Separate renderer, main process, and native crashes where the platform requires it.
- Record updater channel and app build without recording workstation/user names.
- Capture shutdown/flush behavior because desktop apps may exit abruptly.
- Test packaged production builds; development stacks and code-signing paths differ.

## Workshop safety

Use the provided Galaxy S24 and synthetic PolicyLab account. Do not connect a personal device or expose personal notifications during screen sharing. Enable **Do Not Disturb**, close personal apps, and revoke USB/wireless debugging authorization after the workshop.

## Sources and validation

- [Sentry for Android](https://docs.sentry.io/platforms/android/)
- [Sentry for iOS](https://docs.sentry.io/platforms/apple/guides/ios/)
- [Sentry for Electron](https://docs.sentry.io/platforms/javascript/guides/electron/)
- [Sentry: Android Gradle Plugin](https://docs.sentry.io/platforms/android/configuration/gradle/)
- [Sentry: iOS Debug Symbols](https://docs.sentry.io/platforms/apple/guides/ios/dsym/)

Validation: official Android, Apple, and Electron guidance checked 2026-06-29.
