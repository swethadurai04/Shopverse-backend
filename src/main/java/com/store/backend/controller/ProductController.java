package com.store.backend.controller;

import com.store.backend.dto.ProductDto;
import com.store.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) String sort) {
        return ResponseEntity.ok(productService.getFiltered(category, search, minPrice, maxPrice, minRating, sort));
    }

    @GetMapping("/featured")
    public ResponseEntity<List<ProductDto>> getFeatured(
            @RequestParam(defaultValue = "8") int limit) {
        return ResponseEntity.ok(productService.getFeatured(limit));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "4") int limit) {
        return ResponseEntity.ok(productService.getByCategory(categoryId, limit));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProductDto> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(productService.getBySlug(slug));
    }
}