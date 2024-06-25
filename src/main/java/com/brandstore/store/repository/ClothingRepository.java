package com.brandstore.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brandstore.store.entity.Clothing;

@Repository
public interface ClothingRepository extends JpaRepository <Clothing, Long> {

}
