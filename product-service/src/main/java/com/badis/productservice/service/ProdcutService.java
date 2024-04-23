package com.badis.productservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badis.productservice.dto.ProductRequest;
import com.badis.productservice.dto.ProductResponse;
import com.badis.productservice.model.Product;
import com.badis.productservice.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ProdcutService {
    @Autowired
    private ProductRepository productRepository;
    public ProdcutService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public void createProduct(ProductRequest productRequest) {
        
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        System.out.println("Product created");
        log.info("Product {} is saved", product.getId());

    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::maptoProductResponse).toList();
        //return products.stream().map(product -> maptoProductResponse(product)).toList();
        
        
    }

    private ProductResponse maptoProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
