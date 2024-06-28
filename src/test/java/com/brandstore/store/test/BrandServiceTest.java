package com.brandstore.store.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.brandstore.store.dto.BrandDTO;
import com.brandstore.store.entity.Brand;
import com.brandstore.store.entity.BrandCategory;
import com.brandstore.store.entity.Category;
import com.brandstore.store.repository.BrandCategoryRepository;
import com.brandstore.store.repository.BrandRepository;
import com.brandstore.store.repository.CategoryRepository;
import com.brandstore.store.service.BrandService;
import com.brandstore.store.utils.BrandMapper;

@SpringBootTest
public class BrandServiceTest {

	@Mock
	@Autowired
	private BrandRepository brandRepository;

	@Mock
	@Autowired
	private BrandMapper brandMapper;

	@Mock
	@Autowired
	private CategoryRepository categoryRepository;

	@Mock
	@Autowired
	private BrandCategoryRepository brandCategoryRepository;

	@InjectMocks
	@Autowired
	private BrandService brandService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	// const
	private static final Long BRAND_ID = 1l;

	@Test
	void testGetAll_IfNotEmpty() {

		// Arrange
		Brand brand1 = new Brand();
		Brand brand2 = new Brand();

		List<Brand> listBrand = Arrays.asList(brand1, brand2);

		when(brandRepository.findAll()).thenReturn(listBrand);

		// Act
		List<BrandDTO> listBrandDTO = brandService.getAll();

		// Assert
		assertThat(listBrandDTO).isNotNull();
		assertThat(listBrandDTO).hasSize(2);

	}

	@Test
	void testGetAll_IfEmpty() {
		// Arrange

		when(brandRepository.findAll()).thenReturn(Collections.emptyList());

		// Act
		List<BrandDTO> listBrandDTO = brandService.getAll();

		// Assert
		assertThat(listBrandDTO).isNull();
	}

	@Test
	void testGetById() {
		// Arrange
		Brand brand = new Brand();
		brand.setId(BRAND_ID);

		when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.of(brand));

		// Act
		Brand response = brandService.getById(BRAND_ID);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response).hasSameClassAs(brand);
		verify(brandRepository, times(1)).findById(BRAND_ID);
	}


	@Test
	void testCreateBrand() {

		// Arrange
		BrandDTO brandDTO = new BrandDTO();
		brandDTO.setId(BRAND_ID);
		brandDTO.setAdress("Rua Teste");
		brandDTO.setName("Testison");
		brandDTO.setPhone("3333333");
		brandDTO.setCategoriesId(Arrays.asList(BRAND_ID));

		Brand brand = new Brand();
		brand.setId(BRAND_ID);

		Category category1 = new Category();
		category1.setId(BRAND_ID);

		BrandCategory brandCategory = new BrandCategory();
		brandCategory.setId(BRAND_ID);
		brandCategory.setBrand(brand);
		brandCategory.setCategory(category1);

		when(brandMapper.covertToEntity(brandDTO)).thenReturn(brand);
		when(brandRepository.save(brand)).thenReturn(brand);
		when(categoryRepository.getById(BRAND_ID)).thenReturn(category1);

		// Act
		brandService.create(brandDTO);

		// Assert
		verify(brandRepository, times(1)).save(brand);
		verify(categoryRepository, times(1)).getById(BRAND_ID);
		verify(brandCategoryRepository, times(1)).save(any(BrandCategory.class));
	}

	@Test
	void testCreate_WhenThrowException() throws Exception {

		// Arrange
		doThrow(new IllegalArgumentException("Atributes cannot be null")).when(brandRepository).save(null);

		// Act & Assert
		assertThatThrownBy(() -> brandService.create(null)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void testDelete() {

		// Arrange
		Brand brand = new Brand();
		brand.setId(BRAND_ID);

		when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.of(brand));

		String message = "Brand of ID " + BRAND_ID + " removed!";

		// Act
		String act = brandService.delete(BRAND_ID);

		// Assert
		assertEquals(message, act);

	}
	
	@Test
	void testDelete_WhenNotFoundBrand() {

		// Arrange
		when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.empty());

		String message = "This brand ID " + BRAND_ID + " doesn't exist";

		// Act
		String act = brandService.delete(BRAND_ID);

		// Assert
		assertEquals(message, act);
	}
	@Test
	 void testUpdate() {
		 
		 BrandDTO brandDTO = new BrandDTO();
		 brandDTO.setId(BRAND_ID);
		 
		 Brand brand = new Brand();
		brand.setId(BRAND_ID);
		 
		when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.of(brand));
		
		String message = "Brand of ID " + brandDTO.getId() + " updated successfully!";
		
		String act = brandService.update(brandDTO);
		
		assertEquals(message, act);
		verify(brandRepository, times(1)).save(brand);
	 }
	
	@Test
	 void testUpdate_WhenNotFound() {
		
		BrandDTO brandDTO = new BrandDTO();
		brandDTO.setId(BRAND_ID);
		
		when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.empty());
		
		String message = "Brand of ID " + brandDTO.getId() + " not found";
		
		String act = brandService.update(brandDTO);
		
		assertEquals(message, act);
	 }
}
