package com.store.backend.repository;

import com.store.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySlug(String slug);

    List<Product> findByIsFeaturedTrue();
    List<Product> findByCategoryId(Long categoryId);
}