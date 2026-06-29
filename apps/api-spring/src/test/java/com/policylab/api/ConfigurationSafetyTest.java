package com.policylab.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ConfigurationSafetyTest {

  @Test
  void sentryDefaultsArePrivacySafe() throws IOException {
    try (var stream = getClass().getResourceAsStream("/application.properties")) {
      assertThat(stream).isNotNull();
      String properties = new String(stream.readAllBytes(), StandardCharsets.UTF_8);

      assertThat(properties)
          .contains("sentry.send-default-pii=false")
          .contains("sentry.max-request-body-size=none")
          .contains("sentry.logs.enabled=true")
          .contains("sentry.environment=${SENTRY_ENVIRONMENT:training}")
          .contains("sentry.dsn=${SENTRY_DSN:}");
    }
  }
}
