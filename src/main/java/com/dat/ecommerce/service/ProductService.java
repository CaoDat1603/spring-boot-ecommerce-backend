package com.dat.ecommerce.service;

import com.dat.ecommerce.dto.request.CreateProductRequest;
import com.dat.ecommerce.dto.request.ProductFilterRequest;
import com.dat.ecommerce.dto.request.UpdateProductRequest;
import com.dat.ecommerce.dto.response.ProductResponse;
import com.dat.ecommerce.entity.Product;
import com.dat.ecommerce.entity.User;
import com.dat.ecommerce.enums.ProductStatus;
import com.dat.ecommerce.enums.Role;
import com.dat.ecommerce.exception.ProductNotFoundException;
import com.dat.ecommerce.exception.SkuAlreadyExistsException;
import com.dat.ecommerce.exception.UserNotFoundException;
import com.dat.ecommerce.repository.ProductRepository;
import com.dat.ecommerce.repository.UserRepository;
import com.dat.ecommerce.specification.ProductSpecification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        if(productRepository.existsBySku(request.getSku())) {
            throw new SkuAlreadyExistsException(
                    "Sku already exists"
            );
        }

        Product product = new Product(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock(),
                request.getSku(),
                ProductStatus.PENDING);

        Product savedProduct = productRepository.save(product);

        return new ProductResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::new)
                .toList();
    }

    public  ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        return new ProductResponse(product);
    }

    public ProductResponse getProductBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with sku: " + sku));

        return new ProductResponse(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(
            String email,
            ProductFilterRequest filter,
            Pageable pageable
    ) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        )
                );

        Specification<Product> specification =
                (root, query, cb) -> null;

        if (user.getRole() == Role.USER) {
            specification =
                    specification.and(
                            ProductSpecification.hasStatus(
                                    ProductStatus.ACTIVE)
                    );

        } else {

            if (filter.getStatus() != null) {
                specification =
                        specification.and(
                                ProductSpecification.hasStatus(
                                        filter.getStatus())
                        );
            }
        }

        if (filter.getName() != null && !filter.getName().isBlank()) {
            specification =
                    specification.and(
                            ProductSpecification.nameContains(
                                    filter.getName())
                    );
        }

        if (filter.getSku() != null && !filter.getSku().isBlank()) {

            specification =
                    specification.and(
                            ProductSpecification.skuContains(
                                    filter.getSku())
                    );
        }

        if (filter.getMinPrice() != null) {

            specification =
                    specification.and(
                            ProductSpecification
                                    .priceGreaterThanOrEqual(
                                            filter.getMinPrice())
                    );
        }

        if (filter.getMaxPrice() != null) {
            specification =
                    specification.and(
                            ProductSpecification
                                    .priceLessThanOrEqual(
                                            filter.getMaxPrice())
                    );
        }

        if (filter.getMinStock() != null) {
            specification =
                    specification.and(
                            ProductSpecification
                                    .stockGreaterThanOrEqual(
                                            filter.getMinStock())
                    );
        }

        return productRepository
                .findAll(
                        specification,
                        pageable
                )
                .map(ProductResponse::new);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        if (request.getName() != null
                && !request.getName().equals(product.getName())) {
            product.setName(request.getName());
        }

        if (request.getSku() != null
                && !request.getSku().equals(product.getSku())) {

            if(productRepository.existsBySkuAndIdNot(request.getSku(), id)) {
                throw new SkuAlreadyExistsException(
                        "Sku already exists"
                );
            }

            product.setSku(request.getSku());
        }

        if (request.getStock() != null
                && (!Objects.equals(request.getStock(), product.getStock()))) {
            product.setStock(request.getStock());
        }

        if (request.getDescription() != null
                && !request.getDescription().equals(product.getDescription())) {
            product.setDescription(request.getDescription());
        }

        if (request.getPrice() != null
                && (!Objects.equals(request.getPrice(), product.getPrice()))) {
            product.setPrice(request.getPrice());
        }

        productRepository.saveAndFlush(product);

        return new ProductResponse(product);
    }

    @Transactional
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found with id: " + id ) );

        productRepository.delete(product);
    }
}
