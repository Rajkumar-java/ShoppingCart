package com.Ecom.Service.imp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.Ecom.Model.Category;
import com.Ecom.Repository.CategoryRepository;
import com.Ecom.Service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService
{
	@Autowired
	private CategoryRepository categoryrepository;

	
	public Category saveCategory(Category category) 
	{
		Category savecategory=categoryrepository.save(category);
		return savecategory;
	}
     
	
	public Boolean existCategory(String name) 
	{
		return categoryrepository.existsByName(name);
	}
	
	public List<Category> getAllCategory() 
	{
		List<Category> getallcategory=categoryrepository.findAll();
		return getallcategory;
	}


	
	public Boolean deleteCategory(int id) 
	{
		Category category=categoryrepository.findById(id).orElse(null);
		
		if(!ObjectUtils.isEmpty(category))
		{
			categoryrepository.delete(category);
			return true;
		}
		
		return false;
	}


	public Category getCategoryById(int id) 
	{
		Category category=categoryrepository.findById(id).orElse(null);
		return category;
	}


	
	public List<Category> getAllActiveCategory() 
	{
		
		List<Category> categories=categoryrepository.findByIsActiveTrue();
		return categories;
	}

}
