import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

export type DemoScenario = 'normal' | 'server-error' | 'slow' | 'validation' | 'pii';

export interface QuoteRequest {
  vehicleType: string;
  manufactureYear: number;
  coverage: string;
  postalCode: string;
  driverAge: number;
}

export interface QuoteResponse {
  quoteId: string;
  annualPremium: number;
  currency: string;
  riskBand: string;
}

@Injectable({ providedIn: 'root' })
export class QuoteApiService {
  private readonly http = inject(HttpClient);

  createQuote(request: QuoteRequest, scenario: DemoScenario = 'normal'): Observable<QuoteResponse> {
    const headers =
      scenario === 'normal'
        ? new HttpHeaders()
        : new HttpHeaders({ 'X-Demo-Scenario': scenario });
    return this.http.post<QuoteResponse>('/api/quotes', request, { headers });
  }
}
