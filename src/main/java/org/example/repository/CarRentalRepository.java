package org.example.repository;

import org.example.model.CarRental;
import org.example.model.CarRentalId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CarRentalRepository extends JpaRepository<CarRental, CarRentalId> {
    boolean existsByCar_Id(String carId);

    List<CarRental> findByCustomer_CustomerId(String id);

    List<CarRental> findByPickupDateBetweenOrderByPickupDateDesc(LocalDate startDate, LocalDate endDate);

    List<CarRental> findAllByOrderByPickupDateDesc();
    CarRental findByCar_IdAndCustomer_CustomerId(String carId, String customerId);
    List<CarRental> findByCustomer_EmailOrderByPickupDateDesc(String email);
}
