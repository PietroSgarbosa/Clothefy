package com.brandstore.store.dto;

import java.util.List;

import org.modelmapper.ModelMapper;

import com.brandstore.store.entity.Brand;

public class BrandDTO {

	private Long id;

	private String name;

	private String adress;

	private String phone;
	
	private List<Long> categoriesId;
	
	private List<BrandCategoryDTO> categories;

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

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public List<Long> getCategoriesId() {
		return categoriesId;
	}

	public void setCategoriesId(List<Long> categoriesId) {
		this.categoriesId = categoriesId;
	}
	
	public List<BrandCategoryDTO> getCategories() {
		return categories;
	}

	public void setCategories(List<BrandCategoryDTO> categories) {
		this.categories = categories;
	}

	static ModelMapper getModelMapper() {
		return new ModelMapper();
	}
	
	public static BrandDTO convertToDTO(Brand brand) {
		return getModelMapper().map(brand, BrandDTO.class);
	}

}
