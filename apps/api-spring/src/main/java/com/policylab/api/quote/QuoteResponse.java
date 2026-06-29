package com.policylab.api.quote;

import java.math.BigDecimal;
import java.util.UUID;

public record QuoteResponse(
    UUID quoteId, BigDecimal annualPremium, String currency, String riskBand) {}
