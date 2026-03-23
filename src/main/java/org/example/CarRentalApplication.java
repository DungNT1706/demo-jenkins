package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Phép thuật gom mọi thứ lại với nhau nằm ở dòng này
public class CarRentalApplication {

    public static void main(String[] args) {
        // Lệnh kích hoạt toàn bộ hệ thống Spring Boot
        SpringApplication.run(CarRentalApplication.class, args);
    }

}