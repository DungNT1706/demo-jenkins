package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Car;
import org.example.model.CarRental;
import org.example.model.Customer;
import org.example.repository.CarRentalRepository;
import org.example.repository.CarRepository;
import org.example.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final CarRentalRepository carRentalRepository;
    private final CustomerRepository customerRepository;

    public List<Car> getAllCars(){
        return carRepository.findAll();
    }
    public Car getCarById(String id){
        return carRepository.findById(id).orElseThrow(()->new RuntimeException("error: do not found any car"));
    }
    public Car saveCar(Car car){
        return carRepository.save(car);
    }
    public void deleteCar(String carId){
        Car car = carRepository.findById(carId)
                .orElseThrow(()-> new RuntimeException("do not found any car!"));

        boolean carRented = carRentalRepository.existsByCar_Id(car.getId());
        if(carRented){
            car.setStatus("DELETED");
            carRepository.save(car);
            System.out.println("car unvailable");
        }else{
            carRepository.delete(car);
            System.out.println("car deleted");
        }
    }


}
