package com.swiftride.paymentservice.Services;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.swiftride.paymentservice.Entities.Payment;
import com.swiftride.paymentservice.Enums.PaymentStatus;
import com.swiftride.paymentservice.Repositories.PaymentRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private final RazorpayClient razorpayClient;
    private final PaymentRepository paymentRepository;

    public PaymentService(RazorpayClient razorpayClient, PaymentRepository paymentRepository) {
        this.razorpayClient = razorpayClient;
        this.paymentRepository = paymentRepository;
    }

    public Map<String, Object> createOrder (
            String userId,
            Double fare,
            String rideId,
            String captainId
    ) {
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

            Payment saveOrder = paymentRepository.save(payment);

            Map<String, Object> razorpayOrder = new HashMap<>();

            razorpayOrder.put("id", order.get("id"));
            razorpayOrder.put("amount", order.get("amount"));
            razorpayOrder.put("currency", order.get("currency"));
            razorpayOrder.put("status", order.get("status"));

            Map<String, Object> map = new HashMap<>();
            map.put("razorpay_order", razorpayOrder);
            map.put("order", saveOrder);

            return map;
        }
        catch (RazorpayException e) {
            throw new RuntimeException("Error in create-order service: " + e.getMessage(), e);
        }
    }
}
