package com.policylab.api.telemetry;

public interface QuoteTelemetry {

  void setSafeTags(
      String coverage, String vehicleType, String riskBand, String participantAlias);

  void runSlowRiskEvaluation();
}
