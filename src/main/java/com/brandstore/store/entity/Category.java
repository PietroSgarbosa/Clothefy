package com.brandstore.store.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="CATEGORY")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="NAME", nullable = false)
	private String name;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@JsonIgnore
	@OneToMany(mappedBy="category")
	private List<Clothing> clothes;
	
	//Uma categoria para vários negócios/marcas/relação
	@JsonIgnore
	@OneToMany(mappedBy="category")
	private List<BrandCategory> brands;
	
	public List<Clothing> getClothes() {
		return clothes;
	}

	public void setClothes(List<Clothing> clothings) {
		this.clothes = clothings;
	}

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

	public List<BrandCategory> getBrands() {
		return brands;
	}

	public void setBrands(List<BrandCategory> brands) {
		this.brands = brands;
	}

}
