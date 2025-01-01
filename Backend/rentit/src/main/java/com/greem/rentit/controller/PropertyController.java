package com.greem.rentit.controller;


import com.greem.rentit.entity.Property;
import com.greem.rentit.service.BookingService;
import com.greem.rentit.service.PropertyService;
import com.greem.rentit.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    public BookingService bookingService;

    public PropertyService propertyService;


    @Autowired
    public PropertyController(BookingService bookingService, PropertyService propertyService) {
        this.bookingService = bookingService;
        this.propertyService = propertyService;
    }


    @DeleteMapping("/secure/{id}")
    public ResponseEntity<String> deleteProperty(@RequestHeader(value = "Authorization") String token,
                                                 @PathVariable int id) {
        // Check if the property has any active bookings
        String landlordEmail = ExtractJWT.payloadJWTExtraction(token);

        if(propertyService.isPropertyOfLandlord(landlordEmail, id)){
            propertyService.deleteProperty(id);
        } else {
            return new ResponseEntity<>("Property does not belong to landlord", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Property deleted", HttpStatus.OK);
    }



    @PutMapping("/secure/book")
    public Property bookProperty(@RequestParam int propertyId,
                                 @RequestHeader(value = "Authorization") String token) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token);
        return bookingService.bookProperty(userEmail, propertyId);
    }


    @GetMapping("/secure/isbookedbyuser")
    public Boolean isPropertyBookedByUser(@RequestParam int propertyId,
                                      @RequestHeader(value = "Authorization") String token) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token);
        System.out.println("Checking property ID: " + propertyId + " for user: " + userEmail);
        return bookingService.bookingPropertyByUser(userEmail, propertyId);
    }

    @GetMapping("/secure/isBooked")
    public Boolean isPropertyBooked(@RequestHeader(value = "Authorization") String token,
                                    @RequestParam int propertyId) {
        return bookingService.isPropertyBooked(propertyId);
    }


    @GetMapping("/secure/getLandlordProperties")
    public List<Property> getLandlordProperties(@RequestHeader(value = "Authorization") String token) throws Exception{
        String landlordEmail = ExtractJWT.payloadJWTExtraction(token);
        return bookingService.findByLandlordEmail(landlordEmail);
    }



}
