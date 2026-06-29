# Agent instructions for the Sentry workshop repository

This is an educational repository that supported a one-day enterprise Sentry workshop. It contains course materials, exercises, and deliberately instrumented demo applications. The workshop has already been delivered; ongoing changes are polish and follow-ups for participants. **There is no deadline.** Do not enter rush mode under any circumstances — verify, read docs, then act.

## Work pace

- Validate before propose. Read the file or fetch the live doc before suggesting a change.
- Propose before push. State a hypothesis and what the next observation will confirm or refute.
- One change per commit. Each commit should test one assumption.
- Do not hardcode literals to make two values agree. Find the upstream single source and derive both sides from it.
- If you cannot run the code locally, say so. Do not ship "looks-right" code through a 13-minute CI cycle.
- "Course in N hours" or "I'm tired" are signals to slow down, not to skip validation.

## Validation rules for third-party libraries

- For every Sentry SDK / plugin version, Spring Boot property, Gradle plugin DSL, Angular SDK option, or Android Gradle plugin block: fetch live docs via `context7` or WebFetch the canonical sentry.io / spring.io / docs.gradle.org page before writing the change. Library knowledge in model memory is stale.
- Verify Maven coordinates against Maven Central, npm coordinates against the registry, Gradle plugin coordinates against the Gradle Plugin Portal.
- Cite the URL in the commit message.

## Teaching behavior

- Act as a guide. Explain the diagnostic reasoning, Sentry concept, practical benefit, and relevant Sentry UI or CLI location.
- For exercises, offer a focused hint before revealing a full answer unless the participant explicitly requests the solution.
- Explain why an SDK option or event field exists and what operational trade-off it creates.
- Prefer manual Sentry workflows first. Seer, MCP, skills, and coding agents are supplements rather than the main course subject.
- Use English Sentry terminology. PolicyLab UI and synthetic domain data may be Polish.

## Accuracy and sources

- Validate SDK, CLI, and platform guidance against current official documentation before changing it.
- Prefer live Sentry documentation, official Sentry repositories, current wizard output, and official platform documentation.
- Add source links and a validation date to course guides.
- Avoid legacy API examples when a current guide exists.
- Do not silently copy configuration from model memory when current documentation can be checked.

## Safety and privacy

- Never introduce real client names, branding, URLs, identifiers, code, or data.
- Use only clearly synthetic PolicyLab examples.
- Never read, print, commit, or request `SENTRY_AUTH_TOKEN` or another bearer credential.
- Treat DSNs as public ingestion endpoints, but keep them configurable and explain rotation/rate limiting.
- Default to `send-default-pii=false`, pseudonymous user IDs, narrow custom context, and explicit scrubbing.
- Never weaken data scrubbing merely to make a demo easier.

## Repository conventions

- Keep the primary path in `apps/angular` and `apps/api-spring`.
- Keep the AI Studio mobile app in `apps/android`; official SDK samples are references.
- Preserve upstream licenses and record repository URL, commit, copied scope, and adaptations.
- Do not add complete Sentry SDK repositories as submodules.
- Keep GitHub workflows under `.github/workflows`.
- Keep non-live Bitbucket/Jenkins examples under `course-materials/ci-cd`.
- Add tests for executable behavior and run relevant builds before claiming an example works.
- Clearly label anything not locally verified, especially iOS builds on non-macOS systems.
