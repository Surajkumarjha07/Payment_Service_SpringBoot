package com.swiftride.paymentservice.DTOs;

import lombok.Data;

@Data
public class OrderDTO {
    private Long id;
    private String paymentId;
    private String orderId;
    private String rideId;
    private String captainId;
    private String userId;
    private Double totalAmount;
    private Double captainCommission;
    private Double platformCommission;
    private String status;
}