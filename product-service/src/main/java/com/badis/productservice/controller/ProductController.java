package com.badis.productservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.badis.productservice.dto.ProductRequest;
import com.badis.productservice.dto.ProductResponse;
import com.badis.productservice.service.ProdcutService;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProdcutService prodcutService;
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return prodcutService.getAllProducts();
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void creaProduct(@RequestBody ProductRequest productRequest) {
        prodcutService.createProduct(productRequest);
    }
}
