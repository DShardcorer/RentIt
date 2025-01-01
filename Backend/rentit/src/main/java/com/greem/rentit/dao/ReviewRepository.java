package com.greem.rentit.dao;

import com.greem.rentit.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;


public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Page<Review> findByPropertyId(@RequestParam("id") Integer propertyId, Pageable pageable);

    Review findByRenterEmailAndPropertyId(String renterEmail, Integer propertyId);
}
