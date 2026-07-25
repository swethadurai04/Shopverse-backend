package com.store.backend.service;

import com.store.backend.dto.ProductDto;
import com.store.backend.entity.Category;
import com.store.backend.entity.Product;
import com.store.backend.repository.CategoryRepository;
import com.store.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductDto> getAll() {
        return productRepository.findAll().stream().map(this::toDto).toList();
    }

    public ProductDto getBySlug(String slug) {
        Product p = productRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toDto(p);
    }

    public List<ProductDto> getFeatured(int limit) {
        return productRepository.findByIsFeaturedTrue().stream()
                .limit(limit)
                .map(this::toDto)
                .toList();
    }

    public List<ProductDto> getByCategory(Long categoryId, int limit) {
        return productRepository.findByCategoryId(categoryId).stream()
                .limit(limit)
                .map(this::toDto)
                .toList();
    }

	public List<ProductDto> getFiltered(String categorySlug, String search, Double minPrice, Double maxPrice,
			Double minRating, String sort) {
		List<Product> all = productRepository.findAll();

		List<Product> filtered = all.stream()
				.filter(p -> categorySlug == null || categorySlug.isBlank()
						|| (p.getCategory() != null && categorySlug.equals(p.getCategory().getSlug())))
				.filter(p -> search == null || search.isBlank()
						|| p.getName().toLowerCase().contains(search.toLowerCase()))
				.filter(p -> minPrice == null || p.getPrice().doubleValue() >= minPrice)
				.filter(p -> maxPrice == null || p.getPrice().doubleValue() <= maxPrice)
				.filter(p -> minRating == null || minRating <= 0 || (p.getRating() != null && p.getRating() >= minRating))
				.collect(java.util.stream.Collectors.toList());

		if ("price_asc".equals(sort)) {
			filtered.sort((a, b) -> a.getPrice().compareTo(b.getPrice()));
		} else if ("price_desc".equals(sort)) {
			filtered.sort((a, b) -> b.getPrice().compareTo(a.getPrice()));
		} else if ("rating_desc".equals(sort)) {
			filtered.sort((a, b) -> Double.compare(b.getRating() != null ? b.getRating() : 0,
					a.getRating() != null ? a.getRating() : 0));
		}

		return filtered.stream().map(this::toDto).toList();
	}

    public ProductDto create(ProductDto dto) {
        Product p = Product.builder()
                .name(dto.getName())
                .slug(dto.getSlug())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .discountPrice(dto.getDiscountPrice())
                .brand(dto.getBrand())
                .imageUrl(dto.getImageUrl())
                .category(resolveCategory(dto.getCategoryId()))
                .stock(dto.getStock())
                .isFeatured(dto.getIsFeatured() != null && dto.getIsFeatured())
                .officialUrl(dto.getOfficialUrl())
                .build();
        return toDto(productRepository.save(p));
    }

    public ProductDto update(Long id, ProductDto dto) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        p.setName(dto.getName());
        p.setSlug(dto.getSlug());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setDiscountPrice(dto.getDiscountPrice());
        p.setBrand(dto.getBrand());
        p.setImageUrl(dto.getImageUrl());
        p.setCategory(resolveCategory(dto.getCategoryId()));
        p.setStock(dto.getStock());
        p.setIsFeatured(dto.getIsFeatured() != null && dto.getIsFeatured());
        p.setOfficialUrl(dto.getOfficialUrl());
        return toDto(productRepository.save(p));
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    private Category resolveCategory(Long categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    private ProductDto toDto(Product p) {
        return ProductDto.builder()
                .id(p.getId()).name(p.getName()).slug(p.getSlug())
                .description(p.getDescription()).price(p.getPrice())
                .discountPrice(p.getDiscountPrice())
                .brand(p.getBrand())
                .imageUrl(p.getImageUrl())
                .categoryId(p.getCategory() != null ? p.getCategory().getId() : null)
                .stock(p.getStock())
                .isFeatured(p.getIsFeatured())
                .rating(p.getRating())
                .reviewCount(p.getReviewCount())
                .officialUrl(p.getOfficialUrl())
                .build();
    }
}