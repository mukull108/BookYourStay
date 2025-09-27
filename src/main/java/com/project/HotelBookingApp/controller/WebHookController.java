package com.project.HotelBookingApp.controller;

import com.project.HotelBookingApp.services.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class WebHookController {
    private final BookingService bookingService;

    @Value("${stripe.webhook.secret}")
    private String endPointSecret;

    @PostMapping("/payment")
    public ResponseEntity<?> capturePayments(@RequestBody String payload,
                                             @RequestHeader("Stripe-Signature") String sigHeader){

        try{
            Event event = Webhook.constructEvent(payload, sigHeader, endPointSecret);
            bookingService.capturePayment(event);
            return ResponseEntity.notFound().build();
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }
    }
}
