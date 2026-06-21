package com.swiftride.paymentservice.Repositories;

import com.swiftride.paymentservice.Entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByRideId(String rideId);
    Optional<Payment> findByOrderId(String rideId);
}
