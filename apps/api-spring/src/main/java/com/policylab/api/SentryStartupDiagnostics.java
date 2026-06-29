package com.policylab.api;

import java.util.Map;
import java.util.TreeMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Pure diagnostic. Prints to {@code System.out} (NOT through Logback so Logback
 * level configuration cannot hide it) what Sentry-relevant inputs are visible
 * to Spring BEFORE the auto-configuration phase runs.
 *
 * <p>Purpose: definitively answer "is SENTRY_DSN env reaching the JVM?" and
 * "what value does Spring resolve {@code sentry.dsn} property to?". The Sentry
 * Spring Boot starter's {@code @ConditionalOnProperty("sentry.dsn")} gate uses
 * exactly this property; if it resolves to empty the whole integration silently
 * never loads. Without this diagnostic we are guessing.
 *
 * <p>Wired via {@code META-INF/spring.factories}.
 */
public class SentryStartupDiagnostics implements EnvironmentPostProcessor {

  @Override
  public void postProcessEnvironment(
      ConfigurableEnvironment environment, SpringApplication application) {
    System.out.println("============================================================");
    System.out.println("  PolicyLab Sentry startup diagnostics (pre-autoconfig)");
    System.out.println("============================================================");

    // 1) Direct OS env vars containing SENTRY in the name.
    Map<String, String> env = new TreeMap<>(System.getenv());
    System.out.println("OS env vars matching *SENTRY*:");
    int sentryEnvCount = 0;
    for (Map.Entry<String, String> e : env.entrySet()) {
      if (e.getKey().toUpperCase().contains("SENTRY")) {
        sentryEnvCount++;
        String value = e.getValue();
        int len = value == null ? 0 : value.length();
        String summary;
        if (value == null) {
          summary = "null";
        } else if (value.isEmpty()) {
          summary = "EMPTY STRING";
        } else if ("SENTRY_DSN".equals(e.getKey())) {
          // Show DSN host and project id so we can verify which Sentry project
          // the container is pointed at, without printing the public key.
          summary =
              "len=" + len + ", looks like DSN: " + (value.contains("@") ? "yes" : "NO @ found");
        } else {
          summary = "len=" + len + ", value=" + value;
        }
        System.out.println("  " + e.getKey() + " => " + summary);
      }
    }
    if (sentryEnvCount == 0) {
      System.out.println("  (none)");
    }

    // 2) What does Spring resolve "sentry.dsn" to AFTER property placeholders?
    // This is the value the Sentry auto-config conditional reads.
    String resolvedDsn = environment.getProperty("sentry.dsn");
    System.out.println("Resolved Spring property sentry.dsn:");
    if (resolvedDsn == null) {
      System.out.println("  ABSENT (null)");
    } else if (resolvedDsn.isEmpty()) {
      System.out.println("  EMPTY STRING -- @ConditionalOnProperty will likely SKIP the SDK");
    } else {
      System.out.println(
          "  PRESENT, len=" + resolvedDsn.length() + ", has @ = " + resolvedDsn.contains("@"));
    }

    // 3) Show which property sources Spring is using, in order. Helps debug
    // if a profile-specific file or override is winning.
    System.out.println("Spring property sources (in priority order):");
    environment.getPropertySources().forEach(ps -> System.out.println("  - " + ps.getName()));

    System.out.println("============================================================");
  }
}
