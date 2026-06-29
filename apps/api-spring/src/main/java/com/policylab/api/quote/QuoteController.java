package com.policylab.api.quote;

import com.policylab.api.telemetry.QuoteTelemetry;
import jakarta.validation.Validator;
import java.util.Locale;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuoteController.class);
  private static final Set<String> SCENARIOS =
      Set.of("normal", "server-error", "slow", "validation", "pii");

  private final QuoteService quoteService;
  private final QuoteTelemetry telemetry;
  private final Validator validator;
  private final boolean demoScenariosEnabled;
  private final String participantAlias;

  public QuoteController(
      QuoteService quoteService,
      QuoteTelemetry telemetry,
      Validator validator,
      @Value("${demo.scenarios.enabled:false}") boolean demoScenariosEnabled,
      @Value("${participant.alias:anonymous}") String participantAlias) {
    this.quoteService = quoteService;
    this.telemetry = telemetry;
    this.validator = validator;
    this.demoScenariosEnabled = demoScenariosEnabled;
    this.participantAlias = participantAlias;
  }

  @PostMapping
  public ResponseEntity<?> createQuote(
      @RequestBody QuoteRequest request,
      @RequestHeader(name = "X-Demo-Scenario", defaultValue = "normal") String scenarioHeader) {
    if (!validator.validate(request).isEmpty()) {
      return ResponseEntity.badRequest().body(ApiExceptionHandler.invalidQuoteRequest());
    }

    String scenario = activeScenario(scenarioHeader);

    if ("validation".equals(scenario)) {
      return ResponseEntity.badRequest().body(ApiExceptionHandler.demoValidationError());
    }
    if ("server-error".equals(scenario)) {
      throw new DemoServerException();
    }
    if ("slow".equals(scenario)) {
      telemetry.runSlowRiskEvaluation();
    }

    QuoteResponse response = quoteService.createQuote(request);
    String coverage = request.coverage().toLowerCase(Locale.ROOT);
    String vehicleType = request.vehicleType().toLowerCase(Locale.ROOT);
    telemetry.setSafeTags(coverage, vehicleType, response.riskBand(), participantAlias);
    LOGGER.info(
        "Quote created: coverage={}, vehicleType={}, riskBand={}, participantAlias={}",
        coverage,
        vehicleType,
        response.riskBand(),
        participantAlias);
    return ResponseEntity.ok(response);
  }

  private String activeScenario(String scenarioHeader) {
    if (!demoScenariosEnabled) {
      return "normal";
    }
    String normalized = scenarioHeader.toLowerCase(Locale.ROOT);
    return SCENARIOS.contains(normalized) ? normalized : "normal";
  }
}
