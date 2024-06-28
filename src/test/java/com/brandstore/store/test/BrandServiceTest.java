package com.brandstore.store.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
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
	private CategoryRepository categoryRepository;

	@Mock
	@Autowired
	private BrandCategoryRepository negocioRepository;

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

	private static final Long BRAND_ID = 1L;
	private static final Long BRAND_ID2 = 2L;

	@Test
	void testGetAll() {
		// Arrange
		Brand brand1 = new Brand();
		brand1.setId(BRAND_ID);
		brand1.setName("Adidas");

		Brand brand2 = new Brand();
		brand2.setId(BRAND_ID2);
		brand2.setName("Puma");

		List<Brand> listBrand = Arrays.asList(brand1, brand2);

		when(brandRepository.findAll()).thenReturn(listBrand);

		// Act
		List<BrandDTO> brandListDTO = brandService.getAll();

		// Assert
		assertThat(brandListDTO).isNotNull();
		assertThat(brandListDTO).hasSize(2);
		assertThat(brandListDTO.get(0).getId()).isEqualTo(BRAND_ID);
		assertThat(brandListDTO.get(0).getName()).isEqualTo("Adidas");
		assertThat(brandListDTO.get(1).getId()).isEqualTo(BRAND_ID2);
		assertThat(brandListDTO.get(1).getName()).isEqualTo("Puma");
		verify(brandRepository, times(1)).findAll();
	}

	@Test
	void testGetAll_ReturnsNull() {
		// Arrange
		List<Brand> emptyList = Collections.emptyList();

		when(brandRepository.findAll()).thenReturn(emptyList);

		// Act
		List<BrandDTO> brandListDTO = brandService.getAll();

		// Assert - verificar ou validar os efeitos colaterais
		assertThat(brandListDTO).isNull();
		verify(brandRepository, times(1)).findAll();
	}

	@Test
	void testGetById() {
		// Arrange
		Brand brand1 = new Brand();
		brand1.setId(BRAND_ID);
		brand1.setName("Adidas");

		when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.of(brand1));

		// Act
		Brand returnedBrand = brandService.getById(BRAND_ID);

		// Assert
		assertThat(returnedBrand).isNotNull();
		assertThat(returnedBrand.getId()).isEqualTo(BRAND_ID);
		assertThat(returnedBrand.getName()).isEqualTo("Adidas");
		verify(brandRepository, times(1)).findById(BRAND_ID);

	}
	
	@Test
	void testCreateBrand() {
	    
	    // Arrange
	    BrandDTO brandDTO = new BrandDTO();
	    brandDTO.setName("Oakley");
	    brandDTO.setAdress("aaaaaa");
	    brandDTO.setPhone("111111");
	    brandDTO.setCategoriesId(Arrays.asList(1L, 2L));

	    Brand brand = new Brand();
	    brand.setName("Oakley");
	    brand.setAdress("aaaaaa");
	    brand.setPhone("111111");

	    Category category1 = new Category();
	    category1.setId(1L);
	    category1.setName("Criança");

	    Category category2 = new Category();
	    category2.setId(2L);
	    category2.setName("Inverno");

	    BrandCategory brandCategory = new BrandCategory();
	    brandCategory.setBrand(brand);
	    brandCategory.setCategory(category1);

	    when(brandMapper.covertToEntity(brandDTO)).thenReturn(brand);
	    when(categoryRepository.getById(1L)).thenReturn(category1);
	    when(categoryRepository.getById(2L)).thenReturn(category2);
	    when(brandRepository.save(any(Brand.class))).thenReturn(brand);        

	    // Act
	    brandService.create(brandDTO);

	    // Assert
	    verify(brandMapper, times(1)).covertToEntity(brandDTO);
	    verify(brandRepository, times(1)).save(brand);        
	    verify(negocioRepository, times(2)).save(any(BrandCategory.class));
	    verify(categoryRepository, times(1)).getById(1L);
	    verify(categoryRepository, times(1)).getById(2L);
	}

	@Test
	void testCreate_WithInvalidDto() {
		// Configuração do mock para lançar exceção quando create for chamado com null
		doThrow(new IllegalArgumentException("Atributes cannot be null")).when(brandRepository).save(null);

		// Act and Assert
		assertThatThrownBy(() -> brandService.create(null)).isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	void testUpdate_WithValidBrand() {
	    // Arrange
	    BrandDTO brandDTO = new BrandDTO();
	    brandDTO.setId(BRAND_ID);
	    brandDTO.setName("Puma");
	    brandDTO.setAdress("bbbbbb"); 
	    brandDTO.setPhone(null); 
	    
	    Brand brandFound = new Brand();
	    brandFound.setName("Oakley");
	    brandFound.setAdress("aaaaaa");
	    brandFound.setPhone("111111");
	    
	    when(brandRepository.findById(brandDTO.getId())).thenReturn(Optional.of(brandFound));

	    // Act
	    String actualResponse = brandService.update(brandDTO);

	    // Assert
	    String expectedResponse = "Brand of ID " + brandDTO.getId() + " updated successfully!";
	    assertEquals(expectedResponse, actualResponse);
	    
	    verify(brandRepository).save(argThat(savedBrand ->
	        "Puma".equals(savedBrand.getName()) &&
	        "bbbbbb".equals(savedBrand.getAdress()) && 
	        "111111".equals(savedBrand.getPhone())    
	    ));
	    verify(brandRepository, times(1)).save(brandFound);

	}

	
	 @Test
	    void testUpdate_WithInvalidBrand() {
	        // Arrange
	        BrandDTO brandDTO = new BrandDTO();
	        brandDTO.setId(BRAND_ID);
	        brandDTO.setName("Oakley");
	        brandDTO.setAdress("aaaaaa");
	        brandDTO.setPhone("111111");

	        String responseMessage = "Brand of ID " + brandDTO.getId() + " not found";
	        when(brandService.getById(BRAND_ID)).thenReturn(null);
	        when(brandRepository.findById(brandDTO.getId())).thenReturn(Optional.empty());

	        // Act
	        String actualResponse = brandService.update(brandDTO);

	        // Assert
	        assertEquals(responseMessage, actualResponse);

	    }
	 
	 
	 @Test
	    void testDelete() {
		 
		 	// Arrange
		 	Brand brand = new Brand();
		 	brand.setId(BRAND_ID);
		 	brand.setName("Oakley");
		 	brand.setAdress("aaaaaa");
		 	brand.setPhone("111111");
		 	
		 	
		    when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.of(brand));
		    
		    String actualResponse =  "Brand of ID " + brand.getId() + " removed!";

		 	// Act
		    String responseMessage = brandService.delete(BRAND_ID);
	        // Assert
	        assertEquals(responseMessage, actualResponse);

	    }
	 
	 @Test
	    void testDelete_WhenBrandIsNotFound() {
		 	
		 	// Arrange
		    when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.empty());
		    
		    String actualResponse =  "This brand ID " + BRAND_ID + " doesn't exist";

		 	// Act
		    String responseMessage = brandService.delete(BRAND_ID);
	        // Assert
	        assertEquals(responseMessage, actualResponse);
		    verify(brandRepository, times(1)).findById(BRAND_ID);
		    verify(brandRepository, never()).deleteById(anyLong());


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
