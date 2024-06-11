package com.brandstore.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brandstore.store.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
