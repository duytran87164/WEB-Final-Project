package IOT_house.controllers.user;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import IOT_house.entity.Account;
import IOT_house.entity.Equipments;
import IOT_house.entity.Houses;
import IOT_house.services.admin.AccountService;
import IOT_house.services.admin.EquipmentService;
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
	@Autowired
	EquipmentService equipService;
	@Autowired
    private HttpSession session;
	private boolean ledStatus = false; // LED OFF ban đầu
	
	
	@GetMapping("/home")
	public String home(HttpSession session, Model model) {
	    Account user = (Account) session.getAttribute("user"); // Lấy đối tượng user từ session
	    if (user != null && user instanceof Account) {
	        model.addAttribute("user", user);
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
	
	
	@GetMapping("/equipments/{id}")
	public String find_id(@PathVariable String id, Model model) {
		
		
		//session
		Account user = (Account) session.getAttribute("user"); // Lấy đối tượng user từ session
	    if (user != null && user instanceof Account) {
	        model.addAttribute("user", user);
	    }
		// Trực tiếp lấy nhà theo id
		List<Equipments> equip = equipService.findByHouseId(id); // Lấy danh sách Equipments theo id_house
		model.addAttribute("equip", equip); // Thêm danh sách Equipments vào model
		model.addAttribute("idHouse", id); // Thêm id_house vào model
		return "equip/list_equip_of_house_user.html"; // Trả về trang list.html
	}
	
	
	// API để trả về trang web điều khiển LED
    @GetMapping("/monitor/{idHouse}")
    public String led(Model model,@PathVariable String idHouse) {
    	
    	//session
		Account user = (Account) session.getAttribute("user"); // Lấy đối tượng user từ session
	    if (user != null && user instanceof Account) {
	        model.addAttribute("user", user);
	    }
    	
    	List<Equipments> equip = equipService.findByHouseId(idHouse);
    	List<Equipments> button = equipService.findBySensor(equip,"Switch");
    	List<Equipments> dht11 = equipService.findBySensor(equip,"DHT11");
    	model.addAttribute("button", button);
    	model.addAttribute("dht11", dht11);
    	model.addAttribute("idHouse", idHouse);
        model.addAttribute("ledStatus", ledStatus ? 1 : 0); // Trạng thái LED ban đầu
        return "/monitor"; // Trả về trang HTML
    }
}
