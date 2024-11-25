package IOT_house.controllers.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import IOT_house.entity.Account;
import IOT_house.services.user.UserService;

@Controller
@RequestMapping("/admin/account")
public class AccountController {
	@Autowired
	UserService userService;
	@RequestMapping("")
	public String all(Model model) {
		List<Account> list = userService.findAll();
		model.addAttribute("listcate", list);

		return "account/list.html";
	}
}
