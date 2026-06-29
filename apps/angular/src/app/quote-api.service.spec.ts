import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { QuoteApiService, QuoteRequest, QuoteResponse } from './quote-api.service';

describe('QuoteApiService', () => {
  let service: QuoteApiService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(QuoteApiService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('posts the quote contract and selected demo scenario', () => {
    const request: QuoteRequest = {
      vehicleType: 'car',
      manufactureYear: 2018,
      coverage: 'comprehensive',
      postalCode: '00-001',
      driverAge: 35,
    };
    const response: QuoteResponse = {
      quoteId: 'Q-2026-0001',
      annualPremium: 1234.56,
      currency: 'PLN',
      riskBand: 'MEDIUM',
    };

    service.createQuote(request, 'slow').subscribe((quote) => expect(quote).toEqual(response));

    const pending = http.expectOne('/api/quotes');
    expect(pending.request.method).toBe('POST');
    expect(pending.request.body).toEqual(request);
    expect(pending.request.headers.get('X-Demo-Scenario')).toBe('slow');
    pending.flush(response);
  });

  it('omits the demo header for the normal production-compatible path', () => {
    const request: QuoteRequest = {
      vehicleType: 'motorcycle',
      manufactureYear: 2020,
      coverage: 'third-party',
      postalCode: '00-001',
      driverAge: 40,
    };

    service.createQuote(request, 'normal').subscribe();

    const pending = http.expectOne('/api/quotes');
    expect(pending.request.headers.has('X-Demo-Scenario')).toBeFalse();
    pending.flush({
      quoteId: 'Q-2026-0002',
      annualPremium: 800,
      currency: 'PLN',
      riskBand: 'LOW',
    });
  });
});
