package com.greem.rentit.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @Column(name = "landlord_email")
    private String landlordEmail;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "price_per_month")
    private double pricePerMonth;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Column(name = "picture_paths")
    private String picturePaths;

    @Column(name = "slots")
    private int slots;

    @Column(name = "slots_available")
    private int slotsAvailable;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

}
