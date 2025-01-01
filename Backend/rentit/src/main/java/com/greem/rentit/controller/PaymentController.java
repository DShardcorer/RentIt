package com.greem.rentit.controller;


import com.greem.rentit.entity.Booking;
import com.greem.rentit.entity.Payment;
import com.greem.rentit.responsemodels.BookingResponse;
import com.greem.rentit.responsemodels.PaymentResponse;
import com.greem.rentit.service.PaymentService;
import com.greem.rentit.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/secure/findPaymentsByLandlordEmail")
    public List<PaymentResponse> findPaymentsByLandlordEmail(@RequestHeader(value = "Authorization") String token) {
        String landlordEmail = ExtractJWT.payloadJWTExtraction(token);
        return paymentService.findPaymentsByLandlordEmail(landlordEmail);
    }

    @GetMapping("/secure/findPaymentsByRenterEmail")
    public List<PaymentResponse> findPaymentsByRenterEmail(@RequestHeader(value = "Authorization") String token) {
        String renterEmail = ExtractJWT.payloadJWTExtraction(token);
        return paymentService.findPaymentsByRenterEmail(renterEmail);
    }

    @GetMapping("/secure/updatePayment")
    public Boolean updatePayment(@RequestHeader(value = "Authorization") String token, @RequestParam int paymentId, @RequestParam String status) {
        String renterEmail = ExtractJWT.payloadJWTExtraction(token);
        if(status.equals("00")) {
            paymentService.updatePayment(renterEmail, paymentId);
            return true;
        }else {
            return false;
        }

    }
}
