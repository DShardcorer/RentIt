package com.greem.rentit.dao;

import com.greem.rentit.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
//    Payment findByRenterEmailAndPropertyId(String renterEmail, Integer propertyId);

    Optional<Payment> findById(Integer id);
    List<Payment> findPropertiesByRenterEmail(String renterEmail);

    List<Payment> findByRenterEmail(String renterEmail);

    List<Payment> findByLandlordEmail(String landlordEmail);


}
