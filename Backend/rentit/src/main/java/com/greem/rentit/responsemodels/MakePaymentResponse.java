package com.greem.rentit.responsemodels;


import lombok.Data;

import java.io.Serializable;

@Data
public class MakePaymentResponse implements Serializable {

    private String status;

    private String message;

    private String URL;
}
