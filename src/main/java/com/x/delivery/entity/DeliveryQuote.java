package com.x.delivery.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "delivery_quotes") @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class DeliveryQuote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "store_id", nullable = false) private Long storeId;
    @Enumerated(EnumType.STRING) @Column(name = "provider_type", nullable = false, length = 32) private DeliveryProviderType providerType;
    @Column(name = "pickup_latitude", nullable = false, precision = 10, scale = 7) private BigDecimal pickupLatitude;
    @Column(name = "pickup_longitude", nullable = false, precision = 10, scale = 7) private BigDecimal pickupLongitude;
    @Column(name = "destination_latitude", nullable = false, precision = 10, scale = 7) private BigDecimal destinationLatitude;
    @Column(name = "destination_longitude", nullable = false, precision = 10, scale = 7) private BigDecimal destinationLongitude;
    @Column(name = "distance_km", nullable = false, precision = 10, scale = 2) private BigDecimal distanceKm;
    @Column(name = "quoted_fee", nullable = false, precision = 12, scale = 2) private BigDecimal quotedFee;
    @Column(name = "currency_code", nullable = false, length = 3) private String currencyCode;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 16) private DeliveryQuoteStatus status;
    @Column(name = "expires_at", nullable = false) private LocalDateTime expiresAt;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
}
