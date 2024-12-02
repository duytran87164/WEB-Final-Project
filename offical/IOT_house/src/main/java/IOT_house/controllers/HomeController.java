package IOT_house.controllers;

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
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping(value = "/home")
public class HomeController {
	@Autowired
	UserService userService;
	 @Autowired
	 private EmailService emailService;
	@GetMapping("/")
	public String home(HttpSession session, Model model) {
	    model.addAttribute("welcomeMessage", "Welcome to IoT Platform");
	    model.addAttribute("platformDescription", "Our platform allows you to monitor and manage all your IoT devices efficiently.");
	    model.addAttribute("deviceCount", 0); // Thay bằng dữ liệu thực tế
	    model.addAttribute("dataPoints", 0); // Thay bằng dữ liệu thực tế
	    model.addAttribute("year", 2024); // Thay bằng logic lấy năm hiện tại
	    return "user/home.html"; // Trả về view tên "index.html"
	}

	@GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginForm", new Account()); // Thêm đối tượng LoginForm vào model
        return "user/login_temp.html"; // Điều chỉnh theo đường dẫn view của bạn
    }

	@PostMapping("/login")
	public String processLogin(@ModelAttribute Account loginForm,HttpSession session, Model model) {
	    // Kiểm tra xem email và mật khẩu có hợp lệ không
	    Account account = userService.findbyUser(loginForm.getUsername()); // Giả sử bạn có service để lấy thông tin tài khoản
	    
	    
	    if (account != null && account.getPassword().equals(loginForm.getPassword())) {
	        // Đăng nhập thành công, chuyển hướng đến trang chủ
		    if (account.getStatus() == 0) {
		        // Nếu thông tin đăng nhập sai, hiển thị thông báo lỗi
		        model.addAttribute("error", "Account is locked");
		        model.addAttribute("loginForm", new Account());
		        return "user/login_temp.html"; // Quay lại trang đăng nhập
		    }
		    session.setAttribute("user", account);
	        return "redirect:/home/waiting";
	    }
	    else {
	    	model.addAttribute("error", "ERROR user or password");
	    	model.addAttribute("loginForm", new Account());
	        return "user/login_temp.html"; // Quay lại trang đăng nhập
	    }
	    

	}
	@GetMapping("/waiting")
	public String checkIfAdmin(HttpSession session, Model model) {
	    // Lấy thông tin người dùng từ session
	    Account account = (Account) session.getAttribute("user");
	    
	    if (account == null) {
	        // Nếu không có thông tin người dùng trong session, chuyển hướng về trang đăng nhập
	    	model.addAttribute("error", "Account isn't exsit");
	    	model.addAttribute("loginForm", new Account());
	        return "user/login_temp.html"; // Quay lại trang đăng nhập
	    }
	    
	    if (account.getIsAdmin() == true) {
	        // Nếu là admin, chuyển hướng tới trang admin
	        return "redirect:/admin/account";
	    } else {
	        // Nếu không phải admin, hiển thị thông báo
	    	return "redirect:/user/home";
	    }
	}
	@GetMapping("/logout")
    public String logout(HttpSession session) {
        // Hủy toàn bộ session
        session.invalidate();

        // Chuyển hướng người dùng về trang chủ hoặc trang login
        return "redirect:/home/login";
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
		
		if (userService.CheckUserExist(registerForm.getUsername())) {
	        model.addAttribute("error", "user name đã tồn tại");
	        model.addAttribute("registerForm", new Account());
	        return "user/register_temp.html"; // Quay lại trang đăng ký
	    }
	    // Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu chưa
	    if (userService.CheckEmailExist(registerForm.getEmail())) {
	        model.addAttribute("error", "Email đã tồn tại");
	        model.addAttribute("registerForm", new Account());
	        return "user/register_temp.html"; // Quay lại trang đăng ký
	    }
	    
	 // Gán ngày hiện tại vào trường crDate
	    registerForm.setCrDate(LocalDate.now());
	    registerForm.setStatus(1);
	    registerForm.setIsAdmin(false);
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
    public String processForgotPassword(@RequestParam("username") String username, Model model) {
		try {
			Account account =  userService.findbyUser(username);
        	if (account != null) {
            emailService.sendResetPasswordEmail(account.getEmail());
            model.addAttribute("message", "An email with a reset link has been sent.");
            }
        	else {
        		model.addAttribute("message", "User Name isn't exsit");
        	}
        } catch (Exception e) {
            model.addAttribute("error", "There was an error sending the email. Please try again.");
        }
        return "user/login_temp.html";
    }
	@GetMapping("/reset-password")
	public String resetPasswordPage(@RequestParam("email") String email, Model model) {
	    model.addAttribute("email", email); // Truyền email vào model
	    return "user/reset_psw_temp.html"; // Trả về trang đổi mật khẩu
	}
	@PostMapping("/update-password")
	public String updatePassword(@RequestParam("email") String email,
	                             @RequestParam("newPassword") String newPassword,
	                             @RequestParam("confirmPassword") String confirmPassword,
	                             Model model) {
	    if (!newPassword.equals(confirmPassword)) {
	        model.addAttribute("error", "Passwords do not match");
	        model.addAttribute("email", email);
	        return "user/reset_psw_temp.html"; // Trả về trang reset-password kèm lỗi
	    }
        userService.UpdatePswbyEmail(email, confirmPassword);
        model.addAttribute("message", "Password updated successfully");
        model.addAttribute("loginForm", new Account());
        return "user/login_temp.html"; // Chuyển hướng tới trang đăng nhập
	    }


	
}
