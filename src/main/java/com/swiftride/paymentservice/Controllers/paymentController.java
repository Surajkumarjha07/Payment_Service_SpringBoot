package com.swiftride.paymentservice.Controllers;

import com.swiftride.paymentservice.DTOs.CreateOrderDTO;
import com.swiftride.paymentservice.Services.PaymentService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class paymentController {
    private final PaymentService paymentService;

    public paymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder (
            @RequestBody CreateOrderDTO request,
            @RequestHeader(value = "x-user-payload", required = false) String userPayload
    ) {
        JSONObject payload = new JSONObject(userPayload);

        System.out.println("REACHING SERVER::::::::::::::: " + payload);

        if (
            request.getCaptainId() == null ||
            request.getRideId() == null ||
            request.getFare() == null
        ) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "message", "credentials missing!",
                            "captainId", request.getCaptainId()
                    )
            );
        }

        try {
            var response = paymentService.createOrder(payload.getString("userId"), Double.parseDouble(request.getFare()), request.getRideId(), request.getCaptainId());

            if (response == null) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to create the order!");

            }

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (Exception e) {
            throw new RuntimeException("Error in create-order controller: " + e.getMessage(), e);
        }
    }

}
