package com.swiftride.paymentservice.Services;

import com.swiftride.paymentservice.DTOs.PaymentResponse;
import com.swiftride.paymentservice.Enums.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class PaymentIdempotencyService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private final String REDIS_KEY = "swiftride:payments:idempotency:key:";

    public String createIdempotencyKey(String rideId, String userId) {
        return String.format("ride:%s:user:%s", rideId, userId);
    }

    public boolean claimIdempotencyKey(String idempotencyKey, Duration ttl) {
        Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(REDIS_KEY + idempotencyKey, PaymentStatus.pending, ttl);

        return Boolean.TRUE.equals(acquired);
    }

    public void markCompleted(String idempotencyKey, PaymentResponse resultJson, Duration ttl) {
        redisTemplate.opsForValue()
                .set(REDIS_KEY + idempotencyKey, resultJson, ttl);
    }

    public Object getResult(String idempotencyKey) {
        return redisTemplate.opsForValue()
                .get(REDIS_KEY + idempotencyKey);
    }

    public void evictKey(String idempotencyKey) {
        redisTemplate.delete(idempotencyKey);
    }
}
