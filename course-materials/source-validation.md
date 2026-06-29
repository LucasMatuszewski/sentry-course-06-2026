# Source validation register

**Baseline validation date:** 2026-06-29

Course content must be checked against current official sources at implementation time. A live documentation page or current official repository state is preferred over publication-date-based articles.

## Primary sources

| Area | Source |
|---|---|
| Sentry product and platform docs | [docs.sentry.io](https://docs.sentry.io/) |
| Sentry documentation source | [getsentry/sentry-docs](https://github.com/getsentry/sentry-docs) |
| Java, Spring Boot, Android SDK | [getsentry/sentry-java](https://github.com/getsentry/sentry-java) |
| Angular/JavaScript SDK | [getsentry/sentry-javascript](https://github.com/getsentry/sentry-javascript) |
| Apple SDK | [getsentry/sentry-cocoa](https://github.com/getsentry/sentry-cocoa) |
| Sentry CLI | [getsentry/sentry-cli](https://github.com/getsentry/sentry-cli) |
| Android development/device mirroring | [Android Developers](https://developer.android.com/studio/run/device) |
| Angular | [angular.dev](https://angular.dev/) |
| Spring Boot | [Spring Boot reference](https://docs.spring.io/spring-boot/) |
| GitHub Actions | [GitHub Actions documentation](https://docs.github.com/actions) |
| Bitbucket Pipelines | [Atlassian Support](https://support.atlassian.com/bitbucket-cloud/docs/get-started-with-bitbucket-pipelines/) |
| Jenkins Pipeline | [Jenkins documentation](https://www.jenkins.io/doc/book/pipeline/) |

## Pinned upstream research

- `getsentry/sentry-java`: `d8b6ce11cabd05be9a3f03a1d20fe247956d091d`
- The adjacent clone is research-only and is not a repository submodule.
- Android and iOS reference directories must include their own `UPSTREAM.md` with exact copied scope and adaptations.

## Validation rules

1. Prefer current live official documentation and current wizard output.
2. Record exact SDK/build-plugin versions in each executable app.
3. Do not use a legacy-version page when a current guide exists.
4. When wizard output and prose documentation differ, test the current wizard result and document the difference.
5. Mark platform behavior that cannot be locally verified.
6. Recheck Sentry UI navigation immediately before delivery because product labels can change independently from SDK versions.
