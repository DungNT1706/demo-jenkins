package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CarProducer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarProducer {
    @Id
    @Column(name = "ProducerID", nullable = false)
    private String producerId;
    @Column(name = "ProducerName", nullable = false)
    private String name;
    @Column(name = "Address", nullable = false)
    private String address;
    @Column(name = "Country", nullable = false)
    private String country;
}
