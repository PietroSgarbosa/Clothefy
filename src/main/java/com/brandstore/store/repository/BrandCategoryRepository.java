package com.brandstore.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brandstore.store.entity.BrandCategory;

@Repository
public interface BrandCategoryRepository extends JpaRepository <BrandCategory, Long> {

}
