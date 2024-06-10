package com.brandstore.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brandstore.store.repository.BrandRepository;

@Service
public class BrandService {
	
	@Autowired
	private BrandRepository brandRepository;
	
	private BrandRepository getBrandRepository() {
		return brandRepository;
	}
	
}
