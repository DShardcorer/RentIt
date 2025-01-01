package com.greem.rentit.dao;

import com.greem.rentit.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Booking findByRenterEmailAndPropertyId(String renterEmail, Integer propertyId);

    List<Booking> findPropertiesByRenterEmail(String renterEmail);

    Booking findByRenterEmail(String renterEmail);


    List<Booking> findAllByRenterEmail(String renterEmail);

    @Query("SELECT b FROM Booking b JOIN Payment p ON b.id = p.bookingId WHERE p.id = :paymentId")
    Booking findByPaymentId(Integer paymentId);

    @Query("SELECT b FROM Booking b JOIN Property p ON b.propertyId = p.id WHERE p.landlordEmail = :landlordEmail")
    List<Booking> findBookingByLandlordEmail(String landlordEmail);

    Boolean findByPropertyId(int propertyId);
}
