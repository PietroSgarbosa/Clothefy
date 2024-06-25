package com.brandstore.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.brandstore.store.dto.BrandDTO;
import com.brandstore.store.entity.Brand;
import com.brandstore.store.service.BrandService;

@Controller
@RequestMapping("/brands")
public class BrandController {
	
	@Autowired
	private BrandService brandService;
	
	private BrandService getBrandService() {
		return brandService;
	}
	
	@GetMapping(value = "/getById/{id}")
	public @ResponseBody ResponseEntity<?> getById(@PathVariable Long id) {
		try {
			Brand brand = getBrandService().getById(id);
			return ResponseEntity.status(HttpStatus.OK).body(brand);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/create")
	public @ResponseBody ResponseEntity<?> create(@RequestBody BrandDTO brandDTO) {
		try {
			getBrandService().create(brandDTO);
			return ResponseEntity.status(HttpStatus.OK).body("Brand inserted successfully!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed trying to insert new data, error message: " + e.getMessage());
		}
	}
	
	@GetMapping(value = "/getAll")
	public @ResponseBody ResponseEntity<?> getAll() {
		try {
			List<BrandDTO> brandListDTO = getBrandService().getAll();
			return ResponseEntity.status(HttpStatus.OK).body(brandListDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Internal Server Error: " + e.getMessage());
		}
	}
	
	@DeleteMapping("/delete")
	public @ResponseBody ResponseEntity<String> delete(@RequestParam Long id) {
		try {
			getBrandService().delete(id);
			return ResponseEntity.status(HttpStatus.OK).body("Brand deleted succesfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Internal error, message: " + e.getMessage());
		}
	}
	
	@PutMapping(value = "/update")
	public @ResponseBody ResponseEntity<String> update(@RequestBody BrandDTO brandDTO) {
		try {
			String message = getBrandService().update(brandDTO);
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error trying to update brand. Error message: " + e.getMessage());
		}
	}

}
