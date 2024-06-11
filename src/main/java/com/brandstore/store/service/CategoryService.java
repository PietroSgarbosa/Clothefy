package com.brandstore.store.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brandstore.store.dto.CategoryDTO;
import com.brandstore.store.entity.Category;
import com.brandstore.store.repository.CategoryRepository;
import com.brandstore.store.utils.CategoryMapper;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	public List<CategoryDTO> getAll() {
		try {
			List<Category> listCategory = getCategoryRepository().findAll();
			List<CategoryDTO> listCategoryDTO = new ArrayList<CategoryDTO>();
			
			if(listCategory.isEmpty() == false) {
				for(Category category : listCategory) {
					listCategoryDTO.add(CategoryDTO.convertToDTO(category));
				}
				return listCategoryDTO;
			}
			return listCategoryDTO;
			
		} catch(Exception e) {
			//Exceções não usam RETURN, apenas THROW.
			//Eu posso jogar a exceção padrão (e);
			//Eu posso jogar uma exceção especifica que são reservadas do java;
			//Eu posso jogar uma exception customizada.
			throw e;
		}
	}
	
	public String create(CategoryDTO categoryDTO) {
		try {
			if(categoryDTO != null) {
				getCategoryRepository().save(getCategoryMapper().covertToEntity(categoryDTO));
				return "Category registered succesfully!";
			} 
			//Funcionaria melhor como exception customizada
			return "DTO cannot be null";
		} catch (Exception e) {
			throw e;
		}
	}
	
	private CategoryRepository getCategoryRepository() {
		return categoryRepository;
	}
	
	private CategoryMapper getCategoryMapper() {
		return categoryMapper;
	}

}
