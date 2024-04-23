package com.badis.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.badis.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
    
}
