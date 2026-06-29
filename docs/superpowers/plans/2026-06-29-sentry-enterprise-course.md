# **Sentry Enterprise Developer Workshop Repository Implementation Plan**

**For implementation:** use `superpowers:executing-plans`. Do not delegate to subagents unless the user explicitly requests it.

**Goal:** Deliver a course-ready public repository, practical demo system, exercises, guides, and interactive HTML materials for a one-day Sentry workshop on 2026-06-30.

**Architecture:** A polyglot monorepo containing Angular and Spring Boot 4 as the primary hands-on path, a reviewed Android application as the mobile demonstration, official Sentry samples as references, and an iOS reference project. One deployed PolicyLab training system provides synthetic insurance scenarios and end-to-end traces.

**Tech stack:** Angular, TypeScript, Spring Boot 4, Java 17+, Gradle, Android/Kotlin, Swift/iOS reference code, Sentry SaaS EU, GitHub Actions, Docker Compose, Coolify, optional Bitbucket/Jenkins examples, vanilla HTML/CSS/JavaScript slides.

---

## **1\. Fixed decisions and priorities**

* Course language: English materials and Sentry terminology; Polish synthetic application UI is allowed.  
* Audience: five senior/architect-level enterprise developers; known experience includes Java, Angular/TypeScript, Android/Kotlin, and Swift.  
* Main path: Angular → Spring Boot 4 → Sentry.  
* Android: important 20–30 minute live demonstration using the Galaxy S24 Ultra, plus optional participant access through [Google AI Studio](https://ai.studio/apps/f3f80ee8-2b62-41a3-84bb-79d5f97f8558).  
* iOS: code and conceptual material only; no claim of local Windows/Linux build verification.  
* AI/Seer/MCP: brief optional module, not the central workflow.  
* Data: fictional PolicyLab insurance domain, synthetic values only, no PZU name, branding, systems, or real customer data.  
* Repository: public GitHub repository; participants clone or fork. PR submission is optional and not part of the timed workshop.  
* Sentry access: participants receive project-level write access, not organization administration.  
* Secrets: never share `SENTRY_AUTH_TOKEN`; store it only in protected GitHub/Bitbucket variables. DSNs remain runtime configuration and may appear in training documentation because they are public ingestion endpoints.  
* Deployment: one Coolify-hosted training environment. GitHub Actions is the demonstrated pipeline; Bitbucket and Jenkins remain documented equivalents unless time permits a Bitbucket mirror.  
* P0 work must not be delayed by optional iOS, Jenkins, Bitbucket, or second Android-reference refinements.

## **2\. Ordered implementation work**

### **Phase 1 — Research baseline and repository foundation**

1. Persist this plan at `docs/superpowers/plans/2026-06-29-sentry-course-repository.md`.  
2. Record repository state: no commits on `main`, existing remote, original offer agenda/PDF, onboarding markdown, and five screenshots.  
3. Create a source-validation register containing:  
   * Validation date: 2026-06-29.  
   * Live official Sentry documentation and `getsentry/sentry-docs`.  
   * Official SDK repositories and pinned commit identifiers.  
   * Current wizard output.  
   * Official Angular, Spring, Android, Apple, GitHub, Atlassian, and Jenkins documentation.  
4. Reject cached tutorials, legacy-version pages, and pre-2026 articles when current official documentation exists.  
5. When documentation and wizard output differ, use current wizard output for generated integration code and document the discrepancy.  
6. Keep the adjacent `sentry-java-upstream` clone as research-only. Do not add it as a submodule.  
7. Create the repository structure:

apps/  
  angular/  
  api-spring/  
  android/  
  android-sentry-reference/  
  ios-sentry-reference/  
course-materials/  
  README.md  
  survey-results.md  
  course-agenda-1-day.md  
  glossary.md  
  sentry-onboarding-first-steps/  
  guides/  
  exercises/  
  ci-cd/  
  slides/  
.github/workflows/  
AGENTS.md  
CLAUDE.md  
README.md

8. Add `.gitignore` rules for auth tokens, `.env` files, `.sentryclirc`, Android `local.properties`, generated source maps, IDE files, build outputs, and Xcode user data.  
9. Scan the current repository for credentials before the initial commit.  
10. Create the first commit containing the original offer, onboarding assets, repository documentation, agent instructions, survey report, updated onboarding guide, and one-day agenda.

### **Phase 2 — Foundational documentation**

Create the following materials before application development:

* Root `README.md`:  
  * Course purpose, audience, repository map, prerequisites, quick-start paths, security warning, app-to-Sentry-project mapping, zero-install alternatives, and workshop schedule.  
* `course-materials/README.md`:  
  * Ordered learning path, links to every guide/exercise, trainer versus participant usage, and source-validation policy.  
* `survey-results.md`:  
  * Preserve all supplied answers and response counts.  
  * Separate raw responses from normalized conclusions.  
  * Highlight low Sentry experience, tracing/privacy challenges, Android requirements, enterprise Jira/Bitbucket workflow, and moderate AI interest.  
* `AGENTS.md`:  
  * Explain that this is an educational demo repository.  
  * Require agents to teach concepts, benefits, Sentry UI locations, CLI commands, privacy implications, and diagnostic reasoning.  
  * Agents should offer hints before complete exercise solutions.  
  * Require current official sources, synthetic data, least privilege, and secret scanning.  
  * Keep AI assistance secondary to manual Sentry learning.  
* `CLAUDE.md`: exactly AGENTS.md.  
* `glossary.md`:  
  * Organization, team, project, SDK, DSN, event, issue, grouping, fingerprint, tag, context, extra, user context, breadcrumb, environment, release, deploy, transaction, trace, span, propagation, sampling, log, metric, profile, replay, monitor, alert, ownership, suspect commit, source map, source context/source bundle, symbolication artifact, ProGuard/R8 mapping, native debug symbol, NDK symbol, iOS dSYM, debug ID, Sentry CLI, Seer, MCP, and data scrubbing.  
  * Explicitly distinguish JS source maps, JVM source context, Android deobfuscation/native symbolication, and Apple dSYM symbolication.

### **Phase 3 — Onboarding and current best-practice guides**

1. Update the existing onboarding markdown:  
   * Embed each screenshot immediately under its corresponding step using relative paths.  
   * Correct spelling and technical terminology.  
   * Retain DevPowers project mappings:  
     * Angular → `javascript-angular`  
     * Spring → `java-spring-boot`  
     * Android → `android`  
     * iOS → `apple-ios`  
   * Explain DSN versus auth token.  
   * Change the regulated-enterprise baseline to `send-default-pii=false`.  
   * Mark `tracesSampleRate=1.0`, replay at 100%, profiling, and debug logging as training-only settings.  
   * Explain production sampling and cost implications.  
   * Add source links and validation date.  
2. Create module guides for:  
   * Fundamentals and developer observability.  
   * Projects, environments, releases, ownership, and SDK configuration.  
   * Issue triage, grouping, fingerprinting, and impact analysis.  
   * Releases, source maps, source context, symbolication, and CI/CD.  
   * Tracing, logs, replay, profiling, and sampling.  
   * Alerts, trends, Jira/Bitbucket workflow, and alert-noise reduction.  
   * Privacy, data scrubbing, retention, permissions, and regulated environments.  
   * Mobile/desktop observability.  
   * Short Seer/MCP/skills reference.  
3. Create separate setup guides for Angular, Spring Boot 4, Android, and iOS:  
   * PowerShell and Bash commands where platform-specific.  
   * Current wizard/manual alternatives.  
   * Verification event.  
   * Common failures.  
   * Cleanup and token handling.  
4. Create one consolidated best-practices guide:  
   * Environment/release naming.  
   * Sampling strategy.  
   * Pseudonymous user IDs.  
   * Safe custom context.  
   * Scrubbing at SDK and Sentry-project levels.  
   * Ownership and alert routing.  
   * Source artifact validation.  
   * Offline/mobile event handling.  
   * Release-regression analysis.  
   * Enterprise rollout checklist.  
5. Every guide receives a “Sources and validation” section with live official links and 2026-06-29 validation metadata.

### **Phase 4 — Main PolicyLab applications**

#### **Spring Boot 4 API**

* Copy the official Spring Boot 4 sample’s source/resources as the starting point.  
* Preserve attribution to upstream commit `d8b6ce11cabd05be9a3f03a1d20fe247956d091d`.  
* Replace monorepo-only Gradle constructs with a standalone wrapper, settings, current stable Spring Boot 4.x plugin, and released Sentry dependencies.  
* Retain useful official examples only when they support the course: REST, logging, tracing, metrics, caching, and controlled failures.  
* Do not bring Kafka, GraphQL, Quartz, or profiling infrastructure into the P0 runtime unless already required by a selected exercise.  
* Add the PolicyLab API:

POST /api/quotes  
Content-Type: application/json  
X-Demo-Scenario: normal | server-error | slow | validation | pii  
{  
  "vehicleType": "CAR",  
  "manufactureYear": 2018,  
  "coverage": "EXTENDED",  
  "postalCode": "00-001",  
  "driverAge": 35  
}  
{  
  "quoteId": "Q-2026-0001",  
  "annualPremium": 1234.56,  
  "currency": "PLN",  
  "riskBand": "MEDIUM"  
}

* Enable demo headers only when `DEMO_SCENARIOS_ENABLED=true`.  
* Capture 500 errors; treat expected 4xx validation failures as normal application outcomes.  
* Add safe tags such as coverage, vehicle type, risk band, and participant alias.  
* Never attach postal code, request body, or synthetic personal fields without demonstrating explicit scrubbing.  
* Add custom spans around pricing rules and simulated dependency latency.  
* Expose `/actuator/health`.  
* Read DSN, environment, release, and sampling from environment variables.

#### **Angular application**

* Generate a standalone current-stable Angular app under `apps/angular`.  
* Use a Polish PolicyLab quote form and a trainer-only demo scenario panel.  
* Add Sentry through the current Angular wizard/manual output.  
* Implement:  
  * Global error capture.  
  * Router tracing.  
  * Logs and breadcrumbs.  
  * Session Replay with current masking defaults verified against official docs.  
  * Runtime `config.json` for API URL, DSN, environment, release, and demo-mode flag.  
  * Production source-map generation and upload.  
* Route API calls through same-origin `/api` in deployment.  
* Ensure production `.map` files are uploaded to Sentry but not served publicly.

#### **Cross-application behavior**

* Use environment `training`.  
* Use releases:  
  * `angular@<short-git-sha>`  
  * `api-spring@<short-git-sha>`  
  * `android@<versionName>+<versionCode>`  
* Propagate traces from Angular to Spring and verify one complete trace in Sentry.  
* Add a deliberately buggy release followed by a fixing release so the course can demonstrate suspect commits, regression state, and declining error trends.  
* Add deterministic event-generation commands with low event counts to protect trial quotas.

### **Phase 5 — Android primary application and official comparison**

1. Import the Google AI Studio source snapshot into `apps/android` as one commit; generation history is unnecessary.  
2. Preserve its Polish UI and event-lab features.  
3. Perform a review against current official Android guidance and the official Sentry sample:  
   * SDK initialization timing.  
   * Manifest/manual configuration.  
   * Logs, errors, handled exceptions, messages, feedback, tracing, replay, and profiling.  
   * Scope lifetime and custom context.  
   * PII and `beforeSend`.  
   * Sampling.  
   * Offline behavior.  
   * Release/environment/version.  
   * R8/ProGuard mappings.  
   * Native symbols if NDK code exists.  
   * Auth-token handling.  
   * Gradle/plugin compatibility.  
   * Kotlin/Compose lifecycle and coroutine handling.  
4. Fix only Sentry, privacy, build, and serious maintainability defects; do not redesign the application before the course.  
5. Add one Retrofit/OkHttp call to the deployed PolicyLab `/api/quotes` endpoint and verify Android → Spring trace propagation.  
6. Make API URL and DSN configurable; never embed an auth token.  
7. Add `apps/android/README.md` with:  
   * AI Studio link.  
   * Android Studio import/run instructions.  
   * USB and wireless ADB.  
   * Galaxy S24 Ultra deployment.  
   * Android Studio Device Mirroring.  
   * Teams window sharing.  
   * [scrcpy](https://github.com/Genymobile/scrcpy) fallback for Linux/Windows.  
   * Privacy checklist: Do Not Disturb, hidden notification previews, and no personal apps visible.  
8. Device Streaming remains an emergency fallback, not the normal workflow.  
9. Copy the official Android sample source into `apps/android-sentry-reference`.  
10. Preserve its source/resources and refactor only the standalone build boundary:  
    * Replace `projects.sentry*` with released Maven artifacts.  
    * Replace root `Config` and version-catalog dependencies.  
    * Remove SDK-development-only scripts and signing assumptions.  
    * Keep relevant Compose, OkHttp, SQLite, replay, profiling, and native examples.  
11. The primary Android Sentry project receives events only from `apps/android`; the reference app uses no DSN by default to avoid project pollution.  
12. If the AI Studio source is not supplied before the P0 integration cutoff, use the shared AI Studio app, existing Sentry events, and prepared guide without blocking the course.

### **Phase 6 — iOS reference**

* Inspect the current `sentry-cocoa` samples and pin an upstream commit.  
* Correct the previously duplicated SwiftUI URL and identify the actual UIKit and SwiftUI sample paths.  
* Copy the smallest complete UIKit Swift sample plus required shared/XcodeGen/SPM files into `apps/ios-sentry-reference`.  
* Document SDK setup, dSYM upload, release/environment, logs, tracing, feedback, replay availability, privacy, and source symbolication.  
* Clearly label it “not locally built on this Windows/Linux preparation environment.”  
* Link to upstream CI/source as provenance; do not claim runtime verification.

### **Phase 7 — Practical exercises and trainer runbook**

Create exercises with participant instructions, timebox, success criteria, hints, and collapsed solution/explanation:

1. Sentry organization login, project navigation, environment filtering, and participant aliases.  
2. Triage a PolicyLab issue and identify missing context.  
3. Diagnose the hidden quote-calculation bug from stack trace, breadcrumbs, tags, release, and suspect commit.  
4. Add safe custom tags/context while avoiding PII.  
5. Compare raw/minified versus symbolicated/deobfuscated events.  
6. Analyze release trend before and after the fix.  
7. Trace a slow Angular/Android → Spring request and identify the slow span.  
8. Design a data-scrubbing rule for synthetic policyholder fields.  
9. Create a `[TRAINING participant-N]` alert rule and avoid alert fatigue.  
10. Filter Android events by device model, OS, release, environment, and custom app configuration.  
11. Submit user feedback and correlate it with an event.  
12. Perform a final configuration audit and select five production-readiness improvements.

Delivery format:

* Five minutes of individual analysis followed by round-robin discussion.  
* Every coding exercise has a zero-install Sentry-UI alternative.  
* Project initialization is optional/post-course material, not a required live exercise.  
* Trainer runbook includes exact pages to open, event IDs/releases to prepare, fallback screenshots, expected answers, and time limits.

## **3\. Final one-day agenda**

Use exactly 09:00–17:00 Europe/Warsaw:

* 09:00–09:20 — Welcome, access, workflow, and preflight.  
* 09:20–10:10 — Sentry fundamentals and issue triage.  
* 10:10–11:00 — Projects, SDK configuration, custom context, and privacy.  
* 11:00–11:15 — Break.  
* 11:15–12:00 — Angular/Spring onboarding and practical instrumentation.  
* 12:00–13:00 — Hidden-bug triage exercise.  
* 13:00–13:30 — Lunch.  
* 13:30 — Display survey reminder:  
  * [Survey](https://sages.link/848961)  
  * [QR code](https://sages.link/848961/qrcode)  
* 13:30–14:30 — Releases, source maps, source context, R8/ProGuard, native symbols, and dSYMs.  
* 14:30–15:30 — Distributed tracing, logs, Replay, and performance exercise.  
* 15:30–15:40 — Break.  
* 15:40–16:15 — Alerts, trends, ownership, Jira/Bitbucket workflow, and noise reduction.  
* 16:15–16:40 — Android/mobile diagnostics demonstration.  
* 16:40–16:50 — Short Seer/MCP/AI overview.  
* 16:50–17:00 — Deployment checklist, Q\&A, and next steps.

## **4\. CI/CD, deployment, and security**

### **GitHub Actions**

* `.github/workflows/ci.yml`:  
  * Angular tests/build.  
  * Spring tests/build.  
  * Android debug build when source is available.  
  * Path-filter jobs.  
  * No Sentry secrets on pull requests.  
* `.github/workflows/release.yml`:  
  * Trigger on `main` and manual dispatch.  
  * Create Sentry releases.  
  * Upload Angular source maps and Spring source context.  
  * Record commits and deploy metadata.  
  * Build the Docker Compose deployment.  
  * Trigger Coolify through a protected webhook.  
  * Poll the public health endpoint before marking the Sentry deploy successful.  
* Use a GitHub `training` environment with optional manual approval.  
* Store `SENTRY_AUTH_TOKEN` and Coolify credentials as environment secrets.  
* Create a Sentry internal-integration token limited to `org:ci` plus required read access. Sentry documents `org:ci` for releases and artifact uploads. [Sentry permissions](https://docs.sentry.io/api/permissions/)  
* Fork PRs must remain unable to access secrets. [GitHub secret behavior](https://docs.github.com/en/code-security/reference/secret-security/secret-types)

### **Coolify deployment**

* Root `compose.yaml` runs:  
  * Nginx/static Angular gateway.  
  * Spring Boot API on the internal network.  
* Nginx serves Angular and proxies `/api` to Spring, avoiding public backend exposure and unnecessary CORS complexity.  
* Use the Coolify-generated HTTPS URL; write it into runtime configuration after provisioning.  
* Do not use the machine’s read-only Coolify MCP token for deployment.  
* Keep Sentry auth tokens out of runtime images.

### **Bitbucket and Jenkins**

* Create validated examples under `course-materials/ci-cd/`.  
* Bitbucket example uses a secured repository/deployment variable for `SENTRY_AUTH_TOKEN`. [Atlassian secured variables](https://support.atlassian.com/bitbucket-cloud/docs/variables-and-secrets/)  
* Jenkins example uses credentials binding, never plaintext environment configuration.  
* Demonstrate GitHub live; discuss equivalent Bitbucket/Jenkins stages.  
* A Bitbucket mirror and Sentry Bitbucket integration are optional after P0 readiness.

### **Security and cleanup**

* Configure Sentry rate limits for public training DSNs.  
* Rotate/revoke DSNs if abused after the course.  
* Revoke CI tokens and Coolify webhook credentials when the training environment is retired.  
* Disable or remove participant-created alerts.  
* Remove/downgrade temporary Sentry memberships after the agreed trial period.  
* Run a final repository scan for `sntrys_`, auth headers, private keys, `.sentryclirc`, and unexpected environment files.

## **5\. Interactive HTML and verification**

### **Markdown review gate**

* Complete and link-check all Markdown first.  
* Provide the trainer with a manual validation checklist.  
* Do not generate final HTML until the trainer confirms content accuracy.

### **HTML materials**

Create `course-materials/slides/index.html` with local CSS/JavaScript and no CDN dependency:

* Keyboard navigation, progress indicator, deep links, print styling, responsive Teams presentation layout, and accessible contrast.  
* Agenda clock using `Europe/Warsaw`, not browser-local time.  
* Default course date: 2026-06-30.  
* In-page reminders at 11:00, 13:00, 13:30, and 15:30 Polish time.  
* Snooze/dismiss controls and optional sound/browser notification.  
* Survey prompt and locally stored QR asset at 13:30.  
* Exercise timers, trainer notes, glossary links, code snippets, source links, and fallback instructions.  
* Query-controlled test clock so reminder behavior can be tested without waiting for real time.  
* No external analytics or participant tracking.

### **Automated verification**

* Angular: unit tests and production build with source maps.  
* Spring: unit/controller tests, quote-scenario tests, boot JAR build, and health check.  
* Contract test: Angular request/response matches the Spring quote API.  
* Android: unit tests where present, `assembleDebug`, `assembleRelease`, and mapping-generation check.  
* Official Android reference: standalone Gradle sync and debug build.  
* iOS: static project/provenance validation only.  
* Docker Compose: local build, startup, `/actuator/health`, Angular page, and proxied `/api/quotes`.  
* CI: PR workflow without secrets; main release workflow with protected secrets.  
* Slides: navigation, Warsaw-time reminders, survey links, QR rendering, print layout, and offline operation.  
* Markdown: relative images, internal links, external official-source links, and no broken repository paths.  
* Security: secret scan and confirmation that DSNs are the only public Sentry credentials.

### **Manual Sentry acceptance checklist**

Verify in DevPowers:

* Angular error resolves to original TypeScript source.  
* Spring stack trace shows source context.  
* Angular → Spring trace is connected.  
* Logs and breadcrumbs correlate with the issue/trace.  
* Release and environment filters work.  
* Buggy and fixed releases show the expected trend.  
* Custom fields are searchable.  
* Synthetic sensitive data is scrubbed.  
* Android event contains real Galaxy S24 device context.  
* Android filtering by device/release/configuration works.  
* R8/ProGuard mapping produces a readable release stack trace.  
* User feedback appears and links to the event.  
* Alert rule triggers without generating uncontrolled noise.  
* GitHub integration shows commits/suspect commits where supported.

### **Final rehearsal and commits**

Use small, reviewable commits:

1. Initial materials, onboarding images, README files, survey, agenda, and agent instructions.  
2. Current-source research register, glossary, and best-practice guides.  
3. Spring Boot 4 API and tests.  
4. Angular app, source maps, and tracing.  
5. Exercises and seeded buggy/fixed releases.  
6. Android AI Studio app review and backend integration.  
7. Official Android and iOS references.  
8. GitHub Actions, Docker Compose, and deployment.  
9. Validated Bitbucket/Jenkins examples.  
10. Final reviewed HTML slides and trainer runbook.

Before declaring readiness, perform one timed rehearsal using the deployed app, Sentry UI, S24 mirroring, Teams screen sharing, and every fallback path.

**P0 readiness requires:** agenda/material index, Angular and Spring builds, deployed or locally runnable PolicyLab, seeded Sentry issues/traces/releases, core exercises, Android demo access, trainer runbook, and HTML presentation.

**P1 additions:** standalone official Android reference, copied iOS sample, executable Bitbucket mirror, Jenkins example, and deeper optional guides. P1 work must never jeopardize P0 readiness.

