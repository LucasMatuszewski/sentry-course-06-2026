# Setup: iOS

## Install

Use Xcode's Swift Package Manager with:

```text
https://github.com/getsentry/sentry-cocoa
```

Select the version approved by the application team/current Sentry setup page. Do not pin workshop notes to an unverified version.

## Initialize

```swift
import Sentry

SentrySDK.start { options in
    options.dsn = ProcessInfo.processInfo.environment["SENTRY_DSN"]
    options.environment = "staging"
    options.releaseName = "policylab-ios@2026.06.30+3f4c2a1"
    options.tracesSampleRate = 0.10
    options.sendDefaultPii = false
}
```

This is a configuration shape. For a real app, provide environment/release through the approved build configuration and use exact property names from the installed SDK. A DSN may be embedded in the app; an auth token must never be.

## Safe context

```swift
let user = User(userId: "user-17")
SentrySDK.setUser(user)
SentrySDK.configureScope { scope in
    scope.setTag(value: "broker", key: "quote.channel")
    scope.setContext(value: [
        "quote_ref": "Q-1042",
        "product_code": "MOTOR_STANDARD"
    ], key: "quote")
}
```

Avoid names, email, policyholder data, keychain values, request bodies, or free-form user text.

## dSYMs and CI

Symbolication requires dSYMs matching the exact build UUID. Configure the current Sentry Xcode build phase/CLI integration from official docs, keep `SENTRY_AUTH_TOKEN` in the CI secret store, upload before events, and never rebuild between upload and distribution.

Verification:

1. archive a staging build;
2. upload its dSYMs;
3. trigger a synthetic crash using the official test method;
4. relaunch so queued crash data can send;
5. verify readable application frames, release/build, environment, device/OS, and safe context;
6. compare reported missing debug files with local archive UUIDs if symbolication fails.

Crash only a disposable workshop build, never an attendee's production app.

## Zero-install workshop path

Inspect a trainer-provided iOS event and dSYM status. Identify the build, OS/device, symbolication state, safe context, and next diagnostic action without Xcode or an iPhone.

## Sources and validation

- [Sentry for iOS](https://docs.sentry.io/platforms/apple/guides/ios/)
- [iOS installation](https://docs.sentry.io/platforms/apple/guides/ios/install/)
- [iOS configuration](https://docs.sentry.io/platforms/apple/guides/ios/configuration/)
- [iOS debug symbols](https://docs.sentry.io/platforms/apple/guides/ios/dsym/)
- [Official Sentry Cocoa repository](https://github.com/getsentry/sentry-cocoa)

Validation: official iOS SDK, Swift Package Manager, and dSYM guidance checked 2026-06-29.
