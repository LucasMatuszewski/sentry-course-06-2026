# Sentry Android standalone reference

This is a deliberately small Android app adapted from Sentry's official
`sentry-samples-android` app. It demonstrates:

- Android manifest auto-initialization
- message and handled-exception capture
- breadcrumbs, a pseudonymous user ID, tags, and extras
- Sentry Logs with a deliberately non-sensitive message
- User Feedback with name/email fields hidden
- a manual transaction with a child span
- an intentional uncaught crash

It uses the released Maven artifact `io.sentry:sentry-android:8.46.0`. It does
not depend on the Sentry Java monorepo.

## Configure and run

Requirements:

- JDK 17 or newer
- Android SDK Platform 36
- a Gradle installation compatible with Android Gradle Plugin 8.13.1, or
  Android Studio

Open this directory in Android Studio, or build from a shell:

```text
gradle :app:assembleDebug -PsentryDsn=https://PUBLIC_KEY@HOST/PROJECT_ID
```

The default DSN is empty, so the project builds without credentials and Sentry
capture calls do not send data. A Sentry DSN is a client-side project address,
but it should still be supplied at build time rather than committed here.

The **Trigger uncaught crash** action terminates the app intentionally. Launch
the app again to allow the SDK to send the cached crash event.

## Privacy-safe training defaults

This reference is intentionally conservative:

- `io.sentry.send-default-pii=false` is explicit. The context demo sets only a
  fixed pseudonymous ID; it does not set a username, email address, or IP.
- Error screenshots and view-hierarchy attachments are disabled. Both can
  expose text, images, accessibility labels, or values visible in the UI.
- Logs are enabled, but the example log is a fixed message with no personal
  data, secrets, tokens, request bodies, or user-controlled values.
- The User Feedback form opens only after a labelled button tap. It hides name
  and email, ignores the Sentry scope user, and warns against entering personal,
  secret, or production data.

Before production use, define a data classification policy and scrub events in
an SDK `beforeSend` callback. Also configure Sentry project-side data scrubbing
as defense in depth. Scrub log attributes, tags, extras, breadcrumbs, request
data, and user feedback—not only exception messages.

Treat every attachment as potentially sensitive. This includes manually added
files, screenshots, view hierarchies, replay frames, and source files. Enable
an attachment only after reviewing its contents, retention, access controls,
masking behavior, and user-consent requirements. If screenshots or view
hierarchies are needed for a training exercise, make that a separate,
clearly-labelled opt-in build configuration rather than changing these safe
defaults.

## Local validation

On 2026-06-29 the initial project was assembled successfully on Windows with
JDK 21, Android SDK Platform 36, and the pinned Sentry Java upstream Gradle
9.5.1 wrapper:

```text
C:\path\to\sentry-java\gradlew.bat -p C:\path\to\android-sentry-reference :app:assembleDebug
```

The wrapper was used only as the Gradle launcher; the project resolved the
released Maven artifact and did not use any source project from that checkout.
The generated debug APK is a local ignored build artifact. The subsequent
Logs, User Feedback, and privacy-default review update was source-policy
validated but could not be reassembled because shell execution became
unavailable on the validation host. Re-run the command above before relying on
that update as build-verified.

## Scope

This is a teaching reference, not a copy of the full upstream acceptance-test
host. See [UPSTREAM.md](UPSTREAM.md) for exact provenance, omissions, and a
copy/comparison guide.
