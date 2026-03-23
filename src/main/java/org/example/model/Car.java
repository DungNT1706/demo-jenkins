package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Car")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @Column(name = "CarID", nullable = false)
    private String id;
    @Column(name="CarName", nullable = false)
    private String carName;
    @Column(name = "CarModelYear", nullable = false)
    private int modelYear;
    @Column(name = "Color", nullable = false)
    private String color;
    @Column(name = "Capacity", nullable = false)
    private int capacity;
    @Column(name = "Description", nullable = false)
    private String description;
    @Column(name = "ImportDate", nullable = false)
    private LocalDate importDate;
    @Column(name = "RentPrice", nullable = false)
    private BigDecimal rentPrice;
    @Column(name = "Status", nullable = false)
    private String status;
    @ManyToOne
    @JoinColumn(name = "ProducerID",referencedColumnName = "ProducerID", nullable = false)
    private CarProducer carProducer;
}
