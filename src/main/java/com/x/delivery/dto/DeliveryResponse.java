package com.x.delivery.dto;
import com.x.delivery.entity.*;
import java.math.BigDecimal;
public record DeliveryResponse(Long id, Long orderId, Long storeId, DeliveryProviderType providerType,
                               DeliveryStatus status, String trackingNumber, String riderName, String riderPhone,
                               BigDecimal deliveryFee, BigDecimal courierCost, BigDecimal codAmount, BigDecimal remittedAmount) { }
