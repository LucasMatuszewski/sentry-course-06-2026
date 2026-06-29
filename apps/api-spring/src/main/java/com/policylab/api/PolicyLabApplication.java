package com.policylab.api;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PolicyLabApplication {

  public static void main(String[] args) {
    SpringApplication.run(PolicyLabApplication.class, args);
  }

  @Bean
  Clock utcClock() {
    return Clock.systemUTC();
  }
}
