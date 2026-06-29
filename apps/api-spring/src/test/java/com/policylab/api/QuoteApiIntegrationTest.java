package com.policylab.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
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
      "demo.scenarios.enabled=false",
      "participant.alias=participant-07",
      "sentry.dsn=",
      "sentry.traces-sample-rate=0"
    })
@AutoConfigureMockMvc
class QuoteApiIntegrationTest {

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
  void returnsQuoteForValidRequest() throws Exception {
    mockMvc
        .perform(post("/api/quotes").contentType(MediaType.APPLICATION_JSON).content(VALID_REQUEST))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.quoteId").isString())
        .andExpect(jsonPath("$.annualPremium").isNumber())
        .andExpect(jsonPath("$.currency").value("PLN"))
        .andExpect(jsonPath("$.riskBand").value("MEDIUM"));

    verify(telemetry)
        .setSafeTags("comprehensive", "car", anyString(), "participant-07");
  }

  @Test
  void rejectsInvalidPayloadWithoutIntentionalCapture() throws Exception {
    String invalidRequest =
        """
        {
          "vehicleType": "spaceship",
          "manufactureYear": 1800,
          "coverage": "",
          "postalCode": "",
          "driverAge": 12
        }
        """;

    mockMvc
        .perform(post("/api/quotes").contentType(MediaType.APPLICATION_JSON).content(invalidRequest))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.title").value("Invalid quote request"));

    verify(telemetry, never()).setSafeTags(any(), any(), any(), any());
  }

  @Test
  void ignoresDemoScenarioWhenGateIsDisabled() throws Exception {
    mockMvc
        .perform(
            post("/api/quotes")
                .header("X-Demo-Scenario", "server-error")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST))
        .andExpect(status().isOk());

    verify(telemetry).setSafeTags(any(), any(), any(), any());
  }
}
