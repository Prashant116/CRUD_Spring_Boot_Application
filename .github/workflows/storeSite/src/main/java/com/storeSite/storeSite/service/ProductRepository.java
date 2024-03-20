package com.storeSite.storeSite.service;

import com.storeSite.storeSite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
