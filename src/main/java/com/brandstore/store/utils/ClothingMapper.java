package com.brandstore.store.utils;

import org.springframework.stereotype.Component;

import com.brandstore.store.dto.ClothingDTO;
import com.brandstore.store.entity.Clothing;

@Component
public class ClothingMapper {
	
	public Clothing covertToEntity(ClothingDTO dto) {
		Clothing entity = new Clothing();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setPrice(dto.getPrice());
		entity.setSize(dto.getSize());
		
		return entity;
	}

}
