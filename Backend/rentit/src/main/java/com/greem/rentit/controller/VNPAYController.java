package com.greem.rentit.controller;

//import com.greem.rentit.service.VNPAYService;
import com.greem.rentit.responsemodels.TransactionResultResponse;
import com.greem.rentit.service.VNPAYService;
import com.greem.rentit.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import java.io.UnsupportedEncodingException;
import java.text.ParseException;

@RequestMapping("/api/vnpay")
@RestController

public class VNPAYController {

    @Autowired
    private VNPAYService vnpayService;

    @GetMapping("/secure/makePayment")
    public ResponseEntity<?> createPayment(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(value = "paymentId") int paymentId) throws UnsupportedEncodingException, ParseException {
        String renterEmail = ExtractJWT.payloadJWTExtraction(token);
        return vnpayService.createPayment(renterEmail, paymentId);
    }

    @GetMapping("/paymentResult")
    public ResponseEntity<?> paymentResult(
            @RequestParam( value = "vnp_Amount") String amount,
            @RequestParam(value = "vnp_BankCode") String bankCode,
            @RequestParam( value = "vnp_OrderInfo") String orderInfo,
            @RequestParam( value = "vnp_ResponseCode") String responseCode)
    {
        TransactionResultResponse transactionResultResponse = new TransactionResultResponse();
        if(responseCode.equals("00")){
            transactionResultResponse.setStatus("success");
            transactionResultResponse.setMessage("Payment success");
            transactionResultResponse.setURL("http://localhost:3000/payment-success");

        }else {
            transactionResultResponse.setStatus("fail");
            transactionResultResponse.setMessage("Payment fail");
            transactionResultResponse.setURL("http://localhost:3000/payment-fail");
        }
        return ResponseEntity.status(HttpStatus.OK).body(transactionResultResponse);
    }

}