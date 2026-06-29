# Sentry glossary

Validated against current Sentry documentation on **2026-06-29**.

| Term | Practical meaning |
|---|---|
| Organization | Top-level boundary containing teams, projects, integrations, and shared settings. |
| Team | Group used for access, ownership, assignment, and routing. |
| Project | Telemetry boundary for one application or service and its SDK platform. |
| SDK | Runtime library that captures and sends application telemetry. |
| DSN | Public ingestion URL identifying a Sentry project; not an administrative token. |
| Event | One captured occurrence such as an error, transaction, log, or feedback item. |
| Issue | Group of related events considered the same underlying problem. |
| Grouping | Rules deciding which events belong to one issue. |
| Fingerprint | Explicit grouping key overriding or extending default grouping. |
| Tag | Indexed key/value field for filtering, aggregation, and search. |
| Context | Structured diagnostic data displayed with an event; not normally used for high-cardinality search. |
| Extra | Additional unstructured event data; prefer structured context for new instrumentation. |
| User context | Pseudonymous information connecting impact to users. Avoid unnecessary personal data. |
| Breadcrumb | Time-ordered clue recorded before an event, such as navigation, request, or state change. |
| Environment | Deployment context such as production, staging, development, or `training`. |
| Release | Version identifier connecting events to code and deployments. |
| Deploy | Record that a release reached an environment. |
| Transaction | Top-level measured operation represented within a trace. |
| Trace | End-to-end causal path across applications and services. |
| Span | Timed operation inside a trace, such as HTTP, database, or pricing logic. |
| Trace propagation | Passing trace headers so spans join the same trace. |
| Sampling | Selecting telemetry to retain; errors, traces, replay, logs, and profiles use different controls. |
| Log | Structured diagnostic record correlated with traces and issues. |
| Metric | Numeric measurement aggregated over time. |
| Profile | Samples showing which functions consume execution time. |
| Session Replay | Captured UI-session context, subject to masking and sampling. |
| Monitor | Check for scheduled jobs, uptime, or another expected behavior. |
| Alert | Detection/notification rule for issues, regressions, thresholds, or monitors. |
| Ownership | Rules mapping code or issue patterns to responsible teams/users. |
| Suspect commit | Commit Sentry estimates may have introduced an issue. |
| Source map | Mapping from generated/minified JavaScript to TypeScript/original source. |
| Source context / source bundle | JVM source upload that adds surrounding Java/Kotlin code to stack frames. |
| Symbolication artifact | Umbrella term for files converting production addresses/names into readable source locations. |
| ProGuard/R8 mapping | Android mapping that reverses Java/Kotlin name obfuscation. |
| Native debug symbol | Metadata used to symbolicate compiled native machine code. |
| NDK symbol | Android native debug information for C/C++ code built with the NDK. |
| dSYM | Apple debug-symbol bundle used to symbolicate iOS/macOS crash addresses. |
| Debug ID | Identifier injected into an artifact and mapping file for reliable matching. |
| Sentry CLI | Tool for releases, artifacts, deploy records, source maps, and diagnostics. |
| Data Scrubbing | Removing or masking sensitive fields before storage/display. |
| Seer | Sentry AI-assisted debugging capabilities; availability depends on plan/configuration. |
| MCP | Protocol integration allowing an authorized agent to query Sentry through controlled tools. |

## Source-artifact distinction

- Angular/TypeScript: source maps with matching debug IDs.
- Java/Spring: source context/source bundles plus releases/commits.
- Android Java/Kotlin: R8/ProGuard mappings; native symbols for NDK crashes.
- iOS/Swift: dSYMs from the matching archive/build.

These artifacts are build-specific. A file from a different release cannot reliably repair a production stack trace.

## Sources

- [Sentry concepts](https://docs.sentry.io/concepts/)
- [Sentry releases](https://docs.sentry.io/product/releases/)
- [Java source context](https://docs.sentry.io/platforms/java/source-context/)
- [JavaScript source maps](https://docs.sentry.io/platforms/javascript/sourcemaps/)
- [Android ProGuard/R8](https://docs.sentry.io/platforms/android/configuration/proguard/)
- [Apple debug symbols](https://docs.sentry.io/platforms/apple/dsym/)
