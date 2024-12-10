package IOT_house.controllers.admin;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import IOT_house.entity.Account;
import IOT_house.entity.Houses;
import IOT_house.services.admin.AccountService;
import IOT_house.services.admin.HouseService;
import IOT_house.services.user.impl.UserService;


@Controller
@RequestMapping("/admin")
public class HouseController {
	@Autowired
	HouseService houseService;
	@Autowired
	AccountService accService;
	@Autowired
	UserService userService;

	@GetMapping("/list-house/{id}")
	public String all(@PathVariable Long id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username=authentication.getName();
	    Account user = userService.findbyUser(username);
	    if (user != null && user instanceof Account) {
	        model.addAttribute("user", user);
	    }
        // Kiểm tra nếu `id` tồn tại
        Optional<Account> acc = accService.findById(id);
        if (acc.isPresent()) {
            List<Houses> houses = houseService.findByAccount(acc.get());  // Sử dụng hàm findByAccount đã tạo trước đó
            model.addAttribute("houses", houses);
            model.addAttribute("id_acc", id);
        } else {
            model.addAttribute("message", "Account not found");
        }
        return "list-house/list_house_of_acc.html"; // Trả về trang list.html
    }
	
	@GetMapping("/list-house/add/{id}")
	public String add(@PathVariable Long id,Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username=authentication.getName();
	    Account user = userService.findbyUser(username);
	    if (user != null && user instanceof Account) {
	        model.addAttribute("user", user);
	    }
		  Houses listHouse = new Houses();
		  model.addAttribute("id_acc", id);
		  model.addAttribute("house",listHouse); 
		  listHouse.setIsEdit(false);
		  return "list-house/add_edit_house.html";
	}
	
	@PostMapping("/list-house/save/{id_acc}")
	public ModelAndView saveOrUpdate(@PathVariable Long id_acc, ModelMap model,
	        @Valid @ModelAttribute("house") Houses houseModel, BindingResult result,
	        @RequestParam("imageFile") MultipartFile imageFile) {

	    // If validation errors occur, return to the form page with error messages
	    if (result.hasErrors()) {
	        return new ModelAndView("admin/list-house", model);
	    }

	    // Retrieve the account by ID
	    Optional<Account> acc = accService.findById(id_acc);

	    if (!acc.isPresent()) {
	        model.addAttribute("message", "Account not found");
	        return new ModelAndView("/admin/list-house/{id_acc}", model);
	    }
	    // Create a new house object
	    Houses house = new Houses();

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
	            houseModel.setImage(fname);
	        }

	        // Copy properties from the form data (cateModel) to the new house object
	        BeanUtils.copyProperties(houseModel, house);

	        // Set the account reference on the house object
	        house.setAcc(acc.get());  // Linking the house to the account

	        // Save the house to the database
	        houseService.save(house);

	        model.addAttribute("message", "House saved successfully");

	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("message", "Error saving House");
	    }

	    // Determine if the category was saved or edited
	    String message = houseModel.getIsEdit() ? "House is EDIT" : "House is SAVE";
	    model.addAttribute("message", message);

	    // Redirect to the list of houses after saving the house
	    return new ModelAndView("redirect:/admin/list-house/{id_acc}", model);  // Redirect to the list page without specific id
	}

	
	@GetMapping("/list-house/edit/{id}")
	public ModelAndView edit (ModelMap model,@PathVariable("id") String houseId) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username=authentication.getName();
	    Account user = userService.findbyUser(username);
	    if (user != null && user instanceof Account) {
	        model.addAttribute("user", user);
	    }
		Optional<Houses> optHouse =houseService.findById(houseId);
		Houses cateModel = new Houses();
		
		if (optHouse.isPresent()) {
			Houses entity =optHouse.get();
			
			
			BeanUtils.copyProperties(entity, cateModel);
			cateModel.setIsEdit(true);
			Long id = cateModel.getAcc().getId();
			
			model.addAttribute("house",cateModel);
			model.addAttribute("id_acc",id);
			return new ModelAndView("list-house/add_edit_house.html",model);
		}
		model.addAttribute("message","House is not existed");
		return new ModelAndView("redirect:/admin/list-house",model);
	}
	@GetMapping("/list-house/delete/{id}")
	public ModelAndView delete(ModelMap model, @PathVariable("id") String houseId) {
	    Optional<Houses> optHouse = houseService.findById(houseId);

	    if (optHouse.isPresent()) {
	        // Xóa nhà nếu tồn tại
	        houseService.deleteById(houseId);
	        
	        Houses entity = optHouse.get(); // Lấy entity sau khi xóa
	        Long id = entity.getAcc().getId();

	        model.addAttribute("message", "House is deleted");
	        model.addAttribute("id_acc", id);
	    } else {
	        // Nếu không tìm thấy house, xử lý trường hợp lỗi
	        model.addAttribute("message", "House not found");
	    }

	    return new ModelAndView("redirect:/admin/list-house/{id_acc}", model);
	}
	
	@GetMapping("/house")
	public String home(HttpSession session, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username=authentication.getName();
	    Account user = userService.findbyUser(username);
	    if (user != null && user instanceof Account) {
	        model.addAttribute("user", user);
	    }
	    
	    List<Houses> houses = houseService.findAll();
	    model.addAttribute("houses", houses);

	    return "list-house/home_admin_temp.html";
	}
	
	@GetMapping("/house/find")
	public String findidhouse(Model model, @RequestParam String idhousefind) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username=authentication.getName();
	    Account user = userService.findbyUser(username);
	    if (user != null && user instanceof Account) {
	        model.addAttribute("user", user);
	    }
	    
	    Optional<Houses> houses = houseService.findById(idhousefind);
	    model.addAttribute("houses", houses.get());

	    return "list-house/home_admin_temp.html";
	}


}