package com.greem.rentit.requestmodels;

import lombok.Data;

import java.util.Optional;


@Data
public class ReviewRequest {

    private double rating;

    private int propertyId;

    private Optional<String> reviewDescription;
}
