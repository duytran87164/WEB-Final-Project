package IOT_house.controllers.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import IOT_house.entity.Account;
import IOT_house.services.admin.HouseService;
import IOT_house.services.user.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user")
public class HomeUserController {
	@Autowired
	UserService userService;
	@Autowired
	HouseService houseService;
	private boolean ledStatus = false; // LED OFF ban đầu
	@GetMapping("/home")
	public String home(HttpSession session, Model model) {
	    Account user = (Account) session.getAttribute("user"); // Lấy đối tượng user từ session
	    if (user != null && user instanceof Account) {
	        model.addAttribute("welcomeMessage", "Welcome, " + user.getFullName());
	        model.addAttribute("user", user);
	    } else {
	        model.addAttribute("welcomeMessage", "Welcome to IoT Platform");
	    }
	    System.out.println("Welcome message: " + model.getAttribute("welcomeMessage"));
	    model.addAttribute("platformDescription", "Our platform allows you to monitor and manage all your IoT devices efficiently.");
	    model.addAttribute("deviceCount", 0); // Thay bằng dữ liệu thực tế
	    model.addAttribute("dataPoints", 0); // Thay bằng dữ liệu thực tế
	    model.addAttribute("year", 2024); // Thay bằng logic lấy năm hiện tại
	    return "user/home.html"; // Trả về view tên "index.html"
	}
	// API để trả về trang web điều khiển LED
    @GetMapping("/led")
    public String led(Model model) {
        model.addAttribute("ledStatus", ledStatus ? 1 : 0); // Trạng thái LED ban đầu
        return "TestESP/button_tg_led"; // Trả về trang HTML
    }
}
