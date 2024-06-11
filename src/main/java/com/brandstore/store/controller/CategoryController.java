package com.brandstore.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.brandstore.store.dto.CategoryDTO;
import com.brandstore.store.service.CategoryService;

@RestController
@RequestMapping(value="/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping(value="/getAll")
	public @ResponseBody ResponseEntity<List<CategoryDTO>> getAll() {
		return ResponseEntity.status(HttpStatus.OK).body(getCategoryService().getAll());
	}
	
	@PostMapping(value="/create")
	public @ResponseBody ResponseEntity<String> create(@RequestBody CategoryDTO categoryDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(getCategoryService().create(categoryDTO));
	}
	
	private CategoryService getCategoryService() {
		return categoryService;
	}

}
