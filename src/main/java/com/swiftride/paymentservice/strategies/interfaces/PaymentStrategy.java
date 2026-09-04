package com.swiftride.paymentservice.strategies.interfaces;

import com.swiftride.paymentservice.DTOs.PaymentResponse;

public interface PaymentStrategy {
    PaymentResponse createOrder(String userId, Double fare, String rideId, String captainId);
    String getProviderName();
}
