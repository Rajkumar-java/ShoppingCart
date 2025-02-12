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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Ecom.Model.Category;
import com.Ecom.Model.Product;
import com.Ecom.Model.UserDtls;
import com.Ecom.Service.CategoryService;
import com.Ecom.Service.ProductService;
import com.Ecom.Service.UserService;

import jakarta.servlet.http.HttpSession;




@Controller
public class HomeController 
{
		@Autowired
		private CategoryService categoryservice;
		
		@Autowired
		private ProductService productservice;
		
		@Autowired
		private UserService userService;
		
		 @GetMapping
	     public String index()
	     {
	    	 return "INDEX";
	     }
		
		@GetMapping("/signin")
	    public String login()
	    {
	   	 return "login";
	    }
		
		@GetMapping("/register")
	    public String register()
	    {
	   	 return "REGISTER";
	    }
		
		@GetMapping("/products")
	    public String products(Model m, @RequestParam (value="category",defaultValue = "") String category)
	    {
			//System.out.println(category+"=category");
			List<Category> categories=categoryservice.getAllActiveCategory();
			List<Product> products=productservice.getAllActiveProducts(category);
			m.addAttribute("categories",categories);
			m.addAttribute("products",products);
			m.addAttribute("paramValue",category);
			
	   	 return "product";
	    }
		
		@GetMapping("/product/{id}")
	    public String product(@PathVariable int id, Model m)
	    {
			Product productById=productservice.getProductById(id);
			m.addAttribute("product", productById);
	   	 return "VIEW_PRODUCT";
	    }
		
		@PostMapping("/saveUser")
		public String savaUser(@ModelAttribute UserDtls user,@RequestParam("img") MultipartFile file, HttpSession session) throws IOException
		{
		    String imageName=file.isEmpty() ? "default.jpg": file.getOriginalFilename();
		    user.setProfileImage(imageName);
			UserDtls saveUser=userService.saveUser(user);
			
			if(!ObjectUtils.isEmpty(saveUser))
			{
				if(!file.isEmpty())
				{

					File saveFile=new ClassPathResource("static/img").getFile();
					
					Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+"profile_img"+File.separator+file.getOriginalFilename());
						
					  // System.out.println(path);
						
						Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				}
				
				session.setAttribute("succMsg","Register successfully");
			}
			
			else
			{
				session.setAttribute("errorMsg","something went wrong on server");
			}
			
			return "redirect:/register";
		}
		
		
		
		
		
		
		
    
}