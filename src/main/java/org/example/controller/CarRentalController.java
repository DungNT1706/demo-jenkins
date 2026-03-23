package org.example.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.model.CarRental;
import org.example.service.CarRentalService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor

public class CarRentalController {

    private final CarRentalService carRentalService;


    @GetMapping("/admin/rentals")
    public String manageAndReportRentals(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        List<CarRental> rentalList;


        if (startDate != null && endDate != null) {
            rentalList = carRentalService.findByPickupDateBetweenOrderByPickupDateDesc(startDate, endDate);
        }

        else {
            rentalList = carRentalService.findAllByOrderByPickupDateDesc();
        }

        model.addAttribute("rentalList", rentalList);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "rentals";
    }
    @PostMapping("/admin/rentals/complete")
    public String completeRental(@RequestParam String carId, @RequestParam String customerId) {
        // Nhờ Service làm việc nặng nhọc
        carRentalService.completeRental(carId, customerId);

        // Làm xong thì load lại trang Quản lý (PRG Pattern)
        return "redirect:/admin/rentals";
    }

    // XỬ LÝ NÚT: HỦY ĐƠN
    @PostMapping("/admin/rentals/cancel")
    public String cancelRental(@RequestParam String carId, @RequestParam String customerId) {
        carRentalService.cancelRental(carId, customerId);
        return "redirect:/admin/rentals";
    }

}