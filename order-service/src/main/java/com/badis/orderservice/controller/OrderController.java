package com.badis.orderservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.badis.orderservice.dto.OrderRequest;
import com.badis.orderservice.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.observation.annotation.Observed;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    
    private final OrderService orderService;
    

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @Observed(name = "order", contextualName = "OrderController")
    @PostMapping
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackmethod")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
    }

    public CompletableFuture<String> fallbackmethod(OrderRequest orderRequest, RuntimeException e) {
        return CompletableFuture.completedFuture("Order Service is taking too long to respond or is down. Please try again later");
    }
}
