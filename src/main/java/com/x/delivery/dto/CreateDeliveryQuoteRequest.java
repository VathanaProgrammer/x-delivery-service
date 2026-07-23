package com.x.delivery.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record CreateDeliveryQuoteRequest(@NotNull @Positive Long storeId,
        @NotNull @DecimalMin(value = "-90") @DecimalMax(value = "90") BigDecimal destinationLatitude,
        @NotNull @DecimalMin(value = "-180") @DecimalMax(value = "180") BigDecimal destinationLongitude,
        @NotBlank @Pattern(regexp = "[A-Za-z]{3}") String currencyCode) { }
