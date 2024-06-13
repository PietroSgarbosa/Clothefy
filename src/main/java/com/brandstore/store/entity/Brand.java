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
@Table(name="BRAND")
public class Brand {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="NAME", nullable = false)
	private String name;
	
	@Column(name="ADRESS")
	private String adress;
	
	@Column(name="PHONE")
	private String phone;
	
	//Negócio, uma marca para muitos negócios/categorias/relação
	@JsonIgnore
	@OneToMany(mappedBy = "brand")
	private List<BrandCategory> categories;

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

	public List<BrandCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<BrandCategory> categories) {
		this.categories = categories;
	}

}
