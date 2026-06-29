package com.policylab.api;

import io.sentry.Sentry;
import io.sentry.SentryLevel;
import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PolicyLabApplication {

  public static void main(String[] args) {
    // Manual Sentry init BEFORE Spring loads. The Spring Boot Sentry starter
    // also initializes the SDK, but only after Spring has booted. If Spring's
    // context refresh blows up (auto-config mismatch, bad bean, classpath
    // problem) the starter never runs and we lose the crash report.
    //
    // We init here with the same env vars the starter would read, then wrap
    // SpringApplication.run() so any throwable during boot lands in Sentry
    // before the JVM exits. The starter still runs second and takes over for
    // request-time capture; double init is documented as safe.
    initSentryEarly();
    try {
      SpringApplication.run(PolicyLabApplication.class, args);
    } catch (Throwable t) {
      Sentry.captureException(t);
      Sentry.flush(5_000);
      throw t;
    }
  }

  private static void initSentryEarly() {
    String dsn = System.getenv("SENTRY_DSN");
    if (dsn == null || dsn.isBlank()) {
      return;
    }
    Sentry.init(options -> {
      options.setDsn(dsn);
      options.setEnvironment(envOr("SENTRY_ENVIRONMENT", "training"));
      String release = System.getenv("SENTRY_RELEASE");
      if (release != null && !release.isBlank()) {
        options.setRelease(release);
      }
      options.setTracesSampleRate(parseDoubleOr("SENTRY_TRACES_SAMPLE_RATE", 0.0));
      options.setSendDefaultPii(false);
      options.setDiagnosticLevel(SentryLevel.WARNING);
      options.setTag("boot.phase", "pre-spring");
    });
  }

  private static String envOr(String key, String fallback) {
    String v = System.getenv(key);
    return (v == null || v.isBlank()) ? fallback : v;
  }

  private static double parseDoubleOr(String key, double fallback) {
    String v = System.getenv(key);
    if (v == null || v.isBlank()) return fallback;
    try {
      return Double.parseDouble(v);
    } catch (NumberFormatException e) {
      return fallback;
    }
  }

  @Bean
  Clock utcClock() {
    return Clock.systemUTC();
  }
}
