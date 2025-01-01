package com.greem.rentit.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "booking_id")
    private int bookingId;

    @Column(name = "renter_email")
    private String renterEmail;

    @Column(name = "landlord_email")
    private String landlordEmail;

    @Column(name = "payment_date")
    private String paymentDate;

    @Column(name = "amount")
    private double amount;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private String createdAt;

    public Payment(int bookingId, String renterEmail, String landlordEmail, String paymentDate, double amount, String status, String createdAt) {
        this.bookingId = bookingId;
        this.renterEmail = renterEmail;
        this.landlordEmail = landlordEmail;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }


    public Payment() {

    }
}
