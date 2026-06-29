package com.policylab.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.policylab.api.telemetry.SentryQuoteTelemetry;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class SentryQuoteTelemetryTest {

  @Test
  void slowRiskEvaluationRunsTheRealDelayWithoutAnActiveTransaction() {
    SentryQuoteTelemetry telemetry = new SentryQuoteTelemetry();

    long startedAt = System.nanoTime();
    telemetry.runSlowRiskEvaluation();
    Duration elapsed = Duration.ofNanos(System.nanoTime() - startedAt);

    assertThat(elapsed).isGreaterThanOrEqualTo(Duration.ofMillis(700));
  }
}
