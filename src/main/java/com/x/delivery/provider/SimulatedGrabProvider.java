package com.x.delivery.provider;
import com.x.delivery.dto.CreateDeliveryRequest;
import com.x.delivery.entity.DeliveryProviderType;
import org.springframework.stereotype.Component;
@Component
public class SimulatedGrabProvider implements DeliveryProvider {
    public DeliveryProviderType providerType() { return DeliveryProviderType.GRAB; }
    public DeliveryBooking createBooking(CreateDeliveryRequest request) { return new DeliveryBooking("GRAB-SIM-" + request.orderId() + "-" + request.quoteId(), "Simulated Grab Rider", "000000000"); }
}
