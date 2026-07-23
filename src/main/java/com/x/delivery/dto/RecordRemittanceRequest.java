package com.x.delivery.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record RecordRemittanceRequest(@NotNull @PositiveOrZero BigDecimal remittedAmount) { }
