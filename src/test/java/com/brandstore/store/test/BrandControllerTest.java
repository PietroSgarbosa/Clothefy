package com.brandstore.store.test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
	private BrandService brandService; 
	
	@InjectMocks
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
	void testGetAll_WhenThrowException() {
		when(brandService.getAll()).thenThrow(new RuntimeException("Service unavailable"));
		
		ResponseEntity<?> testResponse = brandController.getAll();

        assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(testResponse.getBody()).isEqualTo("Internal Server Error: Service unavailable");
        verify(brandService, times(1)).getAll();
	}
	
	@SuppressWarnings("unchecked")
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
	void testGetById_WhenThrowException() {
		when(brandService.getById(BRAND_ID)).thenThrow(new RuntimeException("Service unavailable"));
		
		ResponseEntity<?> testResponse = brandController.getById(BRAND_ID);

        assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(testResponse.getBody().equals("Service unavailable"));
        verify(brandService, times(1)).getById(BRAND_ID);
	}
	@SuppressWarnings("unchecked")
	@Test
    void testCreate() {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(BRAND_ID);
        brandDTO.setName("Adidas");

        doNothing().when(brandService).create(brandDTO);

        ResponseEntity<String> testResponse = (ResponseEntity<String>) brandController.create(brandDTO);

        assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(testResponse.getBody()).isEqualTo("Brand inserted successfully!");
        verify(brandService, times(1)).create(brandDTO);
    }

    @SuppressWarnings("unchecked")
	@Test
    void testCreate_WhenThrowException() {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(BRAND_ID);
        brandDTO.setName("Adidas");

        doThrow(new RuntimeException("Service unavailable")).when(brandService).create(brandDTO);

        ResponseEntity<String> testResponse = (ResponseEntity<String>) brandController.create(brandDTO);

        assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(testResponse.getBody()).isEqualTo("Failed trying to insert new data, error message: Service unavailable");
        verify(brandService, times(1)).create(brandDTO);
    }
    
    @Test
    void testDeleteSuccess() {
    	when(brandService.delete(BRAND_ID)).thenReturn("Brand deleted succesfully");

        ResponseEntity<String> testResponse = brandController.delete(BRAND_ID);

        assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(testResponse.getBody()).isEqualTo("Brand deleted succesfully");
        verify(brandService, times(1)).delete(BRAND_ID);
    }
    
    @Test
    void testDelete_WhenThrowException() {
        doThrow(new RuntimeException("Service unavailable")).when(brandService).delete(BRAND_ID);

        ResponseEntity<String> testResponse = brandController.delete(BRAND_ID);

        assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(testResponse.getBody()).isEqualTo("Internal error, message: Service unavailable");
        verify(brandService, times(1)).delete(BRAND_ID);
    }
    
    @Test
    void testUpdateSuccess() {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(BRAND_ID);
        brandDTO.setName("Adidas");

        when(brandService.update(brandDTO)).thenReturn("Brand updated successfully");

        ResponseEntity<String> testResponse = brandController.update(brandDTO);

        assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(testResponse.getBody()).isEqualTo("Brand updated successfully");
        verify(brandService, times(1)).update(brandDTO);
    }

    @Test
    void testUpdate_WhenThrowException() {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(BRAND_ID);
        brandDTO.setName("Adidas");

        when(brandService.update(brandDTO)).thenThrow(new RuntimeException("Service unavailable"));

        ResponseEntity<String> testResponse = brandController.update(brandDTO);

        assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(testResponse.getBody()).isEqualTo("Error trying to update brand. Error message: Service unavailable");
        verify(brandService, times(1)).update(brandDTO);
    }
    
}
