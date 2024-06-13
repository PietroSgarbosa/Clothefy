package com.brandstore.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brandstore.store.entity.BrandCategory;
import com.brandstore.store.repository.BrandCategoryRepository;

@Service
public class BrandCategoryService {
	
	@Autowired
	private BrandCategoryRepository brandCategoryRepository;
	
	public List<BrandCategory> getAll() {
		return getBrandCategoryRepository().findAll();
	}
	
	public BrandCategory getById(Long id) {
		return getBrandCategoryRepository().findById(id).orElse(null);
	}
	
	public void create(BrandCategory entity) {
		getBrandCategoryRepository().save(entity);
	}
	
	private BrandCategoryRepository getBrandCategoryRepository() {
		return brandCategoryRepository;
	}

}
