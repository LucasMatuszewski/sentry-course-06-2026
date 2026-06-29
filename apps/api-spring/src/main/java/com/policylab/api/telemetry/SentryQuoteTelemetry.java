package com.policylab.api.telemetry;

import io.sentry.ISpan;
import io.sentry.Sentry;
import org.springframework.stereotype.Component;

@Component
public class SentryQuoteTelemetry implements QuoteTelemetry {

  @Override
  public void setSafeTags(
      String coverage, String vehicleType, String riskBand, String participantAlias) {
    Sentry.configureScope(
        scope -> {
          scope.setTag("coverage", coverage);
          scope.setTag("vehicle_type", vehicleType);
          scope.setTag("risk_band", riskBand);
          scope.setTag("participant_alias", participantAlias);
        });
  }

  @Override
  public void runSlowRiskEvaluation() {
    ISpan parentSpan = Sentry.getSpan();
    ISpan childSpan =
        parentSpan == null ? null : parentSpan.startChild("quote.risk-evaluation");
    try {
      Thread.sleep(750L);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Slow quote demonstration was interrupted", exception);
    } finally {
      if (childSpan != null) {
        childSpan.finish();
      }
    }
  }
}
