# Setup: Angular

Use the current Sentry wizard on a disposable branch; review every change:

```powershell
npx @sentry/wizard@latest -i angular
```

The wizard is preferred because Angular bootstrap, router integration, SDK exports, and bundler setup evolve. The generated diff and current official docs are the source of truth.

## Runtime contract

Configure values from the deployment environment:

```ts
import * as Sentry from "@sentry/angular";

Sentry.init({
  dsn: window.__RUNTIME_CONFIG__.sentryDsn,
  environment: window.__RUNTIME_CONFIG__.environment,
  release: window.__RUNTIME_CONFIG__.release,
  integrations: [
    Sentry.browserTracingIntegration(),
    Sentry.replayIntegration(),
  ],
  tracesSampleRate: 0.1,
  replaysSessionSampleRate: 0.01,
  replaysOnErrorSampleRate: 1.0,
  sendDefaultPii: false,
});
```

This shows the configuration shape, not a drop-in file. Preserve the wizard's Angular error handler, router/trace provider, and initialization placement. Use the exact exports shown for the installed SDK.

## Distributed tracing

Restrict propagation to PolicyLab:

```ts
tracePropagationTargets: [
  "localhost",
  /^https:\/\/api\.policylab\.example\.invalid\//
]
```

The Spring API must allow the Sentry trace headers in CORS. Never use a broad pattern that sends tracing context to unrelated domains.

## Safe context

```ts
Sentry.setUser({ id: "user-17" });
Sentry.setTag("quote.channel", "broker");
Sentry.setContext("quote", {
  quote_ref: "Q-1042",
  product_code: "MOTOR_STANDARD"
});
```

Do not send form values, tokens, customer names, email, addresses, or full URLs with secrets.

## Source maps and verification

Use the wizard-selected Angular/bundler integration to inject debug IDs and upload source maps during the production CI build. Keep `SENTRY_AUTH_TOKEN` only in CI.

Verification:

1. build in production mode;
2. upload artifacts before deployment;
3. deploy unchanged output;
4. trigger a synthetic error;
5. confirm readable TypeScript frames, `release`, `environment`, trace, and masked replay;
6. run `sentry-cli sourcemaps explain <event-id>` if mapping fails.

## Zero-install workshop path

If local setup is unavailable, inspect the trainer-provided Angular event and identify initialization, release/environment, trace headers, safe context, and source-map evidence. No package installation is required.

## Sources and validation

- [Sentry for Angular](https://docs.sentry.io/platforms/javascript/guides/angular/)
- [Angular source maps](https://docs.sentry.io/platforms/javascript/guides/angular/sourcemaps/)
- [Angular tracing](https://docs.sentry.io/platforms/javascript/guides/angular/tracing/)
- [Angular Session Replay privacy](https://docs.sentry.io/platforms/javascript/guides/angular/session-replay/privacy/)

Validation: official Angular SDK, tracing, replay, and source-map pages checked 2026-06-29. Run the current wizard instead of pinning workshop text to a stale package version.
