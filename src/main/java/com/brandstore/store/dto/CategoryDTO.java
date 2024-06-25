package com.brandstore.store.dto;

import java.util.List;

import org.modelmapper.ModelMapper;

import com.brandstore.store.entity.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CategoryDTO {
	
	private Long id;
	
	private String name;
	
	private String description;
	
	@JsonIgnore
	private List<ClothingDTO> clothes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ClothingDTO> getClothes() {
		return clothes;
	}

	public void setClothes(List<ClothingDTO> clothes) {
		this.clothes = clothes;
	}
	
	static ModelMapper getModelMapper() {
		return new ModelMapper();
	}

	public static CategoryDTO convertToDTO(Category entity) {
		return getModelMapper().map(entity, CategoryDTO.class);
	}
	
}
