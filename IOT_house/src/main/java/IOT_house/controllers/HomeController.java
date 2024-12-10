package IOT_house.controllers;

import java.time.LocalDate;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
import IOT_house.services.user.impl.UserService;
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
	    return "home_temp.html";
	}

	@GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,Model model) {
		if (error != null) {
	        model.addAttribute("error", error);
	    }
        model.addAttribute("loginForm", new Account());
        return "login_temp.html";
    }

	@GetMapping("/waiting")
	public String checkIfAdmin(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
		    model.addAttribute("error", "Account does not exist");
		    return "login_temp.html";
		}

	    String username = authentication.getName();
	    Account account = userService.findbyUser(username);

	    if (account != null && !account.isStatus()) {
	        SecurityContextHolder.clearContext();
	        model.addAttribute("error", "Account is locked");
	        return "login_temp.html";
	    }
	    if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
	        return "redirect:/admin/account";
	    }

	    return "redirect:/user/home";
	}

	@GetMapping("/logout")
    public String logout() {
        return "redirect:/home/";
    }

	@GetMapping("/register")
	public String showRegisterForm(Model model) {
	    model.addAttribute("registerForm", new Account());
	    return "register_temp.html";
	}

	@PostMapping("/register")
	public String processRegister(@ModelAttribute Account registerForm, Model model) {
		
		if (userService.CheckUserExist(registerForm.getUsername())) {
	        model.addAttribute("error", "Username is existed");
	        model.addAttribute("registerForm", new Account());
	        return "register_temp.html";
	    }

	    if (userService.CheckEmailExist(registerForm.getEmail())) {
	        model.addAttribute("error", "Email is existed");
	        model.addAttribute("registerForm", new Account());
	        return "register_temp.html";
	    }
	    Roles role = roleRepository.findByName("USER").get();
	    registerForm.setPassword(passwordEncoder.encode(registerForm.getPassword()));
	    registerForm.setRoles(Collections.singleton(role));
	    registerForm.setCrDate(LocalDate.now());
	    registerForm.setStatus(true);
	    
	    userService.insert(registerForm);

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
	    model.addAttribute("email", email);
	    return "reset_psw_temp.html";
	}
	@PostMapping("/update-password")
	public String updatePassword(@RequestParam("email") String email,
	                             @RequestParam("newPassword") String newPassword,
	                             @RequestParam("confirmPassword") String confirmPassword,
	                             Model model) {
	    if (!newPassword.equals(confirmPassword)) {
	        model.addAttribute("error", "Passwords do not match");
	        model.addAttribute("email", email);
	        return "reset_psw_temp.html";
	    }
        userService.UpdatePswbyEmail(email, confirmPassword);
        model.addAttribute("message", "Password updated successfully");
        model.addAttribute("loginForm", new Account());
        return "login_temp.html";
	    }
}
