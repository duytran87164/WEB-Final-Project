package IOT_house.controllers.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import IOT_house.entity.Account;
import IOT_house.services.admin.AccountService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/account")
public class AccountController {
    @Autowired
    private HttpSession session;
	@Autowired
	AccountService accService;

	@RequestMapping("")
	public String all(Model model) {
		//session
		Account user = (Account) session.getAttribute("user"); // Lấy đối tượng user từ session
	    if (user != null && user instanceof Account) {
	        model.addAttribute("user", user);
	    }
	    
		List<Account> list = accService.findAll();
		model.addAttribute("acc", list);

		return "account/list_acc.html";
	}
	
	
	@GetMapping("/find")
	public String findUsername(Model model, @RequestParam String userfind) {
	    // Lấy user từ session
	    Account user = (Account) session.getAttribute("user");
	    if (user != null) {
	        model.addAttribute("user", user);
	    }

	    // Tìm kiếm user theo username
	    Optional<Account> acc = accService.findByUsername(userfind);
	    acc.ifPresentOrElse(
	        account -> model.addAttribute("acc", account),() -> model.addAttribute("acc", null) );
	    return "account/list_acc";
	}


	@PostMapping("/save")
	public ModelAndView saveOrUpdate(@RequestParam Long id, @RequestParam int status, ModelMap model) {
		Optional<Account> optAcc = accService.findById(id);
		Account acc = new Account();
		// Lưu đối tượng vào database
		if (optAcc.isPresent()) {
			Account entity = optAcc.get();

			BeanUtils.copyProperties(entity, acc);
		}
		acc.setStatus(status);
		accService.save(acc);

		return new ModelAndView("redirect:/admin/account", model);
	}

	@GetMapping("/edit/{id}")
	public ModelAndView edit(ModelMap model, @PathVariable("id") Long userId) {
		
		
		//session
		Account user = (Account) session.getAttribute("user"); // Lấy đối tượng user từ session
	    if (user != null && user instanceof Account) {
	        model.addAttribute("user", user);
	    }
		Optional<Account> optCategory = accService.findById(userId);
		Account acc = new Account();

		if (optCategory.isPresent()) {
			Account entity = optCategory.get();

			BeanUtils.copyProperties(entity, acc);

			model.addAttribute("acc", acc);

			return new ModelAndView("account/edit_account_admin.html", model);
		}
		model.addAttribute("message", "Account is not existed");
		return new ModelAndView("redirect:/admin/account", model);
	}

	@GetMapping("/delete/{id}")
	public ModelAndView delete(ModelMap model, @PathVariable("id") Long Id) {
		accService.deleteById(Id);
		model.addAttribute("message", "Account is deleted");
		return new ModelAndView("redirect:/admin/account", model);
	}
}
