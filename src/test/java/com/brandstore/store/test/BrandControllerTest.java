package com.brandstore.store.test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
	
	// MEMORIZAR PADRÂO DE FUNCIONALIDADE ---------------//
	//MOCKANDO O SERVICE, isso são os dados mockados
	@Mock
	@Autowired
	private BrandService brandService; // -> MOCKAR
	
	//INJETANDO OS DADOS MOCKADOS na classe que nós vamos testar
	@InjectMocks
	@Autowired
	private BrandController brandController; // -> TESTAR
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	// --------------------------------------------------//
	
	//setando constantes pra evitar redundância
	private static final Long BRAND_ID = 1l;
	private static final Long BRAND_ID2 = 2l;
	
	@SuppressWarnings("unchecked")
	@Test
	void testGetAll() {
		//Arrange - organizar ou preparar os dados mockados -----------//
		BrandDTO brand1 = new BrandDTO();
		brand1.setId(BRAND_ID);
		brand1.setName("Adidas");
		
		BrandDTO brand2 = new BrandDTO();
		brand2.setId(BRAND_ID2);
		brand2.setName("Puma");
	
		List<BrandDTO> listBrandDTO = Arrays.asList(brand1, brand2);
		
		//Troca o serviço real pelo mockado e traz os dados que preparamos para teste
		when(brandService.getAll()).thenReturn(listBrandDTO);
		// ------------------------------------------------------------//
		
		//Act - chamar o método que vai ser testado (chamar getAll)
		ResponseEntity<List<BrandDTO>> testResponse = (ResponseEntity<List<BrandDTO>>) brandController.getAll();
		
		//Assert - verificar ou validar o teste
		assertThat(testResponse.getBody()).hasSize(2);
		assertThat(testResponse.getBody().get(0).getName()).isEqualTo("Adidas");
		assertThat(testResponse.getBody().get(1).getName()).isEqualTo("Puma");
		verify(brandService, times(1)).getAll(); 
	}
	
	@Test
	void testGetAll_WhenControllerThrowsException() {

	    // Simula uma exceção ao chamar o serviço
	    when(brandService.getAll()).thenThrow(new RuntimeException("Nenhum dado encontrado"));

	    // Act - chamar o método que vai ser testado (chamar getAll)
	    ResponseEntity<?> testResponse = brandController.getAll();

	    // Assert - verificar se a exceção foi lançada corretamente
	    assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	    verify(brandService, times(1)).getAll();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testGetById() {
	    // Arrange - organizar ou preparar os dados mockados
	    Brand brand1 = new Brand();
	    brand1.setId(BRAND_ID);
	    brand1.setName("Adidas");
	    
	    Brand brand2 = new Brand();
		brand2.setId(BRAND_ID2);
		brand2.setName("Puma");

	    // Troca o serviço real pelo mockado e traz os dados que preparamos para teste
	    when(brandService.getById(BRAND_ID2)).thenReturn(brand2);

	    // Act - chamar o método que vai ser testado (chamar getById)
	    ResponseEntity<Brand> testResponse = (ResponseEntity<Brand>) brandController.getById(BRAND_ID2);

	    // Assert - verificar ou validar o teste
	    assertThat(testResponse.getBody()).isNotNull(); 
	    assertThat(testResponse.getBody().getName()).isEqualTo("Puma"); 
	    assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

	    verify(brandService, times(1)).getById(BRAND_ID2); 
	}
	
	@Test
	void testGetById_WhenControllerThrowsException() {
	    // Simula uma exceção ao chamar o serviço
	    when(brandService.getById(1L)).thenThrow(new RuntimeException("Nenhum dado encontrado"));

	    // Chama o método que vai ser testado (chamar getById)
	    ResponseEntity<?> testResponse = brandController.getById(1L);

	    // Verifica se a exceção foi lançada corretamente
	    assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	    verify(brandService, times(1)).getById(1L);
	}
	
	@Test
	void testCreate() {
	    // Arrange - organizar ou preparar os dados mockados
	    BrandDTO brandCreate = new BrandDTO();
	    brandCreate.setId(3L);
	    brandCreate.setName("Condor");
	    brandCreate.setCategoriesId(new ArrayList<>(Arrays.asList(1L, 2L, 3L)));
	    brandCreate.setAdress("São Paulo");
	    brandCreate.setPhone("13-9998588");

	    // Act - chamar o método que vai ser testado (chamar create)
	    ResponseEntity<?> testResponse = brandController.create(brandCreate);

	    // Assert - verificar ou validar os efeitos colaterais
	    assertThat(testResponse.getBody()).isEqualTo("Brand inserted successfully!");
	    assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	    verify(brandService, times(1)).create(brandCreate);
	}
	
	 @Test
	    void testCreate_WhenControllerThrowsException() {
	        // Arrange - configurar o mock para lançar uma exceção
	        doThrow(new RuntimeException("Nenhum dado encontrado")).when(brandService).create(null);

	        // Act - chamar o método do controlador
	        ResponseEntity<?> testResponse = brandController.create(null);

	        // Assert - verificar ou validar os efeitos colaterais
	        assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	        assertThat(testResponse.getBody()).isEqualTo("Failed trying to insert new data, error message: Nenhum dado encontrado");
	        verify(brandService, times(1)).create(null);
	    }

}
