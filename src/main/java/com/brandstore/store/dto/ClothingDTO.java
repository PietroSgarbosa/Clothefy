package com.brandstore.store.dto;

import org.modelmapper.ModelMapper;

import com.brandstore.store.entity.Clothing;

public class ClothingDTO {
	
	private Long id;
	
	private String name;
	
	private String size;
	
	private double price;
	
	private Long categoryId;
	
	private CategoryDTO categoryDTO;

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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	public CategoryDTO getCategoryDTO() {
		return categoryDTO;
	}

	public void setCategoryDTO(CategoryDTO categoryDTO) {
		this.categoryDTO = categoryDTO;
	}

	static ModelMapper getModelMapper() {
		return new ModelMapper();
	}

	public static ClothingDTO convertToDTO(Clothing entity) {
		return getModelMapper().map(entity, ClothingDTO.class);
	}

}
