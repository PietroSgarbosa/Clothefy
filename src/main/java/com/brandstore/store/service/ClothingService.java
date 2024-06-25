package com.brandstore.store.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brandstore.store.dto.CategoryDTO;
import com.brandstore.store.dto.ClothingDTO;
import com.brandstore.store.entity.Category;
import com.brandstore.store.entity.Clothing;
import com.brandstore.store.repository.CategoryRepository;
import com.brandstore.store.repository.ClothingRepository;
import com.brandstore.store.utils.ClothingMapper;

@Service
public class ClothingService {
	
	@Autowired
	private ClothingRepository clothingRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ClothingMapper clothingMapper;
	
	public Clothing getById(Long id) {
		return getClothingRepository().findById(id).orElse(null);
		
	}
	
	public List<ClothingDTO> getAll() {
		List<Clothing> clothingList = getClothingRepository().findAll();
		
		List<ClothingDTO> clothingListDTO = new ArrayList<ClothingDTO>();
		
		for(Clothing clothing : clothingList) {
			ClothingDTO clothingDTO = ClothingDTO.convertToDTO(clothing);
			if(clothing.getCategory() != null) {
				clothingDTO.setCategoryDTO(CategoryDTO.convertToDTO(clothing.getCategory()));
			}
			clothingListDTO.add(clothingDTO);
		}

		return clothingListDTO;
	}
	
	public void create(ClothingDTO clothingDTO) {
		if (clothingDTO != null) {
			Clothing clothingEntity = getClothingMapper().covertToEntity(clothingDTO);
			if(clothingDTO.getCategoryId() != null) {
				Category category = getCategoryRepository().findById(clothingDTO.getCategoryId()).orElse(null);
				clothingEntity.setCategory(category);
			}
			getClothingRepository().save(clothingEntity);
		} else {
			throw new IllegalArgumentException("Clothing cannot be null");
		}
	}
	
	public String update(ClothingDTO clothingDTO) {
		Clothing defaultClothing = getById(clothingDTO.getId());
		String responseMessage = "Clothing of ID " + clothingDTO.getId() + " not found";

		if (defaultClothing != null) {

			defaultClothing.setName(clothingDTO.getName() != null ? clothingDTO.getName() : defaultClothing.getName());			
			defaultClothing.setPrice(clothingDTO.getPrice() != 0 ? clothingDTO.getPrice() : defaultClothing.getPrice());
			defaultClothing.setSize(clothingDTO.getSize() != null ? clothingDTO.getSize() : defaultClothing.getSize());
			responseMessage = "Clothing of ID " + clothingDTO.getId() + " updated successfully!";

			getClothingRepository().save(defaultClothing);

			return responseMessage;
		}
		return responseMessage;
	}
	
	public String delete(Long id) {
		Clothing clothing = getById(id);

		if (clothing == null) {
			return "This clothing ID " + id + " doesn't exist";
		} else {
			getClothingRepository().deleteById(id);
			return "Clothing of ID " + id + " removed!";
		}
	}
	
	private ClothingRepository getClothingRepository() {
		return clothingRepository;
	}
	
	private CategoryRepository getCategoryRepository() {
		return categoryRepository;
	}
	
	private ClothingMapper getClothingMapper() {
		return clothingMapper;
	}

}
