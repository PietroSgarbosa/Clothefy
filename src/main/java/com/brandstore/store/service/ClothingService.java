package com.brandstore.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brandstore.store.dto.ClothingDTO;
import com.brandstore.store.entity.Clothing;
import com.brandstore.store.repository.ClothingRepository;
import com.brandstore.store.utils.BrandMapper;
import com.brandstore.store.utils.ClothingMapper;

@Service
public class ClothingService {
	
	@Autowired
	private ClothingRepository clothingRepository;
	
	@Autowired
	private ClothingMapper clothingMapper;
	
	public ClothingDTO getById(Long id) {
		Clothing clothing = getClothingRepository().findById(id).orElse(null);
		if(clothing != null) {
			return ClothingDTO.convertToDTO(clothing);
		} else {
			return null;
		}
	}
	
	public List<ClothingDTO> getAll() {
		List<Clothing> clothingList = getClothingRepository().findAll();
		List<ClothingDTO> clothingListDTO = clothingList.stream().map(clothing -> ClothingDTO.convertToDTO(clothing))
				.toList();

		return clothingListDTO;
	}
	
	public void create(ClothingDTO clothingDTO) {
		if (clothingDTO != null) {
			Clothing clothingEntity = getClothingMapper().covertToEntity(clothingDTO);
			getClothingRepository().save(clothingEntity);
		} else {
			throw new IllegalArgumentException("Clothing cannot be null");
		}
	}
	
	private ClothingRepository getClothingRepository() {
		return clothingRepository;
	}
	
	private ClothingMapper getClothingMapper() {
		return clothingMapper;
	}

}
