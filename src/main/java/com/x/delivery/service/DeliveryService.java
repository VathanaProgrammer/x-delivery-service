package com.x.delivery.service;

import com.x.delivery.dto.*;
import com.x.delivery.entity.*;
import com.x.delivery.provider.*;
import com.x.delivery.repository.DeliveryRepository;
import com.x.delivery.repository.DeliveryQuoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryQuoteRepository quoteRepository;
    private final Map<DeliveryProviderType, DeliveryProvider> providers;
    public DeliveryService(DeliveryRepository deliveryRepository, DeliveryQuoteRepository quoteRepository, List<DeliveryProvider> providers) {
        this.deliveryRepository = deliveryRepository;
        this.quoteRepository = quoteRepository;
        this.providers = providers.stream().collect(Collectors.toMap(DeliveryProvider::providerType, Function.identity()));
    }
    @Transactional
    public DeliveryResponse create(CreateDeliveryRequest request) {
        DeliveryQuote quote = quoteRepository.findById(request.quoteId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery quote not found"));
        if (quote.getStatus() != DeliveryQuoteStatus.ACTIVE || quote.getExpiresAt().isBefore(LocalDateTime.now())) throw new ResponseStatusException(HttpStatus.CONFLICT, "Delivery quote has expired or was already selected");
        DeliveryProvider provider = providers.get(quote.getProviderType());
        if (provider == null) throw new ResponseStatusException(HttpStatus.CONFLICT, quote.getProviderType() + " is not enabled");
        DeliveryBooking booking = provider.createBooking(request);
        quote.setStatus(DeliveryQuoteStatus.SELECTED);
        Delivery delivery = deliveryRepository.save(Delivery.builder().orderId(request.orderId()).storeId(quote.getStoreId()).quoteId(quote.getId())
                .providerType(quote.getProviderType()).status(DeliveryStatus.ASSIGNED).trackingNumber(booking.trackingNumber())
                .riderName(booking.riderName()).riderPhone(booking.riderPhone()).deliveryFee(quote.getQuotedFee())
                .courierCost(quote.getQuotedFee()).codAmount(request.codAmount()).build());
        return toResponse(delivery);
    }
    @Transactional(readOnly = true)
    public DeliveryResponse get(Long id) { return deliveryRepository.findById(id).map(this::toResponse)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found")); }
    @Transactional
    public DeliveryResponse updateStatus(Long id, UpdateDeliveryStatusRequest request) {
        Delivery delivery = deliveryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found"));
        delivery.setStatus(request.status());
        delivery.setFailureReason(request.failureReason());
        return toResponse(deliveryRepository.save(delivery));
    }
    @Transactional
    public DeliveryResponse recordRemittance(Long id, RecordRemittanceRequest request) {
        Delivery delivery = deliveryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found"));
        if (delivery.getStatus() != DeliveryStatus.DELIVERED) throw new ResponseStatusException(HttpStatus.CONFLICT, "COD can be remitted only after delivery");
        delivery.setRemittedAmount(request.remittedAmount());
        delivery.setRemittedAt(LocalDateTime.now());
        return toResponse(deliveryRepository.save(delivery));
    }
    private DeliveryResponse toResponse(Delivery d) { return new DeliveryResponse(d.getId(), d.getOrderId(), d.getStoreId(), d.getProviderType(), d.getStatus(), d.getTrackingNumber(), d.getRiderName(), d.getRiderPhone(), d.getDeliveryFee(), d.getCourierCost(), d.getCodAmount(), d.getRemittedAmount()); }
}
