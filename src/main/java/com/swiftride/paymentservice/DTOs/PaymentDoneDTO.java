package com.swiftride.paymentservice.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentDoneDTO {
    private Double fare;

    @JsonProperty("payment_id")
    private String paymentId;

    private String orderId;
    private Object order;
    private String userId;
    private String rideId;
    private String captainId;
}
