package com.swiftride.paymentservice.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiftride.paymentservice.DTOs.PaymentDoneDTO;
import com.swiftride.paymentservice.Handlers.PaymentDoneHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    PaymentDoneHandler paymentDoneHandler;

    @KafkaListener(
            topics = "payment-done",
            groupId = "payments-group"
    )
    public void consume(String message) throws JsonProcessingException {
        System.out.println("Received:: " + message);

        ObjectMapper mapper = new ObjectMapper();

        PaymentDoneDTO dtoMessage =
                mapper.readValue(message, PaymentDoneDTO.class);

        paymentDoneHandler.handle(dtoMessage);
    }
}
