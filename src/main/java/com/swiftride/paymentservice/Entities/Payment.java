package com.swiftride.paymentservice.Entities;

import com.swiftride.paymentservice.Enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_rideId", columnList = "rideId", unique = true),
        @Index(name = "idx_paymentId", columnList = "paymentId", unique = true)
})
public class Payment {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paymentId", nullable = true, unique = true)
    private String paymentId;

    @Column(name = "orderId", nullable = false, unique = true)
    private String orderId;

    @Column(name = "rideId", nullable = false, unique = true)
    private String rideId;

    @Column(name = "captainId", nullable = false)
    private String captainId;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "captain_commission", nullable = false)
    private Double captainCommission;

    @Column(name = "platform_commission", nullable = false)
    private Double platformCommission;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.pending;
}
