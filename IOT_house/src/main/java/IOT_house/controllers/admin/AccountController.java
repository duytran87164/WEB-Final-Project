package IOT_house.controllers.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import IOT_house.repository.RoleRepository;
import IOT_house.services.admin.AccountService;
import IOT_house.services.user.impl.UserService;


@Controller
@RequestMapping("/admin/account")
public class AccountController {
	@Autowired
	AccountService accService;
	@Autowired
	UserService userService;
	@Autowired
	RoleRepository roleRepository;

	@RequestMapping("")
	public String all(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.isAuthenticated()) {
	        String username = authentication.getName();
	        Account user = userService.findbyUser(username);
	        
	        if (user != null) {
	            model.addAttribute("fullname", user.getFullName());
	            model.addAttribute("user", user);
	        } else {
	            model.addAttribute("fullname", "User not found");
	        }
	    } else {
	        model.addAttribute("fullname", "User not authenticated");
	    }
	    
	    List<Account> list = accService.findAll();
	    model.addAttribute("acc", list);

	    return "account/list_acc.html";
	}
	
	
	@GetMapping("/find")
	public String findUsername(Model model, @RequestParam String userfind) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username=authentication.getName();
	    Account user = userService.findbyUser(username);
	    if (user != null) {
	        model.addAttribute("user", user);
	    }

	    Optional<Account> acc = accService.findByUsername(userfind);
	    model.addAttribute("acc", acc.get());
	    return "account/list_acc";
	}

	@PostMapping("/save")
	public ModelAndView saveOrUpdate(@RequestParam Long id, @RequestParam boolean status, ModelMap model) {
		Optional<Account> optAcc = accService.findById(id);
		Account acc = new Account();
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
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username=authentication.getName();
	    Account user = userService.findbyUser(username);
			    if (user != null && user instanceof Account) {
			        model.addAttribute("fullname",user.getFullName());
			        model.addAttribute("user", user);
			    } else {
			        model.addAttribute("fullname", "err");
			    }
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
		Optional<Account> optAccount = accService.findById(Id);
		optAccount.get().getRoles().clear();
		accService.save(optAccount.get());
		accService.deleteById(Id);
		model.addAttribute("message", "Account is deleted");
		return new ModelAndView("redirect:/admin/account", model);
	}
}
