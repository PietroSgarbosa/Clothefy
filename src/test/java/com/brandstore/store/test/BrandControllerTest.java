package com.brandstore.store.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
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
	private static final String MSG_ERROR = "MESSAGE ERROR";

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
	void testGetAll_WhenThrowsException() {

		when(brandService.getAll()).thenThrow(new RuntimeException(MSG_ERROR));

		ResponseEntity<?> testResponse = brandController.getAll();

		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(testResponse.getBody()).isEqualTo("Internal Server Error: " + MSG_ERROR);
	}

	// Fazer um teste do getById e como bonus, fazer o teste da exceção do getAll
	// Começar aqui

	@SuppressWarnings("unchecked")
	@Test
	void testGetById() {

		// ---Preparando os dados mockados---
		Brand brand1 = new Brand();
		brand1.setId(BRAND_ID);
		brand1.setName("Adidas");

		// trocando service pelo mockado (retorna um objeto Brand)
		when(brandService.getById(BRAND_ID)).thenReturn(brand1);

		// chamando o método que será testado
		ResponseEntity<Brand> testResponse = (ResponseEntity<Brand>) brandController.getById(BRAND_ID);

		// Verificar ou validar o teste
		assertThat(testResponse.getBody().getName()).isEqualTo("Adidas");
		assertThat(testResponse).isNotNull();

	}

	@SuppressWarnings("unchecked")
	@Test
	void testGetById_WhenThrowsException() {
		
		//Arrange
		Brand brand = new Brand();
		brand.setId(BRAND_ID);
		brand.setName("Adidas");

		doThrow(new RuntimeException(MSG_ERROR)).when(brandService).getById(BRAND_ID);
		
		//Act
		ResponseEntity<Brand> testResponse = (ResponseEntity<Brand>) brandController.getById(BRAND_ID);
		
		//Assert
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@SuppressWarnings("unchecked")
	@Test
	void testCreate() {

		// Arrange
		BrandDTO brand = new BrandDTO();
		brand.setName("Teste");
		brand.setId(BRAND_ID);
		brand.setPhone("3333333");

		doNothing().when(brandService).create(brand);

		// chama o método que será testado
		ResponseEntity<String> testResponse = (ResponseEntity<String>) brandController.create(brand);

		// verificar ou validar teste
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(testResponse.getBody()).isNotNull();
		assertThat(testResponse.getBody()).isEqualTo("Brand inserted successfully!");
		verify(brandService, times(1)).create(brand);
	}

	@Test
	void testCreate_WhenThrowsException() {
		
		//Arrange
		BrandDTO brand = new BrandDTO();
		brand.setName("Teste");
		brand.setAdress("Rua");
		brand.setPhone("333333");

		doThrow(new RuntimeException(MSG_ERROR)).when(brandService).create(brand);

		//Act
		ResponseEntity<?> testResponse = brandController.create(brand);

		//Assert
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(testResponse.getBody()).isEqualTo("Failed trying to insert new data, error message: " + MSG_ERROR);

	}

	@Test
	void testDelete() {
		
		//Arrange
		Brand brand = new Brand();
		brand.setId(BRAND_ID);

		when(brandService.delete(BRAND_ID)).thenReturn("Feito");
		
		//Act
		ResponseEntity<String> testResponse = brandController.delete(BRAND_ID);

		//Assert
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(testResponse.getBody()).isNotNull();
		assertThat(testResponse.getBody()).isEqualTo("Brand deleted succesfully");
		verify(brandService, times(1)).delete(BRAND_ID);

	}

	@Test
	void testDelete_WhenThrowsException() {
		
		//Arrange
		Brand brand = new Brand();
		brand.setId(BRAND_ID);

		doThrow(new RuntimeException(MSG_ERROR)).when(brandService).delete(BRAND_ID);

		//Act
		ResponseEntity<?> testResponse = brandController.delete(BRAND_ID);

		//Assert
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(testResponse.getBody()).isEqualTo("Internal error, message: " + MSG_ERROR);
	}

	@Test
	void testUpdate() {
		
		//Arrange
		BrandDTO brand = new BrandDTO();
		brand.setName("Teste");
		brand.setAdress("Rua");
		brand.setPhone("333333");

		when(brandService.update(brand)).thenReturn("Feito");

		//Act
		ResponseEntity<String> testResponse = brandController.update(brand);

		//Assert
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(testResponse.getBody()).isNotNull();
		assertThat(testResponse.getBody()).isEqualTo("Feito");
		verify(brandService, times(1)).update(brand);

	}

	@Test
	void testUpdate_WhenThrowsException() {

		//Arrange
		BrandDTO brand = new BrandDTO();
		brand.setName("Teste");

		doThrow(new RuntimeException(MSG_ERROR)).when(brandService).update(brand);

		//Act
		ResponseEntity<?> testResponse = brandController.update(brand);

		//Assert
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(testResponse.getBody()).isEqualTo("Error trying to update brand. Error message: " + MSG_ERROR);
	}

}
