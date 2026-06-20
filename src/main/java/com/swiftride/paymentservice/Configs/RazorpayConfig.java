package com.swiftride.paymentservice.Configs;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {
    @Value("${razorpay.key.RAZORPAY_KEY_ID}")
    private String razorPayKeyId;

    @Value("${razorpay.key.RAZORPAY_KEY_SECRET}")
    private String razorpayKeySecret;

    @Value("${razorpay.key.RAZORPAY_MERCHANT_ID}")
    private String razorpayMerchantId;

    @Bean
    public RazorpayClient razorpayClient() throws RazorpayException {
        return new RazorpayClient(razorPayKeyId, razorpayKeySecret);
    }
}
