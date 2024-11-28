package IOT_house.controllers.admin;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import IOT_house.entity.Account;
import IOT_house.entity.Houses;
import IOT_house.services.admin.AccountService;
import IOT_house.services.user.UserService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/account")
public class AccountController {
	@Autowired
	AccountService accService;
	@RequestMapping("")
	public String all(Model model) {
		List<Account> list = accService.findAll();
		model.addAttribute("acc", list);

		return "account/list_acc_temp.html";
	}
	
	@PostMapping("/update")
	public ModelAndView saveOrUpdate (ModelMap model,
			BindingResult result,
			@PathVariable("id") Long userId,
			@PathVariable("status") int status
			) {
//		if(result.hasErrors()) {
//			return new ModelAndView("category/add.html");
//		}
		Optional<Account> optAcc = accService.findById(userId);
		Account acc = new Account();
        // Lưu đối tượng vào database
		if (optAcc.isPresent()) {
			Account entity =optAcc.get();
			
			BeanUtils.copyProperties(entity, acc);
		}
		acc.setStatus(status);
		accService.save(acc);


		String message="";
		message="Account is EDIT";

		model.addAttribute("message", message);
		return new ModelAndView("redirect:/admin/account",model);
	}
	
	@GetMapping("/edit/{id}")
	public ModelAndView edit (ModelMap model,@PathVariable("id") Long userId) {
		Optional<Account> optUser =accService.findById(userId);
		Account acc = new Account();
		
		if (optUser.isPresent()) {
			Account entity =optUser.get();
			
			BeanUtils.copyProperties(entity, acc);
			
			model.addAttribute("acc",acc);
			
			return new ModelAndView("account/edit.html",model);
		}
		model.addAttribute("message","Account is not existed");
		return new ModelAndView("redirect:/admin/account",model);
	}
	
	@GetMapping("/delete/{id}")
	public ModelAndView delete(ModelMap model, @PathVariable("id") Long categoryId) {
			accService.deleteById(categoryId);
			model.addAttribute("message", "Account is deleted");
			return new ModelAndView("redirect:/admin/account", model);
		}
}
