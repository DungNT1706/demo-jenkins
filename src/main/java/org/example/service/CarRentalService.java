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
public class CarRentalService {
    private final CarRentalRepository carRentalRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    public List<CarRental> getCustomerHistory(String email) {
        return carRentalRepository.findByCustomer_EmailOrderByPickupDateDesc(email);
    }
    public List<CarRental> findByPickupDateBetweenOrderByPickupDateDesc(LocalDate startDate, LocalDate endDate){
        return carRentalRepository.findByPickupDateBetweenOrderByPickupDateDesc(startDate,endDate);
    }
    public  List<CarRental> findAllByOrderByPickupDateDesc(){
        return carRentalRepository.findAllByOrderByPickupDateDesc();
    }
    @Transactional // Rất quan trọng: Đảm bảo nếu lỗi thì hoàn tác cả 2 bảng
    public void completeRental(String carId, String customerId) {
        // 1. Lấy đơn hàng ra
        CarRental rental = carRentalRepository.findByCar_IdAndCustomer_CustomerId(carId, customerId);
        if (rental != null) {
            // 2. Chốt đơn: Đổi trạng thái thành Đã hoàn thành
            rental.setStatus("COMPLETED");
            carRentalRepository.save(rental);

            // 3. Nghiệp vụ cốt lõi: Lấy chiếc xe ra và đổi lại thành Sẵn sàng
            Car car = rental.getCar();
            car.setStatus("AVAILABLE");
            carRepository.save(car);
        }
    }

    @Transactional
    public void cancelRental(String carId, String customerId) {
        CarRental rental = carRentalRepository.findByCar_IdAndCustomer_CustomerId(carId, customerId);
        if (rental != null) {
            // Đổi trạng thái thành Đã hủy
            rental.setStatus("CANCELLED");
            carRentalRepository.save(rental);

            // Xe bị bùng kèo -> Trả lại kho để cho người khác thuê
            Car car = rental.getCar();
            car.setStatus("AVAILABLE");
            carRepository.save(car);
        }
    }
    @Transactional // Bắt buộc phải có vì ta thay đổi 2 bảng!
    public void rentCar(String carId, String customerEmail, LocalDate pickupDate, LocalDate returnDate) {
        if (pickupDate.isAfter(returnDate) || pickupDate.isEqual(returnDate)) {
            throw new IllegalArgumentException("Lỗi: Ngày nhận xe (Pickup) phải trước ngày trả xe (Return)!");
        }
        // 1. Tìm chiếc xe khách muốn thuê
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        // 2. Tìm thông tin khách hàng đang đăng nhập bằng Email
        Customer customer = customerRepository.findByEmail(customerEmail);
        // (Lưu ý: Bạn nhớ phải có hàm findByEmail trong CustomerRepository nhé)

        // 3. Tính toán số tiền (Số ngày thuê * Giá 1 ngày)
        long days = ChronoUnit.DAYS.between(pickupDate, returnDate);
        if (days <= 0) days = 1; // Thuê trả trong ngày tính là 1 ngày
        BigDecimal totalPrice = car.getRentPrice().multiply(BigDecimal.valueOf(days));

        // 4. Tạo hóa đơn thuê xe (CarRental)
        CarRental rental = new CarRental();
        rental.setCar(car); // Hoặc rental.setCarid(carId) tùy cách bạn mapping
        rental.setCustomer(customer); // Hoặc rental.setCustomerid(customer.getCustomerId())
        rental.setPickupDate(pickupDate);
        rental.setReturnDate(returnDate);
        rental.setRentPrice(totalPrice);
        rental.setStatus("RENTING"); // Trạng thái đang thuê

        carRentalRepository.save(rental); // Lưu bảng 1

        // 5. Cập nhật lại trạng thái chiếc xe thành "Đang cho thuê"
        car.setStatus("RENTED");
        carRepository.save(car);
    }
}
