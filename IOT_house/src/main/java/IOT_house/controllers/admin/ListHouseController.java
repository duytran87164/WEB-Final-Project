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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import IOT_house.entity.Account;
import IOT_house.entity.Houses;
import IOT_house.services.admin.AccountService;
import IOT_house.services.admin.HouseService;


@Controller
@RequestMapping("/admin/list-house")
public class ListHouseController {
	@Autowired
	HouseService houseService;
	@Autowired
	AccountService accService;
	@GetMapping("/{id}")
	public String all(@PathVariable Long id, Model model) {
        // Kiểm tra nếu `id` tồn tại
        Optional<Account> acc = accService.findById(id);
        if (acc.isPresent()) {
            List<Houses> houses = houseService.findByAccount(acc.get());  // Sử dụng hàm findByAccount đã tạo trước đó
            model.addAttribute("houses", houses);
            model.addAttribute("id_acc", id);
        } else {
            model.addAttribute("message", "Account not found");
        }
        return "list-house/list.html"; // Trả về trang list.html
    }
	
	@GetMapping("/add/{id}")
	public String add(@PathVariable Long id,Model model) {
		  Houses listHouse = new Houses();
		  model.addAttribute("id_acc", id);
		  model.addAttribute("house",listHouse); 
		  listHouse.setIsEdit(false);
		  return "list-house/add.html";
	}
	
	@PostMapping("/save/{id_acc}")
	public ModelAndView saveOrUpdate(@PathVariable Long id_acc, ModelMap model,
	        @Valid @ModelAttribute("house") Houses cateModel, BindingResult result,
	        @RequestParam("imageFile") MultipartFile imageFile) {

	    // If validation errors occur, return to the form page with error messages
	    if (result.hasErrors()) {
	        return new ModelAndView("category/add", model);
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
	    String uploadPath = "E:\\upload";
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

	        // Copy properties from the form data (cateModel) to the new house object
	        BeanUtils.copyProperties(cateModel, house);

	        // Set the account reference on the house object
	        house.setAcc(acc.get());  // Linking the house to the account

	        // Save the house to the database
	        houseService.save(house);

	        model.addAttribute("message", "Category saved successfully");

	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("message", "Error saving category");
	    }

	    // Determine if the category was saved or edited
	    String message = cateModel.getIsEdit() ? "Category is EDIT" : "Category is SAVE";
	    model.addAttribute("message", message);

	    // Redirect to the list of houses after saving the house
	    return new ModelAndView("redirect:/admin/list-house/{id_acc}", model);  // Redirect to the list page without specific id
	}

	
	@GetMapping("/edit/{id}")
	public ModelAndView edit (ModelMap model,@PathVariable("id") String houseId) {
		Optional<Houses> optHouse =houseService.findById(houseId);
		Houses cateModel = new Houses();
		
		if (optHouse.isPresent()) {
			Houses entity =optHouse.get();
			
			
			BeanUtils.copyProperties(entity, cateModel);
			cateModel.setIsEdit(true);
			Long id = cateModel.getAcc().getId();
			
			model.addAttribute("house",cateModel);
			model.addAttribute("id_acc",id);
			return new ModelAndView("list-house/add.html",model);
		}
		model.addAttribute("message","House is not existed");
		return new ModelAndView("redirect:/admin/list-house",model);
	}
	@GetMapping("/delete/{id}")
	public ModelAndView delete(ModelMap model, @PathVariable("id") String houseId) {
	    Optional<Houses> optHouse = houseService.findById(houseId);

	    if (optHouse.isPresent()) {
	        // Xóa nhà nếu tồn tại
	        houseService.deleteById(houseId);
	        
	        Houses entity = optHouse.get(); // Lấy entity sau khi xóa
	        Long id = entity.getAcc().getId();

	        model.addAttribute("message", "Category is deleted");
	        model.addAttribute("id_acc", id);
	    } else {
	        // Nếu không tìm thấy house, xử lý trường hợp lỗi
	        model.addAttribute("message", "House not found");
	    }

	    return new ModelAndView("redirect:/admin/list-house/{id_acc}", model);
	}

//	@RequestMapping("/searchpaginated")
//	  
//	  public String search(ModelMap model,
//	  
//	  @RequestParam(name="name",required = false) String name,
//	  
//	  @RequestParam("page") Optional<Integer> page,
//	  
//	  @RequestParam("size") Optional<Integer> size) {
//	  
//	  int count = (int) categoryService.count(); int currentPage = page.orElse(1);
//	  
//	  int pageSize = size.orElse(3);
//	  
//	  Pageable pageable = PageRequest.of(currentPage-1, pageSize, Sort.by("name"));
//	  Page<Category> resultPage = null; if(StringUtils.hasText(name)) { resultPage
//	  = categoryService.findByNameContaining(name,pageable);
//	  model.addAttribute("name",name); } else { resultPage =
//	  categoryService.findAll(pageable); } int totalPages =
//	  resultPage.getTotalPages(); if(totalPages > 0) { int start = Math.max(1,
//	  currentPage-2); int end = Math.min(currentPage + 2, totalPages);
//	  if(totalPages > count) { if(end == totalPages) start = end - count; else if
//	  (start == 1) end = start + count; } List<Integer> pageNumbers =
//	  IntStream.rangeClosed(start, end) .boxed() .collect(Collectors.toList());
//	  model.addAttribute("pageNumbers",pageNumbers); }
//	  model.addAttribute("categoryPage",resultPage); return
//	  "admin/category/searchpaginated"; }
}