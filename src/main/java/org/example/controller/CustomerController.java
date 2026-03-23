package org.example.controller;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.model.CarRental;
import org.example.model.Customer;
import org.example.service.CarRentalService;
import org.example.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// ... (Các import khác giữ nguyên)

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CarRentalService carRentalService;
    private final CustomerService customerService; // Tiêm thêm CustomerService vào đây nhé!



    @GetMapping("/history")
    public String viewHistory(HttpSession session, Model model) {
        // 1. Kiểm tra xem khách đã đăng nhập chưa
        String email = (String) session.getAttribute("EMAIL");
        if (email == null) {
            return "redirect:/auth/login"; // Chưa đăng nhập thì đuổi ra cửa
        }

        // 2. Lấy danh sách lịch sử dựa vào cái Email vừa lấy trong thẻ Session
        List<CarRental> historyList = carRentalService.getCustomerHistory(email);

        // 3. Đưa dữ liệu lên mâm cho Thymeleaf
        model.addAttribute("historyList", historyList);

        return "history"; // Gọi file history.html
    }
    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        String email = (String) session.getAttribute("EMAIL");
        if (email == null) return "redirect:/auth/login";

        // Lấy thông tin hiện tại của khách hàng gửi sang file HTML
        Customer customer = customerService.getCustomerByEmail(email);
        model.addAttribute("customer", customer);
        return "profile";
    }

    // 2. XỬ LÝ LƯU HỒ SƠ KHI BẤM NÚT UPDATE
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("customer") Customer updatedCustomer,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        String email = (String) session.getAttribute("EMAIL");
        if (email == null) return "redirect:/auth/login";

        // Gọi hàm update trong Service
        customerService.updateCustomerProfile(email, updatedCustomer);

        // Dùng Flash Attribute để báo thành công
        redirectAttributes.addFlashAttribute("successMsg", "🎉 Cập nhật hồ sơ thành công!");

        return "redirect:/customers/profile"; // PRG Pattern: Load lại trang hồ sơ
    }
}