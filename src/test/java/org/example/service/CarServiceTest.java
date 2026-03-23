package org.example.service;

import org.example.model.Car;
import org.example.repository.CarRentalRepository;
import org.example.repository.CarRepository;
import org.example.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Kích hoạt Mockito cho JUnit 5
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarRentalRepository carRentalRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CarService carService; // Tự động tiêm các Mock ở trên vào Service này

    private Car mockCar;

    @BeforeEach
    void setUp() {
        mockCar = new Car();
        mockCar.setId("CAR001");
        mockCar.setCarName("Tesla Model 3");
        mockCar.setStatus("AVAILABLE");
    }

    @Test
    @DisplayName("Test lấy xe theo ID thành công")
    void getCarById_Success() {
        // Arrange: Khi gọi findById thì trả về mockCar
        when(carRepository.findById("CAR001")).thenReturn(Optional.of(mockCar));

        // Act: Gọi hàm thực tế
        Car result = carService.getCarById("CAR001");

        // Assert: Kiểm tra kết quả
        assertNotNull(result);
        assertEquals("Tesla Model 3", result.getCarName());
        verify(carRepository, times(1)).findById("CAR001");
    }

    @Test
    @DisplayName("Test lấy xe theo ID thất bại - Tung ngoại lệ")
    void getCarById_NotFound_ThrowsException() {
        // Arrange
        when(carRepository.findById("NOT_FOUND")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            carService.getCarById("NOT_FOUND");
        });

        assertEquals("error: do not found any car", exception.getMessage());
    }

    @Test
    @DisplayName("Test xóa xe - Trường hợp xe ĐÃ từng được thuê (Soft Delete)")
    void deleteCar_WhenRented_ShouldSetStatusToDeleted() {
        // Arrange
        when(carRepository.findById("CAR001")).thenReturn(Optional.of(mockCar));
        // Giả lập xe này đã tồn tại trong bảng Rental
        when(carRentalRepository.existsByCar_Id("CAR001")).thenReturn(true);

        // Act
        carService.deleteCar("CAR001");

        // Assert
        assertEquals("DELETED", mockCar.getStatus());
        verify(carRepository).save(mockCar); // Kiểm tra xem có gọi hàm save để update status không
        verify(carRepository, never()).delete(any()); // Đảm bảo KHÔNG gọi hàm delete cứng
    }

//    @Test
//    @DisplayName("Test xóa xe - Trường hợp xe CHƯA từng được thuê (Hard Delete)")
//    void deleteCar_WhenNotRented_ShouldDeleteFromDB() {
//        // Arrange
//        when(carRepository.findById("CAR001")).thenReturn(Optional.of(mockCar));
//        when(carRentalRepository.existsByCar_Id("CAR001")).thenReturn(false);
//
//        // Act
//        carService.deleteCar("CAR001");
//
//        // Assert
//        verify(carRepository).delete(mockCar); // Kiểm tra xem có gọi hàm delete cứng không
//        verify(carRepository, never()).save(any()); // Đảm bảo KHÔNG gọi hàm save update
//    }
//
//    @Test
//    @DisplayName("Test xóa xe thất bại - ID không tồn tại")
//    void deleteCar_NotFound_ThrowsException() {
//        // Arrange
//        when(carRepository.findById("UNKNOWN")).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(RuntimeException.class, () -> {
//            carService.deleteCar("UNKNOWN");
//        });
//    }
}