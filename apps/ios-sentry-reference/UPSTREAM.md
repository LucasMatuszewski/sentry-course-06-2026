# iOS upstream provenance and comparison guide

Validated: 2026-06-29

## Source

- Repository: <https://github.com/getsentry/sentry-cocoa>
- Shallow-cloned commit:
  `1ec4cb5bf6059838f8ce137ba53aeac6765317ce`
- Commit date: 2026-06-28
- Current official release used here: `9.19.0`, published 2026-06-24
- License: MIT; the upstream license text is preserved in `LICENSE.md`.

## Actual official sample paths

The main UIKit reference is:

```text
Samples/iOS-Swift/
Samples/iOS-Swift/iOS-Swift.yml
Samples/iOS-Swift/App/Sources/
```

It is a comprehensive UI-test host with extensions, storyboards, shared code,
shared xcconfig files, test plans, and a local `Sentry.xcodeproj` reference.

A smaller Swift 6 UIKit smoke sample is:

```text
Samples/iOS-Swift6/
Samples/iOS-Swift6/App/Sources/AppDelegate.swift
Samples/iOS-Swift6/App/Sources/SceneDelegate.swift
Samples/iOS-Swift6/App/Sources/ViewController.swift
```

That target still depends on the monorepo-only
`Samples/SentrySampleShared/SentrySampleShared.xcodeproj`.

The feature-dense SwiftUI reference is:

```text
Samples/iOS-SwiftUI/
Samples/iOS-SwiftUI/iOS-SwiftUI.yml
Samples/iOS-SwiftUI/App/Sources/
```

The smallest complete SwiftUI/SPM reference, selected for this adaptation, is:

```text
Samples/iOS-SwiftUI-SPM/iOS-SwiftUI-SPM.yml
Samples/iOS-SwiftUI-SPM/Sources/MainApp.swift
Samples/iOS-SwiftUI-SPM/Sources/ContentView.swift
Samples/iOS-SwiftUI-SPM/Resources/Info.plist
```

## Adaptations made here

| Official file | Local file | Change |
| --- | --- | --- |
| `iOS-SwiftUI-SPM.yml` | `project.yml` | local monorepo package replaced with remote package `from: 9.19.0`; watchOS target removed |
| `Sources/MainApp.swift` | same relative path | official DSN replaced by the `SENTRY_DSN` build setting |
| `Sources/ContentView.swift` | same relative path | wording clarified; capture and `.sentryTrace` patterns retained |
| `Resources/Info.plist` | same relative path | test-only Git metadata and obsolete capability declaration removed; `SENTRY_DSN` added |

No generated `.xcodeproj` is checked in. `project.yml` is the source of truth,
matching the upstream repository's XcodeGen workflow while avoiding a large,
fragile generated-project diff.

## Copying additional UIKit or SwiftUI examples

1. Start from the same pinned commit and read `Samples/AGENTS.md`.
2. Identify the source file and every storyboard, asset, plist, entitlement,
   xcconfig, shared target, and test plan referenced by its XcodeGen target.
3. Replace local project dependencies such as `Sentry/Sentry` and
   `SentrySampleShared/...` with the released SPM package/product where public
   APIs permit it.
4. Do not copy `SentrySampleShared` wholesale. It is test infrastructure, and
   parts of its wrapper expose scenarios tied to the SDK monorepo.
5. Add one target at a time to `project.yml`, regenerate with XcodeGen, and
   build with `xcodebuild` on macOS after each addition.

UIKit consumers can import `Sentry` or `SentrySwift` from the same `SentrySPM`
product. The upstream `iOS-Swift6` trio is the clearest small UIKit comparison,
but it is not independently complete until its shared wrapper and storyboard
coupling are removed.

