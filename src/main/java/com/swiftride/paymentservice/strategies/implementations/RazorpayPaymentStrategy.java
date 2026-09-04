package com.swiftride.paymentservice.strategies.implementations;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.swiftride.paymentservice.DTOs.PaymentResponse;
import com.swiftride.paymentservice.Entities.Payment;
import com.swiftride.paymentservice.Enums.PaymentStatus;
import com.swiftride.paymentservice.Repositories.PaymentRepository;
import com.swiftride.paymentservice.Services.PaymentIdempotencyService;
import com.swiftride.paymentservice.strategies.interfaces.PaymentStrategy;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class RazorpayPaymentStrategy implements PaymentStrategy {

    @Autowired
    RazorpayClient razorpayClient;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PaymentIdempotencyService paymentIdempotencyService;

    @Override
    public PaymentResponse createOrder(String userId, Double fare, String rideId, String captainId) {
        String idempotencyKey = paymentIdempotencyService.createIdempotencyKey(rideId, userId);
        boolean isFirstAttempt = paymentIdempotencyService.claimIdempotencyKey(idempotencyKey, Duration.ofMinutes(10));

        if (!isFirstAttempt) {
            Object existing = paymentIdempotencyService.getResult(idempotencyKey);

            if (existing.equals(PaymentStatus.pending)) {
                throw new DuplicateKeyException("Duplicate Payment tried! System blocked it.");
            }

            return (PaymentResponse) existing;
        }

        Optional<Payment> existingPayment = paymentRepository.findByIdempotencyKey(idempotencyKey);

        if (existingPayment.isPresent()) {
            throw new DuplicateKeyException("Duplicate Payment tried! System blocked it.");
        }

        double platformCommission = Math.round((float) (fare * 0.20));
        double captainCommission = Math.round((float) (fare - platformCommission));

        JSONObject options = new JSONObject();

        options.put("amount", fare * 100);
        options.put("currency", "INR");
        options.put("payment_capture", 1);

        try {
            Order order = razorpayClient.orders.create(options);

            Payment payment = new Payment();

            payment.setOrderId(order.get("id"));
            payment.setUserId(userId);
            payment.setRideId(rideId);
            payment.setCaptainId(captainId);
            payment.setCaptainCommission(captainCommission);
            payment.setTotalAmount(fare);
            payment.setPlatformCommission(platformCommission);
            payment.setStatus(PaymentStatus.pending);

            Payment savedOrder = paymentRepository.save(payment);

            PaymentResponse.RazorpayOrderDetails razorpayOrderDetails = new PaymentResponse.RazorpayOrderDetails(
                    order.get("id"),
                    order.get("amount"),
                    order.get("currency"),
                    order.get("status")
            );

            PaymentResponse response = PaymentResponse.of(razorpayOrderDetails, savedOrder);

            // storing response in redis to prevent duplicate payments
            paymentIdempotencyService.markCompleted(idempotencyKey, response, Duration.ofMinutes(10));

            return response;
        }
        catch (RazorpayException e) {
            paymentIdempotencyService.evictKey(idempotencyKey);
            throw new RuntimeException("Error in create-order service: " + e.getMessage(), e);
        }
    }

    @Override
    public String getProviderName() {
        return "razorpay";
    }
}
