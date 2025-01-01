package com.greem.rentit.controller;


import com.greem.rentit.requestmodels.ReviewRequest;
import com.greem.rentit.service.ReviewService;
import com.greem.rentit.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/secure/user/property")
    public Boolean isPropertyReviewedByUser(@RequestHeader(value = "Authorization") String token,
                                    @RequestParam int propertyId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token);
        if (userEmail == null) {
            throw new Exception("User email is missing from token");
        }
        return reviewService.isPropertyReviewedByUser(userEmail, propertyId);
    }

    @PostMapping("/secure")
    public void postReview(@RequestHeader(value = "Authorization") String token,
                           @RequestBody ReviewRequest reviewRequest) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token);
        if (userEmail == null) {
            throw new Exception("User email is missing from token");
        }
        reviewService.postReview(userEmail, reviewRequest);
    }


}
