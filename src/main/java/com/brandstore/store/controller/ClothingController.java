package com.brandstore.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.brandstore.store.dto.ClothingDTO;
import com.brandstore.store.service.ClothingService;

@Controller
@RequestMapping("/clothes")
public class ClothingController {
	
	@Autowired
	private ClothingService clothingService;
	
	@GetMapping(value = "/getAll")
	public @ResponseBody ResponseEntity<?> getAll() {
		try {
			List<ClothingDTO> clothingListDTO = getClothingService().getAll();
			return ResponseEntity.status(HttpStatus.OK).body(clothingListDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Internal Server Error: " + e.getMessage());
		}
	}
	
	@GetMapping(value = "/getById/{id}")
	public @ResponseBody ResponseEntity<?> getById(@PathVariable Long id) {
		try {
			ClothingDTO clothingDTO = getClothingService().getById(id);
			return ResponseEntity.status(HttpStatus.OK).body(clothingDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Internal Server Error: " + e.getMessage());
		}
	}
	
	@PostMapping(value = "/create")
	public @ResponseBody ResponseEntity<?> create(@RequestBody ClothingDTO clothingDTO) {
		try {
			getClothingService().create(clothingDTO);
			return ResponseEntity.status(HttpStatus.OK).body("Training inserted successfully!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed trying to insert new data, error message: " + e.getMessage());
		}
	}
	
	private ClothingService getClothingService() {
		return clothingService;
	}

}
