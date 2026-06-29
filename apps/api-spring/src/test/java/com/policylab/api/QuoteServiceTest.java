package com.policylab.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.policylab.api.quote.QuoteRequest;
import com.policylab.api.quote.QuoteService;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class QuoteServiceTest {

  private final QuoteService quoteService =
      new QuoteService(Clock.fixed(Instant.parse("2026-06-29T12:00:00Z"), ZoneOffset.UTC));

  @Test
  void calculatesDeterministicPremiumAndRiskBand() {
    QuoteRequest request = new QuoteRequest("car", 2022, "comprehensive", "SW1A 1AA", 38);

    var quote = quoteService.createQuote(request);

    assertThat(quote.annualPremium()).isEqualByComparingTo(new BigDecimal("675.00"));
    assertThat(quote.currency()).isEqualTo("GBP");
    assertThat(quote.riskBand()).isEqualTo("MEDIUM");
    assertThat(quote.quoteId()).isNotNull();
  }

  @Test
  void classifiesYoungDriversAsHighRisk() {
    QuoteRequest request = new QuoteRequest("van", 2010, "comprehensive", "EH1 1YZ", 20);

    var quote = quoteService.createQuote(request);

    assertThat(quote.riskBand()).isEqualTo("HIGH");
  }
}
