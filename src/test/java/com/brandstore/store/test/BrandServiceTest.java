package com.brandstore.store.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BrandCategoryRepository brandCategoryRepository;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandService brandService;

    private BrandDTO brandDTO;
    private Brand brand;
    private Brand brand2;
    private Category category;
    private Category category2;
    private static final Long BRAND_ID = 1L;
    private static final Long BRAND_ID2 = 2L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        brandDTO = new BrandDTO();
        brandDTO.setId(BRAND_ID);
        brandDTO.setName("Adidas");
        brandDTO.setCategoriesId(Arrays.asList(BRAND_ID, BRAND_ID2));

        brand = new Brand();
        brand.setId(BRAND_ID);
        brand.setName("Adidas");

        brand2 = new Brand();
        brand2.setId(BRAND_ID2);
        brand2.setName("Puma");

        category = new Category();
        category.setId(BRAND_ID);
        category.setName("Sports");

        category2 = new Category();
        category2.setId(BRAND_ID2);
        category2.setName("Fashion");
    }

    @Test
    void testCreate() {
        when(brandMapper.covertToEntity(brandDTO)).thenReturn(brand);
        when(categoryRepository.getById(BRAND_ID)).thenReturn(category);
        when(categoryRepository.getById(BRAND_ID2)).thenReturn(category2);

        brandService.create(brandDTO);

        verify(brandRepository, times(1)).save(brand);
        verify(categoryRepository, times(1)).getById(BRAND_ID);
        verify(categoryRepository, times(1)).getById(BRAND_ID2);
        verify(brandCategoryRepository, times(2)).save(any(BrandCategory.class));
    }

    @Test
    void testCreate_WhenThrowException() {
        assertThatThrownBy(() -> brandService.create(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Atributes cannot by null");
    }

    @Test
    void testGetById() {
        when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.of(brand));

        Brand result = brandService.getById(BRAND_ID);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(BRAND_ID);
        assertThat(result.getName()).isEqualTo("Adidas");
        verify(brandRepository, times(1)).findById(BRAND_ID);
    }

    @Test
    void testGetById_WhenNotFound() {
        when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.empty());

        Brand result = brandService.getById(BRAND_ID);

        assertThat(result).isNull();
        verify(brandRepository, times(1)).findById(BRAND_ID);
    }

    @Test
    void testGetAll() {
        List<Brand> brandList = Arrays.asList(brand, brand2);

        when(brandRepository.findAll()).thenReturn(brandList);

        List<BrandDTO> result = brandService.getAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(BRAND_ID);
        assertThat(result.get(1).getId()).isEqualTo(BRAND_ID2);
        assertThat(result.get(0).getName()).isEqualTo("Adidas");
        assertThat(result.get(1).getName()).isEqualTo("Puma");
        verify(brandRepository, times(1)).findAll();
    }

    @Test
    void testGetAll_WhenEmpty() {
        when(brandRepository.findAll()).thenReturn(List.of());

        List<BrandDTO> result = brandService.getAll();

        assertThat(result).isNull();
        verify(brandRepository, times(1)).findAll();
    }
    
    @Test
    void testDelete() {
        when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.of(brand));

        String result = brandService.delete(BRAND_ID);

        assertThat(result).isEqualTo("Brand of ID " + BRAND_ID + " removed!");
        verify(brandRepository, times(1)).deleteById(BRAND_ID);
    }

    @Test
    void testDelete_WhenNotFound() {
        when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.empty());

        String result = brandService.delete(BRAND_ID);

        assertThat(result).isEqualTo("This brand ID " + BRAND_ID + " doesn't exist");
        verify(brandRepository, times(0)).deleteById(BRAND_ID);
    }

    @Test
    void testUpdate() {
        when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.of(brand));

        String result = brandService.update(brandDTO);

        assertThat(result).isEqualTo("Brand of ID " + BRAND_ID + " updated successfully!");
        verify(brandRepository, times(1)).save(brand);
    }

    @Test
    void testUpdate_WhenNotFound() {
        when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.empty());

        String result = brandService.update(brandDTO);

        assertThat(result).isEqualTo("Brand of ID " + brandDTO.getId() + " not found");
        verify(brandRepository, times(0)).save(any(Brand.class));
    }
}
