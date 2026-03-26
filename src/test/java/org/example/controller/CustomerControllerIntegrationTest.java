package org.example.controller;

import org.example.model.Car;
import org.example.model.Customer;
import org.example.model.CarRental;
import org.example.service.CarRentalService;
import org.example.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarRentalService carRentalService;

    @MockitoBean
    private CustomerService customerService;

    private MockHttpSession session;
    private final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        // Tạo một session giả lập đã đăng nhập
        session = new MockHttpSession();
        session.setAttribute("EMAIL", TEST_EMAIL);
    }

    // 1. Test xem lịch sử khi ĐÃ đăng nhập
    @Test
    void viewHistory_LoggedIn_ReturnsHistoryView() throws Exception {
        // 1. Tạo đối tượng Car giả lập
        Car mockCar = new Car();
        mockCar.setCarName("Toyota Camry"); // Phải có cái này để Thymeleaf không bị null

        // 2. Tạo đối tượng CarRental giả lập và gắn Car vào
        CarRental rental1 = new CarRental();
        rental1.setCar(mockCar);
        // Nếu trong file HTML bạn còn hiển thị ngày thuê, giá tiền... thì set luôn ở đây
        // rental1.setRentDate(LocalDate.now());

        List<CarRental> mockHistory = Arrays.asList(rental1);

        // 3. Giả lập Service trả về danh sách đã có dữ liệu car
        when(carRentalService.getCustomerHistory(TEST_EMAIL)).thenReturn(mockHistory);

        // Act & Assert
        mockMvc.perform(get("/customers/history").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("history"))
                .andExpect(model().attributeExists("historyList"));
    }

    // 2. Test xem lịch sử khi CHƯA đăng nhập (Session trống)
    @Test
    void viewHistory_NotLoggedIn_RedirectsToLogin() throws Exception {
        mockMvc.perform(get("/customers/history")) // Không truyền session vào
                .andExpect(status().is3xxRedirection()) // Trả về redirect (302)
                .andExpect(redirectedUrl("/auth/login"));
    }

    // 3. Test xem Profile
    @Test
    void viewProfile_LoggedIn_ReturnsProfileView() throws Exception {
        // Arrange
        Customer mockCustomer = new Customer();
        mockCustomer.setEmail(TEST_EMAIL);
        mockCustomer.setCustomerName("Nguyen Van A");
        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(mockCustomer);

        // Act & Assert
        mockMvc.perform(get("/customers/profile").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("customer", mockCustomer));
    }


}