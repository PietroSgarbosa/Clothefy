package com.brandstore.store.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
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
	private CategoryRepository categoryRepository;
	
	@Mock
	@Autowired
	private BrandCategoryRepository brandCategoryRepository;
	
	@Mock
	@Autowired
	private BrandMapper brandMapper;	
	
	@InjectMocks
	@Autowired
	private BrandService brandService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);		
	}
	
	private static final Long BRAND_ID = 1l;
	private static final Long BRAND_ID2 = 2l;
	
	
	@Test
	void testGetAll() {
		// Arrange
		
		Brand brand1 = new Brand();
		brand1.setId(BRAND_ID);
		brand1.setName("Adidas");
		
		Brand brand2 = new Brand();
		brand2.setId(BRAND_ID2);
		brand2.setName("Puma");
		
		List<Brand> brandList = Arrays.asList(brand1, brand2);		
		
		
		when(brandRepository.findAll()).thenReturn(brandList);
	
		//Act
		
		List<BrandDTO> result = brandService.getAll();
		
		//Assert
		
		assertThat(result).hasSize(2);
		assertThat(result.get(0).getName()).isEqualTo("Adidas");
		assertThat(result.get(1).getName()).isEqualTo("Puma");
		verify(brandRepository, times(1)).findAll();		
	}
	
	@Test
	void testGetAllEmpty() {
		
		//Arrange
		
		when(brandRepository.findAll()).thenReturn(Arrays.asList());
		
		// Act
		List<BrandDTO> result = brandService.getAll();
		
		//Assert
		assertThat(result).isNull();
		verify(brandRepository, times(1)).findAll();		
	}
		
	@Test
	void testGetById() {
		
		//Arrange
		
		Brand brand = new Brand();
		
		brand.setId(BRAND_ID);
		brand.setName("Adidas");
		
		when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.of(brand));
		
		//Act
		Brand result = brandService.getById(BRAND_ID);
		
		// Assert
		
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(BRAND_ID);
		assertThat(result.getName()).isEqualTo("Adidas");
		verify(brandRepository, times(1)).findById(BRAND_ID);
	}
	
	@Test
	void testGetByIdNotFound() {
		when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.empty());
		
		// Act
		
		Brand result = brandService.getById(BRAND_ID);
		
		//Assert
		assertThat(result).isNull();
		verify(brandRepository, times(1)).findById(BRAND_ID);
	}
	
	@SuppressWarnings({ "deprecation" })
	@Test
	void testCreateBrand() {
		
		//Arrange			
		BrandDTO brandDTO = new BrandDTO();
		brandDTO.setName("Nike");
		brandDTO.setAdress("Rua 1");
		brandDTO.setPhone("123456");
		brandDTO.setCategoriesId(Arrays.asList(1l, 2l));
		
		Brand brand = new Brand();
		brand.setName("Nike");
		brand.setAdress("Rua 1");
		brand.setPhone("123456");
		
		Category category1 = new Category();
		category1.setId(1l);
		category1.setName("Moda Praia");
		
		Category category2 = new Category();
		category2.setId(2l);
		category2.setName("Sport");		
		
		BrandCategory brandCategory = new BrandCategory();
		brandCategory.setBrand(brand);
		brandCategory.setCategory(category1);
		
		
		when(brandMapper.covertToEntity(brandDTO)).thenReturn(brand);
		when(categoryRepository.getById(1l)).thenReturn(category1);
		when(categoryRepository.getById(2l)).thenReturn(category2);
		when(brandRepository.save(any(Brand.class))).thenReturn(brand);		
		
		// Act
		brandService.create(brandDTO);
		
		//Assert
				
		verify(brandMapper, times(1)).covertToEntity(brandDTO);
		verify(brandRepository, times(1)).save(brand);		
		verify(brandCategoryRepository, times(2)).save(any(BrandCategory.class));
		verify(categoryRepository, times(1)).getById(1l);
		verify(categoryRepository, times(1)).getById(2l);
	}
	
	@Test
	void testCreateBrand_WhenServiceThrowsException() {		
		
		assertThatThrownBy(() -> brandService.create(null))
		.isInstanceOf(IllegalArgumentException.class)
		.hasMessage("Atributes cannot by null");
	}	
	
	
	@Test
	void testUpdateBrand_WhenBrandValid() {
		
		BrandDTO brandDTO = new BrandDTO();		
		brandDTO.setId(BRAND_ID);
		brandDTO.setName("Nike");
		brandDTO.setAdress("Rua 1");
		brandDTO.setPhone("123456");
		
		
		Brand brand = new Brand();		
		brand.setId(BRAND_ID);
		brand.setName("Adidas");
		brand.setAdress("Rua 2");
		brand.setPhone("654321");		
		
		when(brandRepository.findById(brandDTO.getId())).thenReturn(Optional.of(brand));
		
		// Act
		String response = brandService.update(brandDTO);			
		
		// Assert	

		String expectedResponse = "Brand of ID " + brandDTO.getId() + " updated successfully!";
	    assertEquals(expectedResponse, response);
	    verify(brandRepository, times(1)).save(brand);	
		
	}
	
	@Test
	void testUpdateBrand_WhenBrandInvalid() {
		
		// Arrage
		
		BrandDTO brandDTO = new BrandDTO();
		brandDTO.setId(BRAND_ID);
		when(brandRepository.findById(brandDTO.getId())).thenReturn(Optional.empty());
		
		// Act
		String result = brandService.update(brandDTO);
		
		// Assert
		
		assertEquals("Brand of ID " + brandDTO.getId() + " not found", result);
		verify(brandRepository, never()).save(any(Brand.class));
	}
	
	

	@ Test
	void testDeleteWhithBrandValid() {
		
		// Arrange		
		
		Brand brand = new Brand();
		brand.setId(BRAND_ID);
		
		when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.of(brand));		
		
		// Act		
		
		String result = brandService.delete(BRAND_ID);
		
		// Assert
		
		assertEquals("Brand of ID " + BRAND_ID + " removed!", result);  
		verify(brandRepository, times(1)).deleteById(BRAND_ID);
		
	}
	

	@Test
	void testDeleteWithBrandNull() {
		 // Arrange
		
		
		when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.empty());
		
		// Act 
		
		String result = brandService.delete(BRAND_ID);
		
		// Assert
		
		assertEquals("This brand ID " + BRAND_ID + " doesn't exist", result);
		verify(brandRepository, times(1)).findById(BRAND_ID);
		verify(brandRepository, never()).deleteById(anyLong());
	}	
}


