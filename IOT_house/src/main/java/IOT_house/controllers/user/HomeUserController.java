package IOT_house.controllers.user;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import IOT_house.services.admin.HouseService;
import IOT_house.services.user.UserService;

@Controller
@RequestMapping(value = "/user")
public class HomeUserController {
	@Autowired
	UserService userService;
	@Autowired
	HouseService houseService;
	@GetMapping("/home")
	public String homePage(Model model) {
        
        return "user/home.html";  // Return the Thymeleaf template "home.html"
    }
}
