package com.policylab.api.quote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.Year;

public record QuoteRequest(
    @NotBlank
        @Pattern(
            regexp = "(?i)car|motorcycle|van",
            message = "vehicleType must be car, motorcycle, or van")
        String vehicleType,
    @Min(value = 1900, message = "manufactureYear must be 1900 or later")
        int manufactureYear,
    @NotBlank
        @Pattern(
            regexp = "(?i)third-party|comprehensive",
            message = "coverage must be third-party or comprehensive")
        String coverage,
    @NotBlank String postalCode,
    @Min(value = 17, message = "driverAge must be at least 17")
        @Max(value = 100, message = "driverAge must be at most 100")
        int driverAge) {

  @JsonIgnore
  @AssertTrue(message = "manufactureYear must not be in the future")
  public boolean isManufactureYearValid() {
    return manufactureYear <= Year.now().getValue();
  }
}
