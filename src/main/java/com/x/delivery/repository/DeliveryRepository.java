package com.x.delivery.repository;
import com.x.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DeliveryRepository extends JpaRepository<Delivery, Long> { }
