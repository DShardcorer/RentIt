package com.greem.rentit.responsemodels;

import com.greem.rentit.entity.Payment;
import com.greem.rentit.entity.Property;
import lombok.Data;


@Data
public class PaymentResponse {

    private Property property;

    private Payment payment;

    public PaymentResponse(Property property, Payment payment) {
        this.property = property;
        this.payment = payment;
    }

    public PaymentResponse() {

    }
}
