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
import IOT_house.services.user.UserService;


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
<<<<<<< HEAD
	    // Lấy thông tin người dùng từ SecurityContext
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    
	    // Kiểm tra nếu người dùng đã đăng nhập
	    if (authentication != null && authentication.isAuthenticated()) {
	        String username = authentication.getName();
	        
	        // Tìm tài khoản người dùng từ cơ sở dữ liệu
	        Account user = userService.findbyUser(username);
	        
	        if (user != null) {
	            // Thêm thông tin người dùng vào model
	            model.addAttribute("fullname", user.getFullName());
	            model.addAttribute("user", user);
	        } else {
	            model.addAttribute("fullname", "User not found");
	        }
	    } else {
	        model.addAttribute("fullname", "User not authenticated");
=======
		//session
		Account user = (Account) session.getAttribute("user"); // Lấy đối tượng user từ session
	    if (user != null && user instanceof Account) {
	        model.addAttribute("user", user);
>>>>>>> 5dec53c57f841a0ff6f5c024f35820cca18357fc
	    }
	    
	    // Lấy danh sách tất cả tài khoản và thêm vào model
	    List<Account> list = accService.findAll();
	    model.addAttribute("acc", list);

	    // Trả về view để hiển thị
	    return "account/list_acc.html";
	}

	
	
	@GetMapping("/find")
	public String findUsername(Model model, @RequestParam String userfind) {
	    // Lấy user từ session
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username=authentication.getName();
	    Account user = userService.findbyUser(username);
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
<<<<<<< HEAD
	public ModelAndView saveOrUpdate(@RequestParam Long id, @RequestParam boolean status, ModelMap model) {
//		if(result.hasErrors()) {
//			return new ModelAndView("category/add.html");
//		}
=======
	public ModelAndView saveOrUpdate(@RequestParam Long id, @RequestParam int status, ModelMap model) {
>>>>>>> 5dec53c57f841a0ff6f5c024f35820cca18357fc
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
<<<<<<< HEAD
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username=authentication.getName();
	    Account user = userService.findbyUser(username);
			    if (user != null && user instanceof Account) {
			        model.addAttribute("fullname",user.getFullName());
			        model.addAttribute("user", user);
			    } else {
			        model.addAttribute("fullname", "err");
			    }
=======
		Account user = (Account) session.getAttribute("user"); // Lấy đối tượng user từ session
	    if (user != null && user instanceof Account) {
	        model.addAttribute("user", user);
	    }
>>>>>>> 5dec53c57f841a0ff6f5c024f35820cca18357fc
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