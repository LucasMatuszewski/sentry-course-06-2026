import { bootstrapApplication } from '@angular/platform-browser';
import * as Sentry from '@sentry/angular';

import { appConfig } from './app/app.config';
import { App } from './app/app';

Sentry.init({
  dsn: 'https://05129b1196d6b979d74c876edc341744@o4511643784511488.ingest.de.sentry.io/4511646810570832',
  environment: 'training',
  release: 'angular@0.1.0',
  sendDefaultPii: false,
  integrations: [
    Sentry.browserTracingIntegration(),
    Sentry.replayIntegration({
      maskAllText: true,
      blockAllMedia: true,
    }),
    // Browser profiling — flame graphs sampled per-transaction.
    // Cost overhead is ~10 KB JS + per-tx CPU sampling during recording.
    // 1.0 here for the live demo; drop to 0.05 in production.
    Sentry.browserProfilingIntegration(),
  ],
  tracesSampleRate: 1.0,
  // profilesSampleRate is a fraction of transactions that get profiled.
  profilesSampleRate: 1.0,
  tracePropagationTargets: ['localhost', /^\/api/],
  replaysSessionSampleRate: 0.1,
  replaysOnErrorSampleRate: 1.0,
  enableLogs: true,
});

bootstrapApplication(App, appConfig).catch((err) => console.error(err));
