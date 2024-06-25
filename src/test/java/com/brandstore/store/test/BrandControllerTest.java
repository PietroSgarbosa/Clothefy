package com.brandstore.store.test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.brandstore.store.controller.BrandController;
import com.brandstore.store.dto.BrandDTO;
import com.brandstore.store.entity.Brand;
import com.brandstore.store.service.BrandService;

@SpringBootTest
public class BrandControllerTest {
	
	@Mock
	@Autowired
	private BrandService brandService; 
	
	@InjectMocks
	@Autowired
	private BrandController brandController; 
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	private static final Long BRAND_ID = 1l;
	private static final Long BRAND_ID2 = 2l;
	
	@SuppressWarnings("unchecked")
	@Test
	void testGetAll() {
		BrandDTO brand1 = new BrandDTO();
		brand1.setId(BRAND_ID);
		brand1.setName("Adidas");
		
		BrandDTO brand2 = new BrandDTO();
		brand2.setId(BRAND_ID2);
		brand2.setName("Puma");
	
		List<BrandDTO> listBrandDTO = Arrays.asList(brand1, brand2);
		
		when(brandService.getAll()).thenReturn(listBrandDTO);
		
		ResponseEntity<List<BrandDTO>> testResponse = (ResponseEntity<List<BrandDTO>>) brandController.getAll();
		
		assertThat(testResponse.getBody()).hasSize(2);
		assertThat(testResponse.getBody().get(0).getName()).isEqualTo("Adidas");
		assertThat(testResponse.getBody().get(1).getName()).isEqualTo("Puma");
		verify(brandService, times(1)).getAll(); 
	}
	
	@Test
	void testGetAllException() {
		when(brandService.getAll()).thenThrow(new RuntimeException("Serviço Indisponivel"));
		
		ResponseEntity<?> testResponse = brandController.getAll();

        assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(testResponse.getBody()).isEqualTo("Internal Server Error: Serviço Indisponivel");
        verify(brandService, times(1)).getAll();
	}
	
	@Test
	void testGetById() {
		Brand brand = new Brand();
		brand.setId(BRAND_ID);
		brand.setName("Adidas");
		
		when(brandService.getById(BRAND_ID)).thenReturn(brand);
		
		
		ResponseEntity<Brand> testResponse = (ResponseEntity<Brand>) brandController.getById(BRAND_ID);
		
		assertThat(testResponse.getBody().equals(brand));
		verify(brandService, times(1)).getById(BRAND_ID);
	
	}
	
	@Test
	void testGetByIdException() {
		when(brandService.getById(BRAND_ID)).thenThrow(new RuntimeException("Serviço Indisponivel"));
		
		ResponseEntity<?> testResponse = brandController.getById(BRAND_ID);

        assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(testResponse.getBody().equals("Serviço Indisponivel"));
        verify(brandService, times(1)).getById(BRAND_ID);
	}
}
