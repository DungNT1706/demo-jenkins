package org.example.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.model.Car;
import org.example.service.CarRentalService;
import org.example.service.CarService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    private final CarRentalService carRentalService;
    @GetMapping
    public String listCars(Model model){
        List<Car> carList = carService.getAllCars();
        model.addAttribute("carList",carList);
        return "cars";
    }
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        // Đặt một chiếc xe RỖNG lên mâm để giao diện có chỗ điền dữ liệu
        model.addAttribute("car", new Car());
        model.addAttribute("pageTitle", "Thêm Xe Mới"); // Đổi tiêu đề cho ngầu
        return "car-form";
    }

    // 2. MỞ TRANG CẬP NHẬT XE (Update)
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        // Xuống Database lấy chiếc xe có thật lên
        Car car = carService.getCarById(id);
        // Đặt chiếc xe ĐÃ CÓ DỮ LIỆU lên mâm
        model.addAttribute("car", car);
        model.addAttribute("pageTitle", "Cập Nhật Thông Tin Xe");
        return "car-form";
    }

    // 3. XỬ LÝ LƯU DỮ LIỆU (Dùng chung cho cả Create và Update)
    @PostMapping("/save")
    public String saveCar(@ModelAttribute Car car) {
        carService.saveCar(car);
        return "redirect:/cars";
    }
    @PostMapping("/delete/{id}")
    public String deleteCar(@PathVariable("id") String id){
        carService.deleteCar(id);
        return "redirect:/cars";
    }
    @GetMapping("/rent/{id}")
    public String showRentForm(@PathVariable("id") String id, Model model, HttpSession session) {
        // Kiểm tra xem khách đã đăng nhập chưa, nếu chưa đuổi về trang login
        if (session.getAttribute("EMAIL") == null) {
            return "redirect:/auth/login";
        }

        Car car = carService.getCarById(id);
        model.addAttribute("car", car);
        return "rent-form"; // Gọi file rent-form.html
    }

    // 2. Bấm nút "Xác nhận thuê" trên form
    @PostMapping("/rent")
    public String processRent(
            @RequestParam("carId") String carId,
            @RequestParam("pickupDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate pickupDate,
            @RequestParam("returnDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            HttpSession session,
            RedirectAttributes redirectAttributes) { // Thêm cái túi thần kỳ này để mang theo thông báo lỗi

        String email = (String) session.getAttribute("EMAIL");

        try {
            // Cố gắng đặt xe
            carRentalService.rentCar(carId, email, pickupDate, returnDate);
            return "redirect:/customers/history";

        } catch (IllegalArgumentException e) {
            // Nếu bị vấp phải cái "Rào chắn" ở Service, nhét lời mắng vào túi thần kỳ
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            // Đá khách hàng quay lại đúng cái form của chiếc xe đó bắt nhập lại
            return "redirect:/cars/rent/" + carId;
        }
    }
}
