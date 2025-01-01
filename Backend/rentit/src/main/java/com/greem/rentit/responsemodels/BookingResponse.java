package com.greem.rentit.responsemodels;

import com.greem.rentit.entity.Booking;
import com.greem.rentit.entity.Property;
import lombok.Data;

@Data
public class BookingResponse {

    private Property property;

    private Booking booking;

    public BookingResponse(Property property, Booking booking) {
        this.property = property;
        this.booking = booking;
    }
}
