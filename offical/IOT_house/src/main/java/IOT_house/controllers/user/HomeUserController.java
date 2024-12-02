package IOT_house.controllers.user;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import IOT_house.entity.Account;
import IOT_house.entity.Houses;
import IOT_house.services.admin.AccountService;
import IOT_house.services.admin.HouseService;
import IOT_house.services.user.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user")
public class HomeUserController {
	@Autowired
	HouseService houseService;
	@Autowired
	AccountService accService;
	@Autowired
	UserService userService;
	private boolean ledStatus = false; // LED OFF ban đầu
	
	
	@GetMapping("/home")
	public String home(HttpSession session, Model model) {
	    Account user = (Account) session.getAttribute("user"); // Lấy đối tượng user từ session
	    if (user != null && user instanceof Account) {
	        model.addAttribute("fullname",user.getFullName());
	        model.addAttribute("image",user.getImage());
	        model.addAttribute("user", user);
	        
	    } else {
	        model.addAttribute("fullname", "err");
	    }
	    
	    long id=user.getId();
	    
	 // Kiểm tra nếu `id` tồn tại
        Optional<Account> acc = accService.findById(id);
        if (acc.isPresent()) {
            List<Houses> houses = houseService.findByAccount(acc.get());  // Sử dụng hàm findByAccount đã tạo trước đó
            model.addAttribute("houses", houses);
            model.addAttribute("id_acc", id);
        } else {
            model.addAttribute("message", "Account not found");
        }
	    
	    return "list-house/list_house_of_user.html"; 
	}
	// API để trả về trang web điều khiển LED
    @GetMapping("/led")
    public String led(Model model) {
        model.addAttribute("ledStatus", ledStatus ? 1 : 0); // Trạng thái LED ban đầu
        return "TestESP/button_tg_led"; // Trả về trang HTML
    }
}
