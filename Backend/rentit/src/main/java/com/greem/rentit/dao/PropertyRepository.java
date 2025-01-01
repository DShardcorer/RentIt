package com.greem.rentit.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.greem.rentit.entity.Property;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Integer> {

    Page<Property> findByTitleContaining(@RequestParam String title, Pageable pageable);

    Page<Property> findByPricePerMonthLessThanEqual(@RequestParam Integer price, Pageable pageable);

    Page<Property> findByLocationContaining(@RequestParam String location, Pageable pageable);

    @Query("SELECT p FROM Property p WHERE p.id IN :propertyIds")
    List<Property> findPropertiesByIds(List<Integer> propertyIds);


    List<Property> findByLandlordEmail(String landlordEmail);

    //Find properties by booking IDs
    @Query("SELECT p FROM Property p JOIN Booking b ON p.id = b.propertyId WHERE b.id IN :bookingIds")
    List<Property> findPropertiesByBookingIds(List<Integer> bookingIds);



    List<Property> findByLatitudeBetweenAndLongitudeBetween(@RequestParam double latitude1, @RequestParam double latitude2, @RequestParam double longitude1, @RequestParam double longitude2);

}
