package com.policylab.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.sentry.Hint;
import io.sentry.SentryEvent;
import io.sentry.SentryOptions;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
    properties = {
      "demo.scenarios.enabled=true",
      "participant.alias=participant-07",
      "sentry.dsn=http://public@127.0.0.1:1/1",
      "sentry.traces-sample-rate=0",
      "sentry.logs.enabled=false"
    })
@AutoConfigureMockMvc
@Import(SentryCaptureIntegrationTest.CaptureConfiguration.class)
class SentryCaptureIntegrationTest {

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
  @Autowired private CountingBeforeSend beforeSend;

  @BeforeEach
  void resetCapturedEvents() {
    beforeSend.reset();
  }

  @Test
  void expectedValidationResponsesAreNotSentToSentry() throws Exception {
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
        .andExpect(status().isBadRequest());
    mockMvc
        .perform(
            post("/api/quotes")
                .header("X-Demo-Scenario", "validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST))
        .andExpect(status().isBadRequest());

    assertThat(beforeSend.count()).isZero();
  }

  @Test
  void serverErrorIsSentToSentryExactlyOnce() throws Exception {
    mockMvc
        .perform(
            post("/api/quotes")
                .header("X-Demo-Scenario", "server-error")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST))
        .andExpect(status().isInternalServerError());

    assertThat(beforeSend.count()).isOne();
  }

  @TestConfiguration(proxyBeanMethods = false)
  static class CaptureConfiguration {

    @Bean
    CountingBeforeSend beforeSendCallback() {
      return new CountingBeforeSend();
    }
  }

  static final class CountingBeforeSend implements SentryOptions.BeforeSendCallback {

    private final AtomicInteger count = new AtomicInteger();

    @Override
    public SentryEvent execute(SentryEvent event, Hint hint) {
      count.incrementAndGet();
      return null;
    }

    int count() {
      return count.get();
    }

    void reset() {
      count.set(0);
    }
  }
}
