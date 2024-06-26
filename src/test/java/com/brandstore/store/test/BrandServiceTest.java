package com.brandstore.store.test;

import static org.assertj.core.api.Assertions.assertThat;
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
	
}
