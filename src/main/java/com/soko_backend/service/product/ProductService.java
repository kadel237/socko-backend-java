package com.soko_backend.service.product;


import com.soko_backend.dto.product.CreateProductRequest;
import com.soko_backend.dto.product.ProductResponse;

import java.util.List;

public interface ProductService {
        ProductResponse createProduct(CreateProductRequest request, Long merchantId);
        List<ProductResponse> getAllProducts();
        List<ProductResponse> getProductsByMerchant(Long merchantId);
    }

