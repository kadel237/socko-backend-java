package com.soko_backend.service.product;



import com.soko_backend.dto.product.CreateProductRequest;
import com.soko_backend.dto.product.ProductResponse;
import com.soko_backend.entity.product.Product;
import com.soko_backend.entity.user.UserEntity;
import com.soko_backend.repository.ProductRepository;
import com.soko_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public ProductResponse createProduct(CreateProductRequest request, Long merchantId) {
        UserEntity merchant = userRepository.findById(merchantId)
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .imageUrl(request.getImageUrl())
                .merchant(merchant)
                .build();

        Product saved = productRepository.save(product);
        return toDto(saved);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByMerchant(Long merchantId) {
        return productRepository.findByMerchantId(merchantId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ProductResponse toDto(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .merchantId(product.getMerchant().getId())
                .build();
    }
}