package com.brandstore.store.test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
	// MOCKANDO O SERVICE, isso são os dados mockados
	@Mock
	@Autowired
	private BrandService brandService; // -> MOCKAR

	// INJETANDO OS DADOS MOCKADOS na classe que nós vamos testar
	@InjectMocks
	@Autowired
	private BrandController brandController; // -> TESTAR

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	// --------------------------------------------------//

	// setando constantes pra evitar redundância
	private static final Long BRAND_ID = 1l;
	private static final Long BRAND_ID2 = 2l;

	@SuppressWarnings("unchecked")
	@Test
	void testGetAll() {
		// Arrange - organizar ou preparar os dados mockados -----------//
		BrandDTO brand1 = new BrandDTO();
		brand1.setId(BRAND_ID);
		brand1.setName("Adidas");

		BrandDTO brand2 = new BrandDTO();
		brand2.setId(BRAND_ID2);
		brand2.setName("Puma");

		List<BrandDTO> listBrandDTO = Arrays.asList(brand1, brand2);

		// Troca o serviço real pelo mockado e traz os dados que preparamos para teste
		when(brandService.getAll()).thenReturn(listBrandDTO);
		// ------------------------------------------------------------//

		// Act - chamar o método que vai ser testado (chamar getAll)
		ResponseEntity<List<BrandDTO>> testResponse = (ResponseEntity<List<BrandDTO>>) brandController.getAll();

		// Assert - verificar ou validar o teste
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
	    

	    // Troca o serviço real pelo mockado e traz os dados que preparamos para teste
	    when(brandService.getById(BRAND_ID)).thenReturn(brand1);

	    // Act - chamar o método que vai ser testado (chamar getById)
	    ResponseEntity<Brand> testResponse = (ResponseEntity<Brand>) brandController.getById(BRAND_ID);

	    // Assert - verificar ou validar o teste
	    assertThat(testResponse.getBody()).isNotNull(); 
	    assertThat(testResponse.getBody().getName()).isEqualTo("Adidas"); 
	    assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

	    verify(brandService, times(1)).getById(BRAND_ID); 
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
		brandCreate.setId(BRAND_ID);// Não usar o id
		brandCreate.setName("Condor");
		brandCreate.setCategoriesId(new ArrayList<>(Arrays.asList(1L, 2L, 3L)));
		brandCreate.setAdress("São Paulo");
		brandCreate.setPhone("13-9998588");

		// Configurar o mock para não fazer nada quando o método create for chamado
		doNothing().when(brandService).create(brandCreate);

		// Act - chamar o método que vai ser testado (chamar create)
		ResponseEntity<?> testResponse = brandController.create(brandCreate);

		// Assert - verificar ou validar os efeitos colaterais
		assertThat(testResponse.getBody()).isEqualTo("Brand inserted successfully!");
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		verify(brandService, times(1)).create(brandCreate);
	}

	@Test
	void testCreate_WhenControllerThrowsException() {
		// Arrange - organizar ou preparar os dados mockados
		BrandDTO brandCreate = new BrandDTO();
		brandCreate.setName("Condor");
		brandCreate.setCategoriesId(new ArrayList<>(Arrays.asList(1L, 2L, 3L)));
		brandCreate.setAdress("São Paulo");
		brandCreate.setPhone("13-9998588");

		doThrow(new RuntimeException("Nenhum dado encontrado")).when(brandService).create(brandCreate);

		// Act - chamar o método do controlador
		ResponseEntity<?> testResponse = brandController.create(brandCreate);

		// Assert - verificar ou validar os efeitos colaterais
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(testResponse.getBody())
				.isEqualTo("Failed trying to insert new data, error message: Nenhum dado encontrado");
		verify(brandService, times(1)).create(brandCreate);
	}

	@Test
	void testUpdate() {
		// Arrange - organizar ou preparar os dados mockado
		BrandDTO brandUpdate = new BrandDTO();

		// Configurar o mock do serviço para retornar brandExist quando o método update
		when(brandService.update(brandUpdate)).thenReturn("Brand updated successfully");

		// Act - chamar o método do controlador
		ResponseEntity<?> testResponse = brandController.update(brandUpdate);

		// Assert - verificar ou validar os efeitos colaterais
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(testResponse.getBody()).isEqualTo("Brand updated successfully");
		verify(brandService, times(1)).update(brandUpdate);
	}

	@Test
	void testUpdate_WhenControllerThrowsException() {
		// Arrange - organizar ou preparar os dados mockados
		BrandDTO brandUpdate = new BrandDTO();

		// Configurar o mock do serviço para lançar uma exceção quando o método update
		// for chamado com brandUpdate
		when(brandService.update(brandUpdate)).thenThrow(new RuntimeException("None of the brands match"));

		// Act - chamar o método do controlador
		ResponseEntity<?> testResponse = brandController.update(brandUpdate);

		// Assert - verificar ou validar os efeitos colaterais
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(testResponse.getBody())
				.isEqualTo("Error trying to update brand. Error message: None of the brands match");
		verify(brandService, times(1)).update(brandUpdate);
	}

	@Test
	void testDelete() {

		// for chamado com brandUpdate
		when(brandService.delete(BRAND_ID)).thenReturn("Brand deleted succesfully");

		// Act - chamar o método do controlador
		ResponseEntity<?> testResponse = brandController.delete(BRAND_ID);

		// Assert - verificar ou validar os efeitos colaterais
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(testResponse.getBody()).isEqualTo("Brand deleted succesfully");
		verify(brandService, times(1)).delete(BRAND_ID);
	}

	@Test
	void testDelete_WhenControllerThrowsException() {

		// Configurar o mock do serviço para retornar brandExist quando o método update
		when(brandService.delete(BRAND_ID)).thenThrow(new RuntimeException("None of the brands match"));

		// Act - chamar o método do controlador
		ResponseEntity<?> testResponse = brandController.delete(BRAND_ID);

		// Assert - verificar ou validar os efeitos colaterais
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(testResponse.getBody()).isEqualTo("Internal error, message: None of the brands match");
		verify(brandService, times(1)).delete(BRAND_ID);
	}

}
