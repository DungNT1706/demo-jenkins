package org.example.repository;

import org.example.model.CarProducer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarProducerRepository extends JpaRepository<CarProducer,String> {
}
