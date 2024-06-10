package com.brandstore.store.utils;

import org.springframework.stereotype.Component;

import com.brandstore.store.dto.BrandDTO;
import com.brandstore.store.entity.Brand;

@Component
public class BrandMapper {
	
	public Brand covertToEntity(BrandDTO dto) {
		Brand entity = new Brand();
		entity.setAdress(dto.getAdress());
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setPhone(dto.getPhone());
		
		return entity;
	}

}
