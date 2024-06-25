package com.brandstore.store.dto;

import com.brandstore.store.entity.Brand;
import com.brandstore.store.entity.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class BrandCategoryDTO {
	
	@JsonIgnore
	private Long id;
	
	@JsonIgnore
	private Brand brand;
	
	private Category category;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
