package com.greem.rentit.service;


import com.greem.rentit.dao.BookingRepository;
import com.greem.rentit.dao.PaymentRepository;
import com.greem.rentit.dao.PropertyRepository;
import com.greem.rentit.entity.Booking;
import com.greem.rentit.entity.Payment;
import com.greem.rentit.entity.Property;
import com.greem.rentit.responsemodels.PaymentResponse;
import com.greem.rentit.utils.ConversionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentService {

    private PaymentRepository paymentRepository;

    private BookingRepository bookingRepository;

    private PropertyRepository propertyRepository;

    public PaymentService(PaymentRepository paymentRepository, BookingRepository bookingRepository, PropertyRepository propertyRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
    }


    public List<PaymentResponse> findPaymentsByLandlordEmail(String landlordEmail) {
        // Step 1: Fetch payments for the landlord
        List<Payment> payments = paymentRepository.findByLandlordEmail(landlordEmail);

        // Step 2: Extract booking IDs from payments
        List<Integer> bookingIds = payments.stream()
                .map(Payment::getBookingId)
                .collect(Collectors.toList());

        // Step 3: Fetch properties based on booking IDs
        List<Property> properties = propertyRepository.findPropertiesByIds(bookingIds);

        // Step 4: Create a map of bookingId to Property for easy lookup
        Map<Integer, Property> propertyMap = properties.stream()
                .collect(Collectors.toMap(Property::getId, property -> property));

        // Step 5: Create PaymentResponse objects
        List<PaymentResponse> res = new ArrayList<>();
        for (Payment payment : payments) {
            Property property = propertyMap.get(payment.getBookingId());
            if (property != null) {
                PaymentResponse paymentResponse = new PaymentResponse(property, payment);
                res.add(paymentResponse);
            }
        }

        return res; // Return the list of PaymentResponse objects
    }

    public List<PaymentResponse> findPaymentsByRenterEmail(String renterEmail) {
        // Step 1: Fetch payments for the renter
        List<Payment> payments = paymentRepository.findByRenterEmail(renterEmail);

        // Step 2: Extract booking IDs from payments
        List<Integer> bookingIds = payments.stream()
                .map(Payment::getBookingId)
                .collect(Collectors.toList());

        // Step 3: Fetch properties based on booking IDs
        List<Property> properties = propertyRepository.findPropertiesByBookingIds(bookingIds);

        // Step 4: Combine payments and properties into a list of PaymentResponse objects
        List<PaymentResponse> res = new ArrayList<>();

        // Assuming payments and properties lists are of the same size and matched by index
        for (int i = 0; i < payments.size(); i++) {
            Payment payment = payments.get(i);
            Property property = properties.get(i); // Assuming order of payments and properties match
            PaymentResponse paymentResponse = new PaymentResponse(property, payment);
            res.add(paymentResponse);
        }

        return res; // Return the list of PaymentResponse objects
    }

    public void updatePayment(String renterEmail, int paymentId) {
        // Step 1: Find the payment by ID
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        // Step 2: Check if the payment belongs to the renter
        if (!payment.getRenterEmail().equals(renterEmail)) {
            throw new IllegalArgumentException("Payment does not belong to the renter");
        }

        // Step 3: Update the payment status
        payment.setStatus("PAID");

        // Step 4: Save the updated payment
        paymentRepository.save(payment);

        //If booking end date < payment date, add 30 days to booking end date and create a new payment with payment date at booking end date - 15days

        Booking booking = bookingRepository.findById(payment.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (ConversionUtils.convertStringToDate(booking.getEndDate()).isBefore(ConversionUtils.convertStringToDate(payment.getPaymentDate()))) {
            booking.setEndDate(ConversionUtils.convertStringToDate(booking.getEndDate()).plusDays(30).toString());
            Payment newPayment = new Payment(payment.getBookingId(), payment.getRenterEmail(), payment.getLandlordEmail(),
                    ConversionUtils.convertStringToDate(booking.getEndDate()).minusDays(15).toString(), payment.getAmount(), "UNPAID",
                    ConversionUtils.convertStringToDate(booking.getEndDate()).toString());
            paymentRepository.save(newPayment);
            bookingRepository.save(booking);
        }

    }
}

