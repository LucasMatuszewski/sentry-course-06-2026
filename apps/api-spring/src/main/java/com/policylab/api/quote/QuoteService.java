package com.policylab.api.quote;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Year;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {

  private static final Map<String, BigDecimal> BASE_PREMIUM =
      Map.of(
          "car", new BigDecimal("500.00"),
          "motorcycle", new BigDecimal("350.00"),
          "van", new BigDecimal("650.00"));

  private static final Map<String, BigDecimal> COVERAGE_FACTOR =
      Map.of(
          "third-party", new BigDecimal("0.75"),
          "comprehensive", new BigDecimal("1.25"));

  private final Clock clock;

  public QuoteService(Clock clock) {
    this.clock = clock;
  }

  public QuoteResponse createQuote(QuoteRequest request) {
    String vehicleType = request.vehicleType().toLowerCase(Locale.ROOT);
    String coverage = request.coverage().toLowerCase(Locale.ROOT);
    int vehicleAge = Math.max(0, Year.now(clock).getValue() - request.manufactureYear());

    BigDecimal ageFactor =
        BigDecimal.ONE.add(
            new BigDecimal(Math.min(vehicleAge, 20)).multiply(new BigDecimal("0.02")));
    BigDecimal driverFactor = driverFactor(request.driverAge());
    BigDecimal premium =
        BASE_PREMIUM
            .get(vehicleType)
            .multiply(COVERAGE_FACTOR.get(coverage))
            .multiply(ageFactor)
            .multiply(driverFactor)
            .setScale(2, RoundingMode.HALF_UP);

    return new QuoteResponse(UUID.randomUUID(), premium, "PLN", riskBand(premium));
  }

  private BigDecimal driverFactor(int driverAge) {
    if (driverAge < 25) {
      return new BigDecimal("1.50");
    }
    if (driverAge > 70) {
      return new BigDecimal("1.30");
    }
    return BigDecimal.ONE;
  }

  private String riskBand(BigDecimal premium) {
    if (premium.compareTo(new BigDecimal("500.00")) < 0) {
      return "LOW";
    }
    if (premium.compareTo(new BigDecimal("800.00")) < 0) {
      return "MEDIUM";
    }
    return "HIGH";
  }
}
