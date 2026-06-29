# Sentry iOS SwiftUI reference

This is a small, standalone adaptation of Sentry Cocoa's official
`iOS-SwiftUI-SPM` sample. It demonstrates:

- SDK startup from a SwiftUI `App`
- released Swift Package Manager dependency resolution
- handled error capture
- SwiftUI view tracing with `.sentryTrace`
- selected automatic performance, interaction, MetricKit, log, and simulator
  Spotlight options used by the official sample

The XcodeGen spec resolves the released Sentry Cocoa `9.19.0` package and its
`SentrySPM` product. It does not require a checkout of the Sentry Cocoa
monorepo.

## Generate and build on macOS

Requirements:

- macOS with a compatible Xcode/iOS SDK
- XcodeGen
- network access for Swift Package Manager resolution

From this directory:

```text
xcodegen generate --spec project.yml
xcodebuild \
  -project iOS-Sentry-Reference.xcodeproj \
  -scheme iOS-Sentry-Reference \
  -sdk iphonesimulator \
  -destination "generic/platform=iOS Simulator" \
  CODE_SIGNING_ALLOWED=NO \
  SENTRY_DSN="https://PUBLIC_KEY@HOST/PROJECT_ID" \
  build
```

You can also open the generated project in Xcode and set `SENTRY_DSN` as a
user-defined build setting. The default is empty, so no credentials or project
address are committed.

## Privacy and Apple release operations

The sample sets `sendDefaultPii=false` and `attachScreenshot=false`. Error
screenshots can include personal data, authentication codes, private messages,
health or payment information, and other on-screen content. Enable
`options.attachScreenshot = true` only as an explicit product/privacy decision,
after testing masking and exclusion behavior on every sensitive screen. Review
Sentry's [Apple screenshot guidance](https://docs.sentry.io/platforms/apple/guides/ios/enriching-events/screenshots/)
before opting in.

Logs remain enabled to demonstrate the current API. Keep log messages and
attributes free of secrets, access tokens, personal data, request bodies, and
unreviewed user input. Disable `enableLogs` if that cannot be enforced, and use
SDK `beforeSend` hooks plus project-side data scrubbing for defense in depth.

Sentry Cocoa includes its own `Sources/Resources/PrivacyInfo.xcprivacy` privacy
manifest. Swift Package Manager should bundle that vendor manifest. Inspect
Xcode's generated privacy report for each release, keep the SDK current, and
maintain the app's own privacy manifest and App Store privacy disclosures for
the app's actual collection and use. Do not edit the dependency's manifest to
describe app-specific behavior.

For readable production crash reports, archive with dSYM generation enabled
and upload the matching dSYMs for each release using the Sentry Xcode build
phase, Sentry wizard, or `sentry-cli debug-files upload`. Verify debug-file IDs
match the shipped binary. See Sentry's [Apple dSYM documentation](https://docs.sentry.io/platforms/apple/guides/ios/dsym/).

Source context/source bundles are optional and may upload portions of
proprietary source code alongside debug files. Review repository policy,
retention, access controls, and the exact `--include-sources` inputs before
enabling them.

`SENTRY_AUTH_TOKEN` is a deployment secret, unlike the client-side DSN. Keep it
only in the secret store of the macOS CI/release job that performs uploads,
scope it minimally for CI uploads, mask it in logs, and rotate it. Never place
an upload token in this repository, an xcconfig, Xcode user settings, the app
bundle, source code, or a developer-distributed archive.

## Validation boundary

This project was source-validated on 2026-06-29 but was **not built locally**:
the research and adaptation were performed on Windows, where Xcode and Apple
platform SDKs are unavailable. Do not interpret the presence of an XcodeGen
spec as a recorded successful Apple build.

See [UPSTREAM.md](UPSTREAM.md) for exact provenance and the UIKit/SwiftUI
comparison paths.
