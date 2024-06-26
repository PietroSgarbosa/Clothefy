package com.brandstore.store.test;

import static org.assertj.core.api.Assertions.assertThat;
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
import com.brandstore.store.repository.BrandRepository;
import com.brandstore.store.service.BrandService;

@SpringBootTest
public class BrandServiceTest {

    @Mock
    @Autowired
    private BrandRepository brandRepository;

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
       
}
