package com.greem.rentit.controller;


import com.greem.rentit.responsemodels.BookingResponse;
import com.greem.rentit.service.BookingService;
import com.greem.rentit.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/secure/currentbooking")
    public BookingResponse getCurrentBooking(@RequestHeader(value = "Authorization") String token) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token);
        return bookingService.getCurrentBooking(userEmail);
    }

    @GetMapping("/secure/findBookingsByLandlordEmail")
    public List<BookingResponse> getBookingsByLandlordEmail(@RequestHeader(value = "Authorization") String token) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token);
        return bookingService.getBookingsByLandlordEmail(userEmail);
    }

    @PutMapping("/secure/approveDeclineBooking")
    public void approveBooking(@RequestHeader(value = "Authorization") String token,
                                @RequestParam int bookingId, @RequestParam String action) throws Exception {
        String landlordEmail = ExtractJWT.payloadJWTExtraction(token);
        bookingService.approveDeclineBooking(bookingId, landlordEmail, action);
    }


    @GetMapping("/secure/findBookingsByRenterEmail")
    public List<BookingResponse> getBookingsByRenterEmail(@RequestHeader(value = "Authorization") String token) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token);
        return bookingService.getBookingsByRenterEmail(userEmail);
    }
}
