import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import * as Sentry from '@sentry/angular';

import { QuoteApiService, DemoScenario } from './quote-api.service';

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
        error: (err) => this.status.set(`Błąd: ${err?.status ?? err?.message ?? 'unknown'}`),
      });
  }
}
