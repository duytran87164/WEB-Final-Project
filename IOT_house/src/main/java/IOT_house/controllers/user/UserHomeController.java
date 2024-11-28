package IOT_house.controllers.user;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import IOT_house.entity.Account;
import IOT_house.services.user.UserService;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping(value = "/home")
public class UserHomeController {
	@Autowired
	UserService userService;
	 @Autowired
	 private EmailService emailService;
	@GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("websiteTitle", "IoT Platform");
        model.addAttribute("welcomeMessage", "Welcome to the IoT Platform");
        model.addAttribute("platformDescription", "Our platform allows you to monitor and manage all your IoT devices efficiently.");
        model.addAttribute("deviceCount", 42);  // Replace with actual data
        model.addAttribute("dataPoints", 150); // Replace with actual data
        model.addAttribute("year", LocalDate.now().getYear());
        return "/user/home.html";  // Return the Thymeleaf template "home.html"
    }
	@GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginForm", new Account()); // Thêm đối tượng LoginForm vào model
        return "user/login_temp.html"; // Điều chỉnh theo đường dẫn view của bạn
    }

	@PostMapping("/login")
	public String processLogin(@ModelAttribute Account loginForm, Model model) {
	    // Kiểm tra xem email và mật khẩu có hợp lệ không
	    Account account = userService.findbyUser(loginForm.getUsername()); // Giả sử bạn có service để lấy thông tin tài khoản

	    if (account != null && account.getPassword().equals(loginForm.getPassword())) {
	        // Đăng nhập thành công, chuyển hướng đến trang chủ
	        return "redirect:/home/";
	    } else {
	        // Nếu thông tin đăng nhập sai, hiển thị thông báo lỗi
	        model.addAttribute("error", "Invalid email or password");
	        return "redirect:/home/login"; // Quay lại trang đăng nhập
	    }
	}
	
	// Hiển thị trang đăng ký
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
	    model.addAttribute("registerForm", new Account()); // Tạo đối tượng Account trống để bind dữ liệu
	    return "user/register_temp.html"; // Trang đăng ký
	}


	// Controller đăng ký tài khoản
	@PostMapping("/register")
	public String processRegister(@ModelAttribute Account registerForm, Model model) {
	    // Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu chưa
	    if (userService.CheckEmailExist(registerForm.getEmail())) {
	        model.addAttribute("error", "Email đã tồn tại");
	        return "redirect:/home/register"; // Quay lại trang đăng ký
	    }
	    if (userService.CheckUserExist(registerForm.getUsername())) {
	        model.addAttribute("error", "user name đã tồn tại");
	        return "redirect:/home/register"; // Quay lại trang đăng ký
	    }
	 // Gán ngày hiện tại vào trường crDate
	    registerForm.setCrDate(LocalDate.now());
	    // Nếu email chưa tồn tại, tiến hành lưu tài khoản mới
	    userService.insert(registerForm); // Giả sử bạn có service để lưu tài khoản

	    // Chuyển hướng tới trang đăng nhập sau khi đăng ký thành công
	    return "redirect:/home/login";
	}
	@GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "user/forget_psw.html";
    }

	@PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        try {
            emailService.sendEmail(email,"Welcome" , "xin chao Dkhai");
            model.addAttribute("message", "An email with a reset link has been sent.");
        } catch (Exception e) {
            model.addAttribute("error", "There was an error sending the email. Please try again.");
        }
        return "user/forget_psw";
    }

	
}
