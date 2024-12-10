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

import IOT_house.entity.Account;
import IOT_house.entity.Equipments;
import IOT_house.entity.Houses;
import IOT_house.services.admin.EquipmentService;
import IOT_house.services.admin.HouseService;
import IOT_house.services.user.impl.UserService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/equipments")
public class EquipmentController {
	@Autowired
	EquipmentService equipService;
	@Autowired
	HouseService houseService;
	@Autowired
	UserService userService;
	
	
	@GetMapping("/{id}")
	public String find_id(@PathVariable String id, Model model) {
		
		//session
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username=authentication.getName();
	    Account user = userService.findbyUser(username);
	    if (user != null && user instanceof Account) {
	        model.addAttribute("user", user);
	    }
		// Trực tiếp lấy nhà theo id
		List<Equipments> equip = equipService.findByHouseId(id); // Lấy danh sách Equipments theo id_house
		model.addAttribute("equip", equip); // Thêm danh sách Equipments vào model
		model.addAttribute("idHouse", id); // Thêm id_house vào model
		return "equip/list_equip_of_house.html"; // Trả về trang list.html
	}

	@GetMapping("/add/{id}")
	public String add(@PathVariable String id,Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username=authentication.getName();
	    Account user = userService.findbyUser(username);
	    if (user != null && user instanceof Account) {
	        model.addAttribute("user", user);
	    }
		  Equipments listequip = new Equipments();
		  model.addAttribute("idHouse", id);
		  model.addAttribute("house",listequip); 
		  listequip.setIsEdit(false);
		  return "equip/add_edit_equip.html";
	}

	@PostMapping("/save/{idHouse}")
	public ModelAndView saveOrUpdate(@PathVariable String idHouse,ModelMap model,
			@Valid @ModelAttribute("house") Equipments cateModel, BindingResult result,
			@RequestParam("imageFile") MultipartFile imageFile) {

		// If validation errors occur, return to the form page with error messages
		if (result.hasErrors()) {
			return new ModelAndView("equip/add", model);
		}
		Optional<Houses> house = houseService.findById(idHouse);
		if (!house.isPresent()) {
	        model.addAttribute("message", "Account not found");
	        return new ModelAndView("/admin/equiments/{idHouse}", model);
	    }
		// Create a new equiments object
		Equipments equip = new Equipments();

		// Define the upload path for images
		String uploadPath = "D:\\upload";
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir(); // Create directory if not exists
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

			// Copy properties from the form data (cateModel) to the new house object
			BeanUtils.copyProperties(cateModel, equip);
			equip.setHouse(house.get());
			
			// Save the house to the database
			equipService.save(equip);

			model.addAttribute("message", "Equipment saved successfully");

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "Error saving Equipment");
		}

		// Determine if the category was saved or edited
		String message = cateModel.getIsEdit() ? "Equipment is EDIT" : "Equipment is SAVE";
		model.addAttribute("message", message);

		// Redirect to the list of houses after saving the house
		return new ModelAndView("redirect:/admin/equipments/{idHouse}", model); // Redirect to the list page without
																				// specific id
	}
	@GetMapping("/edit/{id}")
	public ModelAndView edit (ModelMap model,@PathVariable("id") Long Id) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username=authentication.getName();
	    Account user = userService.findbyUser(username);
	    if (user != null && user instanceof Account) {
	        model.addAttribute("user", user);
	    }
		Optional<Equipments> optEquip =equipService.findById(Id);
		Equipments equipModel = new Equipments();
		if (optEquip.isPresent()) {
			Equipments entity =optEquip.get();
			BeanUtils.copyProperties(entity, equipModel);
			equipModel.setIsEdit(true);
			String id = equipModel.getHouse().getIdHouse();
			
			model.addAttribute("equip",equipModel);
			model.addAttribute("idHouse",id);
			return new ModelAndView("equip/add_edit_equip.html",model);
		}
		model.addAttribute("message","Equipment is not existed");
		return new ModelAndView("redirect:/admin/equipment",model);
	}

	@GetMapping("/delete/{id}")
	public ModelAndView delete(ModelMap model, @PathVariable("id") Long Id) {
	    Optional<Equipments> optequip= equipService.findById(Id);

	    if (optequip.isPresent()) {
	        // Xóa nhà nếu tồn tại
	        equipService.deleteById(Id);
	        
	        Equipments entity = optequip.get(); // Lấy entity sau khi xóa
	        String id = entity.getHouse().getIdHouse();

	        model.addAttribute("message", "Equipment is deleted");
	        model.addAttribute("idHouse", id);
	    } else {
	        // Nếu không tìm thấy house, xử lý trường hợp lỗi
	        model.addAttribute("message", "House not found");
	    }

	    return new ModelAndView("redirect:/admin/equipments/{idHouse}", model);
	}
	
	

}
