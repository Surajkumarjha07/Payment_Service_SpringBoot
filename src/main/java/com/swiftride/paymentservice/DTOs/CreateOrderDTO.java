package com.swiftride.paymentservice.DTOs;

import lombok.Data;

@Data
public class CreateOrderDTO {
    private String captainId;
    private String rideId;
    private String fare;
}
