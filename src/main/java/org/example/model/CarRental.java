package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "CarRental")
@Getter
@Setter
@IdClass(CarRentalId.class)
@NoArgsConstructor
@AllArgsConstructor
public class CarRental {
    @Id
    @ManyToOne
    @JoinColumn(name = "CustomerID", referencedColumnName = "CustomerID", nullable = false)
    private Customer customer;


    @Id
    @ManyToOne
    @JoinColumn(name = "CarID", referencedColumnName = "CarID", nullable = false)
    private Car car;

    @Column(name = "PickupDate", nullable = false)
    private LocalDate pickupDate;

    @Column(name = "ReturnDate", nullable = false)
    private LocalDate returnDate;

    @Column(name = "RentPrice", nullable = false)
    private BigDecimal rentPrice;

    @Column(name = "Status", nullable = false)
    private String status;
}
