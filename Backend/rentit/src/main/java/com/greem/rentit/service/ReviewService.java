package com.greem.rentit.service;


import com.greem.rentit.dao.ReviewRepository;
import com.greem.rentit.entity.Review;
import com.greem.rentit.requestmodels.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

@Service
@Transactional
public class ReviewService {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception{
        Review validateReview = reviewRepository.findByRenterEmailAndPropertyId(userEmail, reviewRequest.getPropertyId());
    if(validateReview != null){
        throw new Exception("You have already reviewed this book");
    }
    Review review = new Review();
    review.setRating(reviewRequest.getRating());
    review.setRenterEmail(userEmail);
    review.setPropertyId(reviewRequest.getPropertyId());

    if(reviewRequest.getReviewDescription().isPresent()){
        review.setComments(reviewRequest.getReviewDescription().map(
                Objects::toString).orElse(null));
    }
    review.setCreatedAt(String.valueOf(Date.valueOf(LocalDate.now())));
    reviewRepository.save(review);
    }

    public Boolean isPropertyReviewedByUser(String userEmail, int propertyId){
        Review validateReview = reviewRepository.findByRenterEmailAndPropertyId(userEmail, propertyId);
        if (validateReview != null){
            return true;
        }else {
            return false;
        }
    }
}

