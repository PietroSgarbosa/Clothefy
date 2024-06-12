package com.brandstore.store.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "BRAND_CATEGORY")
public class BrandCategory {
	
	private Long id; 
	
	@JoinColumn(name = "ID_BRAND")
	private Brand brand;

	@JoinColumn(name = "ID_CATEGORY")
	private Category category;

}
