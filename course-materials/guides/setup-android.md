# Setup: Android and Galaxy S24 demo

## Sentry SDK

Use the current Android setup page/wizard and Sentry Android Gradle plugin. Let the official page supply approved plugin/SDK versions.

Manual initialization shape:

```kotlin
import io.sentry.android.core.SentryAndroid

SentryAndroid.init(this) { options ->
    options.dsn = BuildConfig.SENTRY_DSN
    options.environment = BuildConfig.APP_ENVIRONMENT
    options.release = BuildConfig.APP_RELEASE
    options.tracesSampleRate = 0.10
    options.isSendDefaultPii = false
}
```

Prefer manifest/Gradle configuration produced by the current wizard. Never put `SENTRY_AUTH_TOKEN` in `BuildConfig`, resources, APK, or source control. It belongs only in CI for mapping/symbol upload.

Verify a release build: deobfuscated Kotlin/Java frames, exact release/build, environment, breadcrumbs, trace, and R8/ProGuard mapping upload.

## Galaxy S24: prepare safely

1. Use the workshop phone, not a personal device.
2. Enable **Do Not Disturb** and close personal/enterprise apps.
3. On Samsung, open **Settings > About phone > Software information** and tap **Build number** seven times.
4. Return to **Settings > Developer options** and enable only the debugging mode needed.
5. Revoke debugging authorizations and disable developer options after the session.

Samsung/Android labels can vary by One UI version or enterprise policy. Search Settings for **Build number**, **Developer options**, or **Wireless debugging** if the path differs.

## USB ADB

1. Use a data-capable USB-C cable.
2. Unlock the phone and enable **USB debugging**.
3. Connect, choose a data/Android Auto USB mode if prompted, and accept the RSA fingerprint only for the workshop PC.
4. Verify:

```powershell
adb devices -l
```

Expected: one device in `device` state. `unauthorized` means unlock the phone and accept/revoke/retry. No device often means cable, USB mode, driver, policy, or port.

## Wireless ADB (Android 11+)

Phone and PC must be on the same trusted network; corporate Wi-Fi may block peer discovery.

Android Studio path validated by Android documentation:

1. enable **Wireless debugging**;
2. in Android Studio choose **Pair Devices Using Wi-Fi** from the run-device menu, or use Device Manager;
3. pair with QR code or six-digit pairing code;
4. deploy and confirm the expected serial in `adb devices -l`.

CLI fallback:

```powershell
adb pair <phone-ip>:<pairing-port>
adb connect <phone-ip>:<debug-port>
adb devices -l
```

The pairing and debug ports can differ and change. Read both from the phone's Wireless debugging screen; do not assume port 5555.

## Mirroring and sharing

### Android Studio

Open **View > Tool Windows > Running Devices** or Device Manager and start device mirroring. Current Android Studio supports mirroring devices connected through USB or wireless debugging. Disable mirrored notifications and audio if not needed.

### Microsoft Teams

Share only the Android Studio **Running Devices** window or the scrcpy window, not the full desktop. Turn off notification previews. Teams UI changes frequently: use the current meeting **Share** control and select the single window.

### scrcpy fallback

Use only the official Genymobile release:

```powershell
scrcpy --select-usb
scrcpy --select-tcpip
```

If multiple devices exist, select the exact serial (`scrcpy --serial <serial>`). scrcpy uses ADB and can control the phone; close it and revoke authorization afterward.

## Workshop app and AI Studio

- Open the supplied PolicyLab Android build and use only synthetic account `user-17`.
- Optional trainer resource: [PolicyLab AI Studio app](https://ai.studio/apps/f3f80ee8-2b62-41a3-84bb-79d5f97f8558).
- AI Studio is supplemental. Do not upload Sentry event payloads, secrets, customer data, or client code.

## Zero-install workshop path

Use the trainer's mirrored device and shared Sentry project. Participants can filter the prepared Android issue by release/device/OS and inspect feedback without installing Android Studio, ADB, or scrcpy.

## Sources and validation

- [Sentry for Android](https://docs.sentry.io/platforms/android/)
- [Sentry Android Gradle Plugin](https://docs.sentry.io/platforms/android/configuration/gradle/)
- [Android Developers: Run apps on a hardware device](https://developer.android.com/studio/run/device)
- [Android Developers: Developer options](https://developer.android.com/studio/debug/dev-options)
- [Genymobile: official scrcpy repository](https://github.com/Genymobile/scrcpy)
- [Microsoft Support: Share content in Teams meetings](https://support.microsoft.com/en-us/office/share-content-in-microsoft-teams-meetings-fcc2bf59-aecd-4481-8f99-ce55dd836ce8)

Validation: official Sentry, Android device/ADB/mirroring, scrcpy, and Teams sources checked 2026-06-29. Galaxy S24 labels must be verified against its installed One UI build and organizational policy.
