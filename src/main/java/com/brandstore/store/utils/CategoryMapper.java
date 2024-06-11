package com.brandstore.store.utils;

import org.springframework.stereotype.Component;

import com.brandstore.store.dto.CategoryDTO;
import com.brandstore.store.entity.Category;

@Component
public class CategoryMapper {

	//Convertendo DTO para Entidade
	public Category covertToEntity(CategoryDTO dto) {
		Category entity = new Category();

		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		
		return entity;
	}
	
}
