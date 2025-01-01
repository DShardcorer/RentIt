package com.greem.rentit.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "bookings")
public class Booking {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;

        @Column(name = "property_id")
        private int propertyId;

        @Column(name = "renter_email")
        private String renterEmail;

        @Column(name = "status")
        private String status;

        @Column(name = "start_date")
        private String startDate;

        @Column(name = "end_date")
        private String endDate;

        @Column(name = "created_at")
        private String createdAt;

        @Column(name = "updated_at")
        private String updatedAt;

        public Booking( int propertyId, String renterEmail, String status, String startDate, String endDate, String createdAt, String updatedAt) {
            this.propertyId = propertyId;
            this.renterEmail = renterEmail;
            this.status = status;
            this.startDate = startDate;
            this.endDate = endDate;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

    public Booking() {

    }
}
