package com.greem.rentit.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "reviews")
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "property_id")
    private int propertyId;

    @Column(name = "renter_email")
    private String renterEmail;

    @Column(name = "rating")
    private double rating;

    @Column(name = "comments")
    private String comments;

    @Column(name = "created_at")
    private String createdAt;
}
