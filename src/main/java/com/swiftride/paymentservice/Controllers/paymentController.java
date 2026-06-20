package com.swiftride.paymentservice.Controllers;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class paymentController {
    private final RazorpayClient razorpayClient;

    public paymentController(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder () throws RazorpayException {
        JSONObject options = new JSONObject();

        options.put("amount", 4000);
        options.put("currency", "INR");
        options.put("receipt", "order_rcptid_11");

        return ResponseEntity.ok("Payment order created!");
    }

}
