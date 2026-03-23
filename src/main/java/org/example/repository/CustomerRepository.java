package org.example.repository;

import org.example.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,String> {
    Customer findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
    Customer findByEmail(String email);
}
