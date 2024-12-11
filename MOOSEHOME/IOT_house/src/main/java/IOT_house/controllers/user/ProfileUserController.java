package IOT_house.controllers.user;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.io.File;
import java.util.Collections;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import IOT_house.entity.Account;
import IOT_house.entity.Roles;
import IOT_house.repository.RoleRepository;
import IOT_house.services.admin.AccountService;
import IOT_house.services.user.impl.UserService;
import jakarta.validation.Valid;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/user/profile")

public class ProfileUserController {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserService userService;
	@Autowired
	AccountService accService;
	@Autowired
	RoleRepository roleRepository;
	
	@GetMapping("/edit/{id}")
	public ModelAndView edit(ModelMap model, @PathVariable("id") Long userId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Account user = userService.findbyUser(username);
	    if (user != null && user instanceof Account) {
	        model.addAttribute("fullname", user.getFullName());
	        model.addAttribute("user", user);
	    }
	    Account acc = new Account();
	    BeanUtils.copyProperties(user, acc);
	    model.addAttribute("acc", acc);
	    model.addAttribute("id_acc", acc.getId());
	    return new ModelAndView("account/edit_profile.html", model);
	}
	
	@PostMapping("/save")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("acc") Account cateModel, 
	        BindingResult result,
	        @RequestParam("imageFile") MultipartFile imageFile) {
	    if (result.hasErrors()) {
	        return new ModelAndView("account/edit_profile", model);
	    }
	    Account acc = new Account();
	 // Define the upload path for images
	    String uploadPath = "D:\\upload";
	    File uploadDir = new File(uploadPath);
	    if (!uploadDir.exists()) {
	        uploadDir.mkdir();
	    }
	    try {
	        // Xử lý hình ảnh nếu có
	        if (!imageFile.isEmpty()) {
	            String originalFilename = imageFile.getOriginalFilename();
	            int index = originalFilename.lastIndexOf(".");
	            String ext = originalFilename.substring(index + 1);

	            String fname = System.currentTimeMillis() + "." + ext;
	            imageFile.transferTo(new File(uploadPath + "/" + fname));
	            cateModel.setImage(fname);
	        }
	        // Sao chép thông tin từ cateModel vào đối tượng acc
	        if (!cateModel.getPassword().equals(userService.findbyUser(cateModel.getUsername()).getPassword())) {
	        	String hashedPassword = passwordEncoder.encode(cateModel.getPassword());
				cateModel.setPassword(hashedPassword);
			}
	        BeanUtils.copyProperties(cateModel, acc);
	        
	        acc.setStatus(true); // Đảm bảo người dùng không bị khóa

	        // Lấy role và gán cho người dùng
	        Roles role = roleRepository.findByName("USER").get();
	        acc.setRoles(Collections.singleton(role));

	        // Lưu người dùng vào cơ sở dữ liệu
	        accService.save(acc);

	        // Cập nhật thông tin authentication trong Spring Security
	        Authentication authentication = new UsernamePasswordAuthenticationToken(
	                cateModel.getUsername(),cateModel.getPassword(),cateModel.getAuthorities());

	        // Cập nhật authentication vào SecurityContext
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	     
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("message", "Error saving profile");
	    }

	    return new ModelAndView("redirect:/user/home", model);
	}



}
