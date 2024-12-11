package IOT_house.controllers;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import IOT_house.entity.Account;
import IOT_house.entity.Roles;
import IOT_house.repository.RoleRepository;
import IOT_house.services.user.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping(value = "/home")
public class HomeController {
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserService userService;
	@Autowired
	RoleRepository roleRepository;
	 @Autowired
	 private EmailService emailService;
	@GetMapping("/")
	public String home(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username=authentication.getName();
	    Account user = userService.findbyUser(username);
	    if (user != null && user instanceof Account) {
	        model.addAttribute("fullname",user.getFullName());
	        model.addAttribute("user", user);
	    } else {
	        model.addAttribute("fullname", "err");
	    }
	    return "home_temp.html"; // Trả về view tên "index.html"
	}

	@GetMapping("/login")
    public String showLoginPage(Model model) {
		
        model.addAttribute("loginForm", new Account()); // Thêm đối tượng LoginForm vào model
        return "login_temp.html"; // Điều chỉnh theo đường dẫn view của bạn
    }

	@PostMapping("/login")
<<<<<<< HEAD
	public String processLogin(@RequestParam String username, 
	                           @RequestParam String password, 
	                           Model model) {
	    // Kiểm tra thông tin đăng nhập thủ công (nếu cần)
	    Account account = userService.findbyUser(username);
	    if (account != null) {
	        if (!account.isStatus()) {
	            model.addAttribute("error", "Account is locked");
	            return "login_temp";  // Quay lại trang đăng nhập nếu tài khoản bị khóa
	        }
	        
=======
	public String processLogin(@ModelAttribute Account loginForm,HttpSession session, Model model) {
	    // Kiểm tra xem email và mật khẩu có hợp lệ không
	    Account account = userService.findbyUser(loginForm.getUsername()); // Giả sử bạn có service để lấy thông tin tài khoản
	    
	    
	    if (account != null && account.getPassword().equals(loginForm.getPassword())) {
	        // Đăng nhập thành công, chuyển hướng đến trang chủ
		    if (account.getStatus() == 0) {
		        // Nếu thông tin đăng nhập sai, hiển thị thông báo lỗi
		        model.addAttribute("error", "Account is locked");
		        model.addAttribute("loginForm", new Account());
		        return "login_temp.html"; // Quay lại trang đăng nhập
		    }
		    session.setAttribute("user", account);
	        return "redirect:/home/waiting";
	    }
	    else {
	    	model.addAttribute("error", "The Username or Password is Incorrect");
	    	model.addAttribute("loginForm", new Account());
	        return "login_temp.html"; // Quay lại trang đăng nhập
	    }
	    
>>>>>>> 5dec53c57f841a0ff6f5c024f35820cca18357fc

	    }
	    return "redirect:/home/waiting";  // Chuyển hướng đến trang waiting sau khi đăng nhập thành công
	}




	@GetMapping("/waiting")
	public String checkIfAdmin(Model model) {
		// Kiểm tra nếu người dùng chưa đăng nhập
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
		    model.addAttribute("error", "Account does not exist");
		    return "login_temp.html"; // Quay lại trang đăng nhập
		}
//
//	    // Kiểm tra vai trò của người dùng
	    if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
	        // Nếu là admin
	        return "redirect:/admin/account";
	    }
	 // Kiểm tra xem có quyền nào không
//	    if (authentication != null && authentication.getAuthorities() != null) {
//	        model.addAttribute("authorities", authentication.getPrincipal());
//	    } else {
//	        model.addAttribute("authorities", "No authorities found");
//	    }
//		Account account = userService.findbyUser(authentication.getName());
////		Set<Roles> roles = account.getRoles();
////		List<Roles> role = roleRepository.findAll();
//		model.addAttribute("roles", authentication.getPrincipal());

	    return "redirect:/user/home"; // Trả về trang test.html
	}

	@GetMapping("/logout")
    public String logout() {

        // Chuyển hướng người dùng về trang chủ hoặc trang login
        return "redirect:/home/";
    }
	// Hiển thị trang đăng ký
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
	    model.addAttribute("registerForm", new Account()); // Tạo đối tượng Account trống để bind dữ liệu
	    return "register_temp.html"; // Trang đăng ký
	}


	// Controller đăng ký tài khoản
	@PostMapping("/register")
	public String processRegister(@ModelAttribute Account registerForm, Model model) {
		
		if (userService.CheckUserExist(registerForm.getUsername())) {
	        model.addAttribute("error", "Username is existed");
	        model.addAttribute("registerForm", new Account());
	        return "register_temp.html"; // Quay lại trang đăng ký
	    }
	    // Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu chưa
	    if (userService.CheckEmailExist(registerForm.getEmail())) {
	        model.addAttribute("error", "Email is existed");
	        model.addAttribute("registerForm", new Account());
	        return "register_temp.html"; // Quay lại trang đăng ký
	    }
	    Roles role = roleRepository.findByName("USER").get();
	    registerForm.setPassword(passwordEncoder.encode(registerForm.getPassword()));
	    registerForm.setRoles(Collections.singleton(role));
	 // Gán ngày hiện tại vào trường crDate
	    registerForm.setCrDate(LocalDate.now());
	    registerForm.setStatus(true);
	    
	    // Nếu email chưa tồn tại, tiến hành lưu tài khoản mới
	    userService.insert(registerForm); // Giả sử bạn có service để lưu tài khoản

	    // Chuyển hướng tới trang đăng nhập sau khi đăng ký thành công
	    return "redirect:/home/login";
	}
	@GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forget_psw.html";
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
        		model.addAttribute("message", "UserName isn't exsit");
        	}
        } catch (Exception e) {
            model.addAttribute("error", "There was an error sending the email. Please try again.");
        }
        return "login_temp.html";
    }
	@GetMapping("/reset-password")
	public String resetPasswordPage(@RequestParam("email") String email, Model model) {
	    model.addAttribute("email", email); // Truyền email vào model
	    return "reset_psw_temp.html"; // Trả về trang đổi mật khẩu
	}
	@PostMapping("/update-password")
	public String updatePassword(@RequestParam("email") String email,
	                             @RequestParam("newPassword") String newPassword,
	                             @RequestParam("confirmPassword") String confirmPassword,
	                             Model model) {
	    if (!newPassword.equals(confirmPassword)) {
	        model.addAttribute("error", "Passwords do not match");
	        model.addAttribute("email", email);
	        return "reset_psw_temp.html"; // Trả về trang reset-password kèm lỗi
	    }
        userService.UpdatePswbyEmail(email, confirmPassword);
        model.addAttribute("message", "Password updated successfully");
        model.addAttribute("loginForm", new Account());
        return "login_temp.html"; // Chuyển hướng tới trang đăng nhập
	    }


	
}
