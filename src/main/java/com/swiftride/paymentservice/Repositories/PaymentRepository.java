package com.swiftride.paymentservice.Repositories;

import com.swiftride.paymentservice.Entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
