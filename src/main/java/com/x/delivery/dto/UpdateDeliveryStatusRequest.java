package com.x.delivery.dto;
import com.x.delivery.entity.DeliveryStatus;
import jakarta.validation.constraints.NotNull;
public record UpdateDeliveryStatusRequest(@NotNull DeliveryStatus status, String failureReason) { }
