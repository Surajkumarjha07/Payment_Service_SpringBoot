package com.swiftride.paymentservice.Services;

import com.razorpay.RazorpayClient;
import com.swiftride.paymentservice.DTOs.PaymentResponse;
import com.swiftride.paymentservice.factories.PaymentFactory;
import com.swiftride.paymentservice.strategies.interfaces.PaymentStrategy;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentFactory paymentFactory;

    public PaymentService(RazorpayClient razorpayClient, PaymentFactory paymentFactory) {
        this.paymentFactory = paymentFactory;
    }

    public PaymentResponse createOrder (
            String userId,
            Double fare,
            String rideId,
            String captainId,
            String provider,
            String idempotencyKey
    ) {
        PaymentStrategy strategy = paymentFactory.create(provider);

        String fetchedProvider = strategy.getProviderName();

        System.out.println("PROVIDER NAME::::::::: " + fetchedProvider);

        return strategy.createOrder(userId, fare, rideId, captainId);
    }
}
