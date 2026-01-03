package com.gym.service;

import com.gym.dto.request.CreateProductRequest;
import com.gym.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse getProductById(UUID id);
    Page<ProductResponse> getAllProducts(Pageable pageable);
    ProductResponse updateProduct(UUID id, CreateProductRequest request);
    void deleteProduct(UUID id);
}
