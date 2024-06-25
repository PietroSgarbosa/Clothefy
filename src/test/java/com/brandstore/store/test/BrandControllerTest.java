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
import org.springframework.http.ResponseEntity;

import com.brandstore.store.controller.BrandController;
import com.brandstore.store.dto.BrandDTO;
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
	
	//Fazer um teste do getById e como bonus, fazer o teste da exceção do getAll
	//Começar aqui

}
