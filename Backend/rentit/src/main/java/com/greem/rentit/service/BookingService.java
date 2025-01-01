package com.greem.rentit.service;

import com.greem.rentit.dao.BookingRepository;
import com.greem.rentit.dao.PaymentRepository;
import com.greem.rentit.dao.PropertyRepository;
import com.greem.rentit.entity.Booking;
import com.greem.rentit.entity.Payment;
import com.greem.rentit.entity.Property;
import com.greem.rentit.responsemodels.BookingResponse;
import com.greem.rentit.utils.ConversionUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class BookingService {

    private PropertyRepository propertyRepository;

    private BookingRepository bookingRepository;

    private PaymentRepository paymentRepository;


    public BookingService(PropertyRepository propertyRepository, BookingRepository bookingRepository, PaymentRepository paymentRepository) {
        this.propertyRepository = propertyRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
    }


    public Property bookProperty(String renterEmail, int propertyId) throws Exception {

        Optional<Property> property = propertyRepository.findById(propertyId);

        Booking validatebooking = bookingRepository.findByRenterEmailAndPropertyId(renterEmail, propertyId);

        if (!property.isPresent() || validatebooking != null || property.get().getSlotsAvailable() == 0) {
            throw new Exception("property not found or out of slots");
        }
        //property.get is used to get the property object from the Optional<property> object
        property.get().setSlotsAvailable(property.get().getSlotsAvailable() - 1);
        propertyRepository.save(property.get());

        Booking booking = new Booking(propertyId, renterEmail, "PENDING", LocalDate.now().toString(), LocalDate.now().plusDays(30).toString(), LocalDate.now().toString(), LocalDate.now().toString());
        bookingRepository.save(booking);

        return property.get();
    }

    public Boolean bookingPropertyByUser(String renterEmail, int propertyId) {
        Booking validatebooking = bookingRepository.findByRenterEmailAndPropertyId(renterEmail, propertyId);
        return validatebooking != null;
    }

    public int currentLoansCount(String renterEmail) {
        return bookingRepository.findPropertiesByRenterEmail(renterEmail).size();
    }



    public void returnProperty(String userEmail, int propertyId) throws Exception {
        Optional<Property> property = propertyRepository.findById(propertyId);
        Booking booking = bookingRepository.findByRenterEmailAndPropertyId(userEmail, propertyId);

        if (!property.isPresent() || booking == null) {
            throw new Exception("property not found or not checked out");
        }

        property.get().setSlotsAvailable(property.get().getSlotsAvailable() + 1);
        propertyRepository.save(property.get());

        bookingRepository.delete(booking);
    }

    public void renewBooking(String userEmail, int propertyId) throws Exception {
        Optional<Property> property = propertyRepository.findById(propertyId);
        Booking booking = bookingRepository.findByRenterEmailAndPropertyId(userEmail, propertyId);

        if (!property.isPresent() || booking == null) {
            throw new Exception("property not found or not checked out");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = sdf.parse(booking.getEndDate());
        Date d2 = sdf.parse(LocalDate.now().toString());

        TimeUnit time = TimeUnit.DAYS;

        long diff = d1.getTime() - d2.getTime();

        long diffDays = time.convert(diff, TimeUnit.MILLISECONDS);


        booking.setEndDate(LocalDate.now().plusDays(30).toString());
    }

    public BookingResponse getCurrentBooking(String userEmail) {

        Booking curBooking = bookingRepository.findByRenterEmail(userEmail);
        int id = curBooking.getPropertyId();
        Property property = propertyRepository.findById(id).get();
        return new BookingResponse(property, curBooking);
    }

    public List<BookingResponse> getBookingsByLandlordEmail( String landlordEmail) {
        List<Booking> bookings = bookingRepository.findBookingByLandlordEmail(landlordEmail);
        List<Integer> ids = bookings.stream().map(Booking::getPropertyId).toList();
        List<BookingResponse> bookingResponses = bookings.stream().map(booking -> {
            Property property = propertyRepository.findById(booking.getPropertyId()).get();
            return new BookingResponse(property, booking);
        }).toList();
        return bookingResponses;
    }

    public void approveDeclineBooking(int bookingId, String landlordEmail, String action) {
        Booking booking = bookingRepository.findById(bookingId).
                orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        Property property = propertyRepository.findById(booking.getPropertyId()).get();
        if (action.toUpperCase().equals("APPROVE")) {
            booking.setStatus("CONFIRMED");
            Payment payment= new Payment(bookingId, booking.getRenterEmail(), landlordEmail,
                    LocalDate.now().toString(), property.getPricePerMonth(), "UNPAID",
                    ConversionUtils.convertStringToDate(booking.getCreatedAt().split(" ")[0]).plusDays(30).toString());
            paymentRepository.save(payment);

        } else if (action.toUpperCase().equals("DECLINE")) {
            booking.setStatus("CANCELLED");
        }
        bookingRepository.save(booking);
    }

    public List<Property> findByLandlordEmail(String landlordEmail) {
        return propertyRepository.findByLandlordEmail(landlordEmail);
    }

    public List<BookingResponse> getBookingsByRenterEmail(String userEmail) {
        List<Booking> bookings = bookingRepository.findPropertiesByRenterEmail(userEmail);
        List<Integer> ids = bookings.stream().map(Booking::getPropertyId).toList();
        List<BookingResponse> bookingResponses = bookings.stream().map(booking -> {
            Property property = propertyRepository.findById(booking.getPropertyId()).get();
            return new BookingResponse(property, booking);
        }).toList();
        return bookingResponses;
    }

    public Boolean isPropertyBooked(int propertyId) {
        return bookingRepository.findByPropertyId(propertyId) != null;
    }
}
