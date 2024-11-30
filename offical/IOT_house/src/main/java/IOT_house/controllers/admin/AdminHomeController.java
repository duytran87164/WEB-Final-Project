package IOT_house.controllers.admin;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import IOT_house.entity.Account;

import jakarta.servlet.http.HttpSession;
@Controller
@RequestMapping(value = "/admin")
public class AdminHomeController {
//	@GetMapping("/")
//	public String index() {
//		return "list-house/list.html";
//	}

	@GetMapping("/home")
	public String home(HttpSession session, Model model) {
	    Account user = (Account) session.getAttribute("user"); // Lấy đối tượng user từ session
	    if (user != null && user instanceof Account) {
	        model.addAttribute("fullname",user.getFullName());
	        model.addAttribute("user", user);
	    } else {
	        model.addAttribute("fullname", "err");
	    }

	    return "admin/home_admin_temp.html"; // Trả về view tên "index.html"
	}
}
