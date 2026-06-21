package com.swiftride.paymentservice.Handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiftride.paymentservice.DTOs.PaymentDoneDTO;
import com.swiftride.paymentservice.Entities.Payment;
import com.swiftride.paymentservice.Enums.PaymentStatus;
import com.swiftride.paymentservice.Repositories.PaymentRepository;
import com.swiftride.paymentservice.Services.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentDoneHandler {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    KafkaProducerService kafkaProducerService;

    @Autowired
    ObjectMapper objectMapper;

    public void handle (PaymentDoneDTO message) {
        try {
            Optional<Payment> existingPayment = paymentRepository.findByRideId(message.getRideId());

            if (existingPayment.isEmpty()) {
                throw new RuntimeException("no payment exists for this ride!");
            }

            Payment payment = existingPayment.get();

            if (message.getPaymentId() == null || message.getOrderId() == null) {
                payment.setStatus(PaymentStatus.failed);
                paymentRepository.save(payment);

                throw new RuntimeException("Error in payment service: Payment ID and Order ID are missing!");
            }

            System.out.println("PAYMENT ID::: " + message.getPaymentId());

            payment.setPaymentId(message.getPaymentId());
            payment.setStatus(PaymentStatus.success);

            paymentRepository.save(payment);

            PaymentDoneDTO event = new PaymentDoneDTO();

            event.setFare(payment.getTotalAmount());
            event.setPaymentId(payment.getPaymentId());
            event.setOrderId(payment.getOrderId());
            event.setOrder(message.getOrder());
            event.setUserId(payment.getUserId());
            event.setRideId(payment.getRideId());
            event.setCaptainId(payment.getCaptainId());

            String jsonEvent =
                    objectMapper.writeValueAsString(event);

            kafkaProducerService.sendMessage("payment-settled", jsonEvent);
            kafkaProducerService.sendMessage("ride-completed-notify-user", jsonEvent);
            kafkaProducerService.sendMessage("update-captain-earnings", jsonEvent);
        }
        catch (Exception e) {
            throw new RuntimeException("Something went wrong while handling payment-done handler!" + e.getMessage(), e);
        }
    }
}
