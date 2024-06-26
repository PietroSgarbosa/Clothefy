package com.brandstore.store.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brandstore.store.dto.BrandDTO;
import com.brandstore.store.entity.Brand;
import com.brandstore.store.entity.BrandCategory;
import com.brandstore.store.entity.Category;
import com.brandstore.store.repository.BrandCategoryRepository;
import com.brandstore.store.repository.BrandRepository;
import com.brandstore.store.repository.CategoryRepository;
import com.brandstore.store.utils.BrandMapper;

@Service
public class BrandService {

	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired 
	private CategoryRepository categoryRepository;
	
	@Autowired
	private BrandCategoryRepository negocioRepository;

	@Autowired
	private BrandMapper brandMapper;

	public Brand getById(Long id) {
		return getBrandRepository().findById(id).orElse(null);
	}
	
	@SuppressWarnings("deprecation")
	public void create(BrandDTO dto) {
		if (dto != null) {
			Brand brand = getBrandMapper().covertToEntity(dto);

			getBrandRepository().save(brand);
			
			List<BrandCategory> negocioLista = new ArrayList<BrandCategory>();

			for(Long id : dto.getCategoriesId()) {
				Category category = getCategoryRepository().getById(id);
				BrandCategory negocio = new BrandCategory();
				negocio.setBrand(brand);
				negocio.setCategory(category);
				getNegocioRepository().save(negocio);
				negocioLista.add(negocio);
			}
			
			brand.setCategories(negocioLista);
		} else {
			throw new IllegalArgumentException("Atributes cannot by null");

		}
	}

	public List<BrandDTO> getAll() {
		List<Brand> brandList = getBrandRepository().findAll();
		List<BrandDTO> brandListDTO = brandList.stream().map(brand -> BrandDTO.convertToDTO(brand)).toList();

		if (!brandListDTO.isEmpty()) {
			return brandListDTO;
		} else {
			return null;
		}
	}

	public String delete(Long id) {
		Brand brand = getById(id);

		if (brand == null) {
			return "This brand ID " + id + " doesn't exist";
		} else {
			getBrandRepository().deleteById(id);
			return "Brand of ID " + id + " removed!";
		}
	}

	public String update(BrandDTO brandDTO) {
		Brand defaultBrand = getById(brandDTO.getId());
		String responseMessage = "Brand of ID " + brandDTO.getId() + " not found";

		if (defaultBrand != null) {

			defaultBrand.setName(brandDTO.getName() != null ? brandDTO.getName() : defaultBrand.getName());
			defaultBrand.setAdress(brandDTO.getAdress() != null ? brandDTO.getAdress() : defaultBrand.getAdress());
			defaultBrand.setPhone(brandDTO.getPhone() != null ? brandDTO.getPhone() : defaultBrand.getPhone());
			responseMessage = "Brand of ID " + brandDTO.getId() + " updated successfully!";

			getBrandRepository().save(defaultBrand);

			return responseMessage;
		}
		return responseMessage;
	}

	private BrandRepository getBrandRepository() {
		return brandRepository;
	}
	
	private CategoryRepository getCategoryRepository() {
		return categoryRepository;
	}
	
	private BrandCategoryRepository getNegocioRepository() {
		return negocioRepository;
	}

	public BrandMapper getBrandMapper() {
		return brandMapper;
	}

}
