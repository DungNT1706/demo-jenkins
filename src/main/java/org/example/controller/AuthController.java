package org.example.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.dto.CustomerRegisterDTO;
import org.example.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final CustomerService customerService;
    @GetMapping("/login")
    public String showPageLogin(){
        return "login";
    }
    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               Model model,
                               HttpSession session){
        String role = customerService.login(email, password);

        if ("ADMIN".equals(role) || "CUSTOMER".equals(role)) {
            // Lưu quyền vào Session để các trang khác biết
            session.setAttribute("ROLE", role);
            session.setAttribute("EMAIL", email);

            // Đăng nhập thành công thì ĐIỀU HƯỚNG (redirect) sang trang danh sách xe
            return "redirect:/cars";
        } else {
            // Đăng nhập thất bại: Gắn câu thông báo lỗi lên mâm, và bắt ở lại trang login
            model.addAttribute("error", "Sai email hoặc mật khẩu!");
            return "login";
        }
    }
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDTO", new CustomerRegisterDTO());
        return "register";
    }

    // 2. Xử lý khi khách hàng bấm nút Submit
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("registerDTO") CustomerRegisterDTO registerDTO) {
        // Nhờ Service thực hiện nghiệp vụ lưu 2 bảng
        customerService.registerNewCustomer(registerDTO);

        // PRG Pattern: Thành công thì sút về trang Đăng nhập kèm theo một thông báo nhỏ
        return "redirect:/auth/login?registered";
    }
}
