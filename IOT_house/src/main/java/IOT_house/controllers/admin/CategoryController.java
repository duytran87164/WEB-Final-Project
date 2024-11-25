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
import IOT_house.entity.Category;
import IOT_house.services.admin.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
	@Autowired
	CategoryService categoryService;

	@RequestMapping("")
	public String all(Model model) {
		List<Category> list = categoryService.findAll();
		model.addAttribute("listcate", list);

		return "category/list.html";
	}
	
	@GetMapping("/add")
	public String add(Model model) {

		  Category category = new Category(); 
		  model.addAttribute("cate",category); 
		  category.setIsEdit(false);
			return "category/add.html";
	}
	
	@PostMapping("/save")
	public ModelAndView saveOrUpdate (ModelMap model,
			@Valid @ModelAttribute("category") Category cateModel, BindingResult result,
			@RequestParam("imageFile") MultipartFile imageFile) {
//		if(result.hasErrors()) {
//			return new ModelAndView("category/add.html");
//		}
		Category entity = new Category();
		String uploadPath = "E:\\upload";
	    File uploadDir = new File(uploadPath);
	    if (!uploadDir.exists()) {
	        uploadDir.mkdir();  // Tạo thư mục nếu chưa tồn tại
	    }
	    try {
	        // Kiểm tra nếu người dùng tải tệp ảnh lên
	        if (!imageFile.isEmpty()) {
	            String originalFilename = imageFile.getOriginalFilename();
	            int index = originalFilename.lastIndexOf(".");
	            String ext = originalFilename.substring(index + 1);

	            // Đổi tên tệp để tránh trùng lặp
	            String fname = System.currentTimeMillis() + "." + ext;

	            // Lưu tệp vào thư mục
	            imageFile.transferTo(new File(uploadPath + "/" + fname));

	            // Cập nhật đường dẫn ảnh vào đối tượng category
	            cateModel.setImage(fname);

	        }
	        // Lưu đối tượng vào database
			BeanUtils.copyProperties(cateModel,entity);
			categoryService.save(entity);

	        model.addAttribute("message", "Category saved successfully");
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("message", "Error saving category");
	    }

		String message="";
		if(cateModel.getIsEdit()==true) {
		message="Category is EDIT";
		}
		else {
			message="Category is SAVE";
		}
		model.addAttribute("message", message);
		return new ModelAndView("redirect:/admin/categories",model);
	}
	
	@GetMapping("/edit/{id}")
	public ModelAndView edit (ModelMap model,@PathVariable("id") Long categoryId) {
		Optional<Category> optCategory =categoryService.findById(categoryId);
		Category cateModel = new Category();
		
		if (optCategory.isPresent()) {
			Category entity =optCategory.get();
			
			BeanUtils.copyProperties(entity, cateModel);
			cateModel.setIsEdit(true);
			
			model.addAttribute("cate",cateModel);
			
			return new ModelAndView("category/add.html",model);
		}
		model.addAttribute("message","Category is not existed");
		return new ModelAndView("redirect:/admin/categories",model);
	}
	
	@GetMapping("/delete/{id}")
	public ModelAndView delete(ModelMap model, @PathVariable("id") Long categoryId) {
			categoryService.deleteById(categoryId);
			model.addAttribute("message", "Category is deleted");
			return new ModelAndView("redirect:/admin/categories", model);
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
