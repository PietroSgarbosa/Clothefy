package com.brandstore.store.test;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
//import static org.mockito.ArgumentMatchers.any;
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
	private static final String DATABASE_ERROR = "Database error";
	private static final String MESSAGE_SUCCESS = "Brand updated successufully";

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

	// Fazer um teste do getById e como bonus, fazer o teste da exceção do getAll
	// Começar aqui

	@SuppressWarnings("unchecked")
	@Test
	void testGetById() {
		// Arrange - organizar ou preparar os dados mockados
		Brand brand = new Brand();
		brand.setId(BRAND_ID);

		// Troca o serviço real pelo serviço mocado e traz os dados que preparamos para
		// teste
		when(brandService.getById(BRAND_ID)).thenReturn(brand);

		// Act - chamar o método que vai ser testado (chamar getById)
		ResponseEntity<Brand> testResponse = (ResponseEntity<Brand>) brandController.getById(BRAND_ID);

		// Assert - verifica ou validar o teste
		assertThat(testResponse).isNotNull();
		assertThat(testResponse.getBody().getId()).isEqualTo(BRAND_ID);
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		verify(brandService, times(1)).getById(BRAND_ID);
	}

	@SuppressWarnings("unchecked")
	@Test
	void testGetAll_WhenControllerThrowsException() {
		// Arrange
		when(brandService.getAll()).thenThrow(new RuntimeException(DATABASE_ERROR));

		// Act
		ResponseEntity<?> testResponse = brandController.getAll();

		// Assert
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(testResponse.getBody()).isEqualTo("Internal Server Error: " + DATABASE_ERROR);
		verify(brandService, times(1)).getAll();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testCreate() {
		//Arrange
		BrandDTO newBrand = new BrandDTO();
		newBrand.setName("Nike");
		newBrand.setAdress("Rua 1");
		newBrand.setPhone("123456");		
		
		doNothing().when(brandService).create(newBrand);
			
		//Act
		ResponseEntity<String> testResponse = (ResponseEntity<String>) brandController.create(newBrand);
		
		//Assert
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(testResponse.getBody()).isNotNull();
		assertThat(testResponse.getBody()).isEqualTo("Brand inserted successfully!");
		verify(brandService, times(1)).create(newBrand);		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testCreate_WhenControllerThrowsException() {
		//Arrange
		BrandDTO newBrand = new BrandDTO();
		newBrand.setName("Nike");
		newBrand.setAdress("Rua 1");
		newBrand.setPhone("123456");		
		
		doThrow(new RuntimeException(DATABASE_ERROR)).when(brandService).create(newBrand);
		
		//Act
		
		ResponseEntity<?> testResponse = brandController.create(newBrand);
		
		//Assert
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		//assertThat(testResponse.getBody()).isEqualTo("Internal Server Error: " + DATABASE_ERROR);
		verify(brandService, times(1)).create(newBrand);
	}
	
	
	@Test
	void testUpdate() {
		// Arrange
		
		BrandDTO brandToUpdate = new BrandDTO();
		brandToUpdate.setName("Nike");
		brandToUpdate.setAdress("Rua 1");
		brandToUpdate.setPhone("123456");
		
		when(brandService.update(brandToUpdate)).thenReturn(MESSAGE_SUCCESS);
		
		//Act
		
		ResponseEntity<String> testResponse = brandController.update(brandToUpdate);
		
		//Assert 
		
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(testResponse.getBody()).isEqualTo(MESSAGE_SUCCESS);
		verify(brandService, times(1)).update(brandToUpdate);
				
	}
	
	@Test
	void testUpdate_WhenControllerThrowsException() {
		//Arrange
		
		BrandDTO brandToUpdate = new BrandDTO();
		brandToUpdate.setName("Nike");
		brandToUpdate.setAdress("Rua 1");
		brandToUpdate.setPhone("123456");
		
		String errorMessage = "Error trying to update brand";
		
		when(brandService.update(brandToUpdate)).thenThrow(new RuntimeException(DATABASE_ERROR));
		
		// Act
		
		ResponseEntity<String> testResponse = brandController.update(brandToUpdate);
		
		//Assert
		
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(testResponse.getBody()).isEqualTo("Error trying to update brand. Error message: " + DATABASE_ERROR);
		verify(brandService, times(1)).update(brandToUpdate);
		}
	
	@Test
	void testDelete() {
		//Arrange
		
		when(brandService.delete(BRAND_ID)).thenReturn( "Brand of ID " + BRAND_ID + " removed!");
		
		//Act
		
		ResponseEntity<String> testResponse = brandController.delete(BRAND_ID);
		
		//Assert
				
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(testResponse.getBody()).isEqualTo("Brand deleted succesfully");
		verify(brandService, times(1)).delete(BRAND_ID);
	}
	
	@Test
	void TestDelete_WhenControllerThrowsException() {
		
		//Arrage
		
		doThrow(new RuntimeException(DATABASE_ERROR)).when(brandService).delete(BRAND_ID);
		
		//Act
		
		ResponseEntity<String> testResponse = brandController.delete(BRAND_ID);
		
		//Assert
		
		assertThat(testResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		verify(brandService, times(1)).delete(BRAND_ID);
	}
	
}
