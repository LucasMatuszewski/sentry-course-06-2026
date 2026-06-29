# PolicyLab Android demo

Snapshot Androidowej demo aplikacji zaimportowany z Google AI Studio (commit `f374841`) i poddany code review (zobacz [`CODE_REVIEW.md`](CODE_REVIEW.md)). DSN pokazuje na `devpowers/android`.

Alternatywne ścieżki dla uczestników bez Android Studio:

- [Open the PolicyLab/Sentry demo in Google AI Studio](https://ai.studio/apps/f3f80ee8-2b62-41a3-84bb-79d5f97f8558) — zero-install
- scrcpy ([github.com/Genymobile/scrcpy](https://github.com/Genymobile/scrcpy)) jako cross-OS fallback do mirroringu fizycznego urządzenia trenera (Galaxy S24 Ultra).

Aplikacja jest po review zgodnie z poniższą listą — żadna pozycja nie jest TODO.

## Review gate (PASSED)

- No embedded `SENTRY_AUTH_TOKEN`, `.sentryclirc`, or private signing material
- DSN and PolicyLab API URL configurable outside source
- SDK initialized once and early
- `sendDefaultPii` disabled unless explicitly demonstrated
- Safe pseudonymous user context and custom tags
- Logs, handled errors, crashes, feedback, traces, Replay, and profiling checked against current SDK APIs
- Production sampling differs from the 100% training configuration
- Release/environment set consistently
- R8/ProGuard mapping upload enabled only in a CI/release build
- Retrofit/OkHttp integration propagates Android → Spring traces
- Debug and release builds verified before the course

## Trainer: physical Galaxy S24 Ultra

1. Install current Android Studio and SDK Platform Tools.
2. On the phone, enable Developer options and USB debugging.
3. Connect by USB. On Ubuntu, ensure the user belongs to `plugdev` and Android udev rules are installed.
4. Confirm the phone with `adb devices` and approve the phone's trust prompt.
5. Open **View → Tool Windows → Device Manager** and select the connected phone.
6. Start **Device Mirroring**. The phone appears in Android Studio's Running Devices window.
7. In Microsoft Teams, share only the Android Studio/Running Devices window.
8. Enable Do Not Disturb and hide notification previews before sharing.

Android 11+ can also pair through **Pair Devices Using Wi-Fi** when laptop and phone use the same trusted network.

## Fallback: scrcpy

[scrcpy](https://github.com/Genymobile/scrcpy) mirrors and controls an Android device over USB or TCP/IP on Linux and Windows without installing a permanent phone app.

Use only the official Genymobile repository/packages. Share the scrcpy window in Teams.

## Device Streaming

Firebase-powered Android Device Streaming is an emergency fallback for a missing physical device. It is not required for sharing a locally connected phone: Android Studio Device Mirroring already handles that.

## Sources and validation

Validated 2026-06-29:

- [Run apps on a hardware device and Device Mirroring](https://developer.android.com/studio/run/device)
- [Android Device Streaming](https://developer.android.com/studio/run/android-device-streaming)
- [Sentry for Android](https://docs.sentry.io/platforms/android/)
- [Official Sentry Android sample](https://github.com/getsentry/sentry-java/tree/main/sentry-samples/sentry-samples-android)
