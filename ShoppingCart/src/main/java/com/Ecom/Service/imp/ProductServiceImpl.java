package com.Ecom.Service.imp;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.Ecom.Model.Product;
import com.Ecom.Repository.ProductRepository;
import com.Ecom.Service.ProductService;

@Service 
public class ProductServiceImpl implements ProductService
{
	@Autowired
	private ProductRepository productrepository;

	public Product saveProduct(Product product) 
	{
		return productrepository.save(product);
	}  

	
	public List<Product> getAllProducts() 
	{
		return productrepository.findAll();
	}


	
	public Boolean deleteProduct(Integer id) 
	{
		Product product=productrepository.findById(id).orElse(null);
		
		if(!ObjectUtils.isEmpty(product))
		{
			productrepository.delete(product);
			return true;
		}
			return false;
		
	}


	public Product getProductById(Integer id) 
	{
		Product product=productrepository.findById(id).orElse(null);
		return product;
	}


	public Product updateProduct(Product product,MultipartFile image) 
	{
		Product dbproduct=getProductById(product.getId());
	    	
	    String imageName=image.isEmpty() ? dbproduct.getImage() : image.getOriginalFilename();
	    
	    dbproduct.setTitle(product.getTitle());
	    dbproduct.setDescription(product.getDescription());
	    dbproduct.setCategory(product.getCategory());
	    dbproduct.setPrice(product.getPrice());
	    dbproduct.setStock(product.getStock());
	    dbproduct.setImage(imageName);
	    dbproduct.setIsActive(product.getIsActive());
	    dbproduct.setDiscount(product.getDiscount());
	    Double discount=product.getPrice()*(product.getDiscount()/100.0);
	    Double discountProduct=product.getPrice()-discount;
	    dbproduct.setDiscountPrice(discountProduct);
	   Product updateProduct= productrepository.save(dbproduct);
	   
	   
	   if(!ObjectUtils.isEmpty(updateProduct))
	   {
		   if(!image.isEmpty())
		   {
			   try
			   {
			   File saveFile=new ClassPathResource("static/img").getFile();
			   Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+"product_img"+File.separator+image.getOriginalFilename());
			   Files.copy(image.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			   }		
			   catch(Exception e)
			   {
				   e.printStackTrace();
			   }
		   }
		   return product;
	   }
	   
		return null;
	}


	
	public List<Product> getAllActiveProducts(String category) 
	{
		List<Product> products=null;
		if(ObjectUtils.isEmpty(category))
		{
			products=productrepository.findByIsActiveTrue();
		}
		else
		{
			products=productrepository.findByCategory(category);
		}
		
		return products;
	}
		
}
