package com.swiftride.paymentservice.DTOs;

import com.swiftride.paymentservice.Entities.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private RazorpayOrderDetails razorpayOrder;
    private Payment order;

    public static PaymentResponse of(RazorpayOrderDetails razorpayOrder, Payment order) {
        return new PaymentResponse(razorpayOrder, order);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RazorpayOrderDetails {
        private String id;
        private Object amount;
        private String currency;
        private String status;
    }
}
