package com.brandstore.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brandstore.store.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository <Brand, Long> {

}
