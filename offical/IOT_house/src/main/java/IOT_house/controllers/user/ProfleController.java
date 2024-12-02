package IOT_house.controllers.user;

import java.io.File;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import IOT_house.entity.Houses;
import IOT_house.services.admin.AccountService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/profile")
public class ProfleController {
	
	@Autowired
    private HttpSession session;
	@Autowired
	AccountService accService;
	
	@GetMapping("/edit/{id}")
	public ModelAndView edit(ModelMap model, @PathVariable("id") Long userId) {
		
		//session
		Account user = (Account) session.getAttribute("user"); // Lấy đối tượng user từ session
	    if (user != null && user instanceof Account) {
	        model.addAttribute("fullname",user.getFullName());
	        model.addAttribute("user", user);
	    } else {
	        model.addAttribute("fullname", "err");
	    }
		
		Account acc = new Account();

			BeanUtils.copyProperties(user, acc);

			model.addAttribute("acc", acc);
			model.addAttribute("id_acc", acc.getId());

			return new ModelAndView("account/edit_profile.html", model);

	}
	
	
	@PostMapping("/save")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("acc") Account cateModel, 
			BindingResult result,HttpSession session,
			@RequestParam("imageFile") MultipartFile imageFile) {
	    if (result.hasErrors()) {
	        // Nếu có lỗi xác thực, trả về lại form với các lỗi hiển thị
	        return new ModelAndView("account/edit_profile", model);
	    }

	    Account acc = new Account();

	 // Define the upload path for images
	    String uploadPath = "D:\\upload";
	    File uploadDir = new File(uploadPath);
	    if (!uploadDir.exists()) {
	        uploadDir.mkdir();  // Create directory if not exists
	    }

	    try {
	        // If the user uploaded an image, handle file upload
	        if (!imageFile.isEmpty()) {
	            String originalFilename = imageFile.getOriginalFilename();
	            int index = originalFilename.lastIndexOf(".");
	            String ext = originalFilename.substring(index + 1);

	            // Generate a unique filename for the uploaded image
	            String fname = System.currentTimeMillis() + "." + ext;

	            // Save the image to the specified directory
	            imageFile.transferTo(new File(uploadPath + "/" + fname));

	            // Update the category object with the image filename
	            cateModel.setImage(fname);
	        }
		BeanUtils.copyProperties(cateModel, acc);
	    acc.setStatus(1);
	    
	    accService.save(acc);
	    session.setAttribute("user", acc);
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("message", "Error saving category");
	    }
	    return new ModelAndView("redirect:/user/home", model);
	}


}
