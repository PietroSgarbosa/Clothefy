package com.brandstore.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brandstore.store.dto.BrandDTO;
import com.brandstore.store.entity.Brand;
import com.brandstore.store.repository.BrandRepository;
import com.brandstore.store.utils.BrandMapper;

@Service
public class BrandService {

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private BrandMapper brandMapper;

	public Brand getById(Long id) {
		return getBrandRepository().getById(id);
	}

	public void create(BrandDTO dto) {
		if (dto != null) {
			Brand brand = getBrandMapper().covertToEntity(dto);

			getBrandRepository().save(brand);

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

	public BrandMapper getBrandMapper() {
		return brandMapper;
	}

}
