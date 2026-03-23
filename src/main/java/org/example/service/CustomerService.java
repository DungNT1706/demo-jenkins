package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CustomerRegisterDTO;
import org.example.model.Account;
import org.example.model.Customer;
import org.example.repository.AccountRepository;
import org.example.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    public List<Customer> getAllCustomer(){
        return customerRepository.findAll();
    }
    public boolean emailExisted(String email){
        return customerRepository.existsByEmail(email);
    }
    public String login(String email, String password){
        if ("admin@gmail.com".equals(email) && "admin123".equals(password)) {
            return "ADMIN"; // Thành công với quyền Admin
        }
        Customer customer = customerRepository.findByEmailAndPassword(email,password);
        if(customer!=null){
            return "CUSTOMER";
        }
        return "INVALID";
    }

    @Transactional // Bảo vệ 2 thao tác Insert dưới đây!
    public void registerNewCustomer(CustomerRegisterDTO dto) {
        if(emailExisted(dto.getEmail())){
            throw new RuntimeException("email existed!");
        }
        // 1. TẠO TÀI KHOẢN (ACCOUNT) TRƯỚC
        Account newAccount = new Account();
        // Tạo mã ID ngẫu nhiên, ví dụ: ACC_a1b2c3d4
        newAccount.setAccountId("ACC_" + UUID.randomUUID().toString().substring(0, 8));
        newAccount.setAccountName(dto.getCustomerName());
        newAccount.setRole("CUSTOMER"); // Mặc định ai đăng ký cũng là Khách hàng

        accountRepository.save(newAccount);


        Customer newCustomer = new Customer();
        newCustomer.setCustomerId("CUS_" + UUID.randomUUID().toString().substring(0, 8));
        newCustomer.setCustomerName(dto.getCustomerName());
        newCustomer.setEmail(dto.getEmail());
        newCustomer.setPassword(dto.getPassword());
        newCustomer.setMobile(dto.getMobile());
        newCustomer.setBirthday(dto.getBirthday());
        newCustomer.setIdentityCard(dto.getIdentityCard());
        newCustomer.setLicenceNumber(dto.getLicenceNumber());
        newCustomer.setLicenceDate(dto.getLicenceDate());


        newCustomer.setAccount(newAccount);

        customerRepository.save(newCustomer);
    }
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    // 2. Cập nhật thông tin
    @Transactional
    public void updateCustomerProfile(String email, Customer updatedData) {
        // Tìm khách hàng gốc trong DB
        Customer existingCustomer = customerRepository.findByEmail(email);

        if (existingCustomer != null) {
            // Chỉ cập nhật những trường được phép sửa (Không cho sửa ID, Email, Password ở form này)
            existingCustomer.setCustomerName(updatedData.getCustomerName());
            existingCustomer.setMobile(updatedData.getMobile());
            existingCustomer.setBirthday(updatedData.getBirthday());
            existingCustomer.setIdentityCard(updatedData.getIdentityCard());
            existingCustomer.setLicenceNumber(updatedData.getLicenceNumber());
            existingCustomer.setLicenceDate(updatedData.getLicenceDate());

            // Lưu đè lại xuống DB
            customerRepository.save(existingCustomer);
        }
    }
}
