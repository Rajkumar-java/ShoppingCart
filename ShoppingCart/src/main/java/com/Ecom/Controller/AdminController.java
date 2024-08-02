package com.Ecom.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Ecom.Model.Category;
import com.Ecom.Model.Product;
import com.Ecom.Service.CategoryService;
import com.Ecom.Service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController 
{
	@Autowired
	private CategoryService categoryservice;
	
	@Autowired
	private ProductService productservice;
  
	@GetMapping("/")
	public String index()
	{
		return "admin/index";
	}
	
	@GetMapping("/loadAddProduct")
	public String loadAddProduct(Model m)
	{
		List<Category> categories=categoryservice.getAllCategory();
		m.addAttribute("categories",categories);
		return "admin/add_product";
	}
	
	@GetMapping("/category")
	public String category(Model m)
	{
		m.addAttribute("categorys",categoryservice.getAllCategory());
		return "admin/category";
	}
	
	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException
	{
		
		String imageName=file !=null ? file.getOriginalFilename():"default.jpg";
		category.setImageName(imageName);
		
		Boolean existsCategory=categoryservice.existCategory(category.getName());
		if(existsCategory)
		{
			session.setAttribute("errorMsg","category name already exists");
		}
		else
		{
			Category savedcategory=categoryservice.saveCategory(category);
			
			// savecategory is not null
			
			if(ObjectUtils.isEmpty(savedcategory))
			{
				session.setAttribute("errorMsg","not saved ! internal server error");
			}
			else
			{
				File saveFile=new ClassPathResource("static/img").getFile();
				
			Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+"category_img"+File.separator+file.getOriginalFilename());
				
				// System.out.println(path);
				
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				
				session.setAttribute("succMsg","saved successfully");
			}
		}
		return "redirect:/admin/category";
	}
	
	
	@GetMapping("/deleteCategory/{id}")
	public String deleteCategory(@PathVariable("id") int id,HttpSession session)
	{
		Boolean deleteCategory=categoryservice.deleteCategory(id);
		if(deleteCategory)
		{
			session.setAttribute("succMsg","category delete successfully");
		}
		else
		{
			session.setAttribute("errorMsg","something went wrong on server");
		}
		return "redirect:/admin/category";
		
	}
	
	@GetMapping("/loadEditCategory/{id}")
     public String loadEditCategory(@PathVariable int id,Model m)
     {
		m.addAttribute("category",categoryservice.getCategoryById(id));
    	 return "admin/edit_category";
     }
	
	
	@PostMapping("/updateCategory")
	public String updateCategory(@ModelAttribute Category category,@RequestParam("file") MultipartFile file,HttpSession session) throws IOException
	{
		Category oldCategory=categoryservice.getCategoryById(category.getId());
		String imageName=file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();
		if(!ObjectUtils.isEmpty(category))
		{
			oldCategory.setName(category.getName());
			oldCategory.setIsActive(category.getIsActive());
			oldCategory.setImageName(imageName);
				
		} 
		Category updateCategory=categoryservice.saveCategory(oldCategory);
		
		if(!ObjectUtils.isEmpty(updateCategory))
		{
			
			if(!file.isEmpty())
			{
				File saveFile=new ClassPathResource("static/img").getFile();
				
				Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+"category_img"+File.separator+file.getOriginalFilename());
					
					// System.out.println(path);
					
					Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			}
			
			
			session.setAttribute("succMsg","category update successfully");
		}
		else
		{
			session.setAttribute("errorMsg","something went wrong on server");
		}
		
		return "redirect:/admin/loadEditCategory/"+category.getId();
	}
	
	
	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image, HttpSession session) throws IOException
	{
		String imageName=image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
		product.setImage(imageName);
		product.setDiscount(0);
		product.setDiscountPrice(product.getPrice());
		
		Product saveProduct=productservice.saveProduct(product);
		
		if(!ObjectUtils.isEmpty(saveProduct))
		{
			
			File saveFile=new ClassPathResource("static/img").getFile();
			
			Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+"product_img"+File.separator+image.getOriginalFilename());
				
				// System.out.println(path);
				
				Files.copy(image.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			
			session.setAttribute("succMsg","product saved successfully");
		}
		else
		{
			session.setAttribute("errorMsg","something went wrong on server");
		}
		
		return "redirect:/admin/loadAddProduct";
	}
	
	 @GetMapping("/products") 
	public String loadViewProduct(Model m)
	{
		 m.addAttribute("products",productservice.getAllProducts());
		return "admin/products";
	}
	 
	 
	 @GetMapping("/deleteProduct/{id}")
	 public String deleteProduct(@PathVariable("id") int id, HttpSession session) {
	     Boolean deleteProduct = productservice.deleteProduct(id);
	     if (deleteProduct) {
	         session.setAttribute("succMsg", "Product deleted successfully");
	     } else {
	         session.setAttribute("errorMsg", "Something went wrong on the server");
	     } 
	     return "redirect:/admin/products";
	 }
	 
	 
	    @GetMapping("/editProduct/{id}") 
		public String editProduct(@PathVariable int id,Model m)
		{
	    	m.addAttribute("product",productservice.getProductById(id));
	    	m.addAttribute("categories",categoryservice.getAllCategory());
			 
			return "admin/edit_product";
		}
		 
	        @PostMapping("/updateProduct") 
	  		public String updateProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile image,HttpSession session,Model m)
	  		{
	        	
	        	if(product.getDiscount()<0 || product.getDiscount()>100)
	        	{
	        		 session.setAttribute("errorMsg", "invalid discount");
	        	}
	        	else
	        	{
	        	
	        	Product updateProduct=productservice.updateProduct(product, image);	
	        	
	        	if(!ObjectUtils.isEmpty(updateProduct))
	        	{
	        		 session.setAttribute("succMsg", "Product update successfully");
	        	}
	        	else
	        	{
	        		 session.setAttribute("errorMsg", "Something went wrong on the server");
	        	}
	        	}
	  			return "redirect:/admin/editProduct/"+product.getId() ;
	  		}
	  		
	 
}
