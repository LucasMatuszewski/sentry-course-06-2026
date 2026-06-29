package com.policylab.api;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.policylab.api.telemetry.QuoteTelemetry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
    properties = {
      "demo.scenarios.enabled=true",
      "participant.alias=participant-07",
      "sentry.dsn=",
      "sentry.traces-sample-rate=0"
    })
@AutoConfigureMockMvc
class DemoScenarioIntegrationTest {

  private static final String VALID_REQUEST =
      """
      {
        "vehicleType": "car",
        "manufactureYear": 2022,
        "coverage": "comprehensive",
        "postalCode": "00-001",
        "driverAge": 38
      }
      """;

  @Autowired private MockMvc mockMvc;

  @MockitoBean private QuoteTelemetry telemetry;

  @Test
  void serverErrorScenarioReturns500AndCapturesException() throws Exception {
    mockMvc
        .perform(
            post("/api/quotes")
                .header("X-Demo-Scenario", "server-error")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.title").value("Demo server error"));

  }

  @Test
  void slowScenarioCreatesCustomSpan() throws Exception {
    mockMvc
        .perform(
            post("/api/quotes")
                .header("X-Demo-Scenario", "slow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST))
        .andExpect(status().isOk());

    verify(telemetry).runSlowRiskEvaluation();
  }

  @Test
  void validationScenarioReturnsExpected400WithoutCapture() throws Exception {
    mockMvc
        .perform(
            post("/api/quotes")
                .header("X-Demo-Scenario", "validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.title").value("Demo validation error"));

  }

  @Test
  void piiScenarioStillAddsOnlyAllowListedTags() throws Exception {
    mockMvc
        .perform(
            post("/api/quotes")
                .header("X-Demo-Scenario", "pii")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST))
        .andExpect(status().isOk());

    verify(telemetry)
        .setSafeTags("comprehensive", "car", anyString(), "participant-07");
  }
}
