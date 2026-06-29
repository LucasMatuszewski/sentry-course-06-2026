# Trainer content review gate

Complete this checklist before treating the HTML slides as final.

## Accuracy

- [ ] Sentry UI labels and navigation match the DevPowers organization on 2026-06-30.
- [ ] Angular, Spring Boot, Android, iOS, Gradle plugin, and CLI versions match the executable examples.
- [ ] Every production recommendation is clearly separated from 100% training sampling/debug settings.
- [ ] DSN and auth-token explanations are unambiguous.
- [ ] Source maps, source context, R8/ProGuard mappings, native symbols, and dSYMs are distinguished correctly.
- [ ] No material implies that iOS was built or demonstrated on Windows/Linux.

## Privacy and enterprise fit

- [ ] No client name, branding, URL, system detail, or real data appears.
- [ ] All PolicyLab values are visibly synthetic.
- [ ] `send-default-pii` is disabled by default.
- [ ] User identifiers are pseudonymous.
- [ ] Request bodies, postal codes, authorization headers, cookies, and feedback fields are covered by the privacy discussion.
- [ ] Sentry project-level scrubbing and SDK `beforeSend` controls are both explained.

## Workshop readiness

- [ ] All five participants can access the required Sentry projects.
- [ ] Prepared issue/event links open with project-level permissions.
- [ ] Buggy and fixed releases exist and display the intended trend.
- [ ] At least one Angular → Spring trace is available.
- [ ] Android events contain useful device context and custom fields.
- [ ] Zero-install paths work.
- [ ] Survey and QR links work.
- [ ] Break reminders use Europe/Warsaw time.
- [ ] Each exercise fits its stated timebox.

## Fallbacks

- [ ] Static screenshots/event IDs are available if live ingestion is delayed.
- [ ] The Angular/Spring demo works locally if deployment is unavailable.
- [ ] The Google AI Studio Android link opens.
- [ ] Galaxy S24 mirroring or scrcpy has been rehearsed.
- [ ] The course remains coherent if Android, Bitbucket, Jira, Seer, or Replay is unavailable.

Approval recorded by: ____________________  
Date/time: ____________________
