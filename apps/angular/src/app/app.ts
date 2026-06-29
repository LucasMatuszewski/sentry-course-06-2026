import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import * as Sentry from '@sentry/angular';

import { QuoteApiService, DemoScenario, QuoteResponse } from './quote-api.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('PolicyLab');
  protected readonly status = signal<string>('Gotowy.');

  private readonly quotes = inject(QuoteApiService);

  throwTestError(): void {
    Sentry.logger.info(
      Sentry.logger.fmt`Participant ${'sentry-test'} triggered test error button`,
      { action: 'test_error_button_click' },
    );
    throw new Error('Sentry Test Error from PolicyLab Angular');
  }

  requestQuote(scenario: DemoScenario): void {
    this.status.set(`Wysyłam zapytanie o wycenę (${scenario})...`);
    this.quotes
      .createQuote(
        {
          vehicleType: 'car',
          manufactureYear: 2018,
          coverage: 'comprehensive',
          postalCode: '00-001',
          driverAge: 35,
        },
        scenario,
      )
      .subscribe({
        next: (quote) =>
          this.status.set(
            `Wycena ${quote.quoteId}: ${quote.annualPremium} ${quote.currency} (${quote.riskBand})`,
          ),
        error: (err) => {
          // Capture every API failure as a Sentry event so the trainer can
          // demo "FE error + linked BE issue + shared trace" without
          // relying on the Spring SDK doing the right thing. The error
          // carries the same sentry-trace header the request was tagged
          // with, so this event joins the same trace as the Spring event.
          Sentry.captureException(err, {
            tags: { 'demo.scenario': scenario, 'api.endpoint': '/api/quotes' },
            contexts: {
              quote_request: {
                scenario,
                status: err?.status,
                message: err?.message,
              },
            },
          });
          this.status.set(`Błąd: ${err?.status ?? err?.message ?? 'unknown'}`);
        },
      });
  }

  /**
   * Stress test — fires N parallel quote requests inside a single Sentry
   * transaction. In Sentry → Insights → Backend you can watch p95 and
   * throughput spike. With profiling on, each transaction has a flame
   * graph attached.
   */
  stressTest(): void {
    const total = 8;
    this.status.set(`Stress test: ${total} równoległych wycen...`);
    Sentry.startSpan(
      { name: 'PolicyLab stress test', op: 'demo.stress', attributes: { total } },
      async (span) => {
        const start = performance.now();
        const promises: Promise<QuoteResponse>[] = [];
        for (let i = 0; i < total; i++) {
          // Mix scenarios so the trace is interesting.
          const scenario: DemoScenario = i % 4 === 0 ? 'slow' : 'normal';
          promises.push(
            new Promise((resolve, reject) => {
              this.quotes
                .createQuote(
                  {
                    vehicleType: i % 2 ? 'motorcycle' : 'car',
                    manufactureYear: 2015 + (i % 8),
                    coverage: i % 3 ? 'comprehensive' : 'third-party',
                    postalCode: '00-001',
                    driverAge: 25 + (i % 40),
                  },
                  scenario,
                )
                .subscribe({ next: resolve, error: reject });
            }),
          );
        }
        const results = await Promise.allSettled(promises);
        const ok = results.filter((r) => r.status === 'fulfilled').length;
        const elapsed = Math.round(performance.now() - start);
        // Sentry "measurements" replace the deprecated Metrics API.
        // They are attached to the transaction and visible in Sentry UI.
        span?.setAttribute('quotes.ok', ok);
        span?.setAttribute('quotes.failed', total - ok);
        span?.setAttribute('elapsed.ms', elapsed);
        Sentry.logger.info(
          Sentry.logger.fmt`Stress test finished: ${String(ok)}/${String(total)} ok in ${String(
            elapsed,
          )}ms`,
          { action: 'stress_test_finished', ok, failed: total - ok, elapsed_ms: elapsed },
        );
        this.status.set(`Stress test: ${ok}/${total} OK w ${elapsed} ms`);
      },
    );
  }

  /**
   * Nested calls — 3 sequential quote requests, each child span of the
   * outer transaction. Visualizes a chain in the Sentry waterfall and
   * mimics a real N+1-like pattern.
   */
  nestedCalls(): void {
    this.status.set('Łańcuch 3 wycen sekwencyjnie...');
    Sentry.startSpan(
      { name: 'PolicyLab nested chain', op: 'demo.nested' },
      async () => {
        const summaries: string[] = [];
        for (let i = 0; i < 3; i++) {
          await new Promise<void>((resolve) => {
            this.quotes
              .createQuote(
                {
                  vehicleType: 'car',
                  manufactureYear: 2020 - i,
                  coverage: 'comprehensive',
                  postalCode: '00-001',
                  driverAge: 30 + i * 5,
                },
                i === 1 ? 'slow' : 'normal',
              )
              .subscribe({
                next: (quote) => {
                  summaries.push(`${quote.quoteId} ${quote.annualPremium}`);
                  resolve();
                },
                error: () => resolve(),
              });
          });
        }
        this.status.set(`Chain: ${summaries.join(' | ')}`);
      },
    );
  }
}
