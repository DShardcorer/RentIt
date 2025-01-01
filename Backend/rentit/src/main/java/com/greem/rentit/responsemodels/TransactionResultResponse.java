package com.greem.rentit.responsemodels;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransactionResultResponse implements Serializable {

        private String status;

        private String message;

        private String URL;
}
