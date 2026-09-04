package com.swiftride.paymentservice.factories;

import com.swiftride.paymentservice.strategies.interfaces.PaymentStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentFactory {

    private final Map<String, PaymentStrategy> strategies;

    public PaymentFactory(List<PaymentStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(PaymentStrategy::getProviderName, Function.identity()));
    }

    public PaymentStrategy create(String provider) {
        PaymentStrategy strategy = strategies.get(provider.toLowerCase());

        if (strategy == null) {
            throw new IllegalArgumentException("Invalid provider:::: " + provider);
        }

        return strategy;
    }
}
