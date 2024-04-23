package com.badis.orderservice.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.badis.orderservice.dto.InventoryResponse;
import com.badis.orderservice.dto.OrderLineItemsdto;
import com.badis.orderservice.dto.OrderRequest;
import com.badis.orderservice.model.Order;
import com.badis.orderservice.model.OrderLineItems;
import com.badis.orderservice.repository.OrderRepository;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final Tracer tracer;

    @Autowired
    private WebClient webClient;

    public OrderService(OrderRepository orderRepository, Tracer tracer) {
        this.orderRepository = orderRepository;
        this.tracer = tracer;
    }

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        // orderRequest.getOrderLineItems().forEach(orderLineItem -> {
        // order.addOrderLineItem(orderLineItem);
        // });
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapDto)
                .toList();

        order.setOrderLineList(orderLineItems);

        List<String> skuCodes = order.getOrderLineList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();
        Span InventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");
        try (Tracer.SpanInScope ws = tracer.withSpan(InventoryServiceLookup.start())) {
            InventoryResponse[] inventoryResponseArray = webClient.get()
                    .uri("http://localhost:8082/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();
            Boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                    .allMatch(InventoryResponse::isInStock);

            if (allProductsInStock) {
                orderRepository.save(order);
                return "Order Placed Successfully";
            } else {
                throw new IllegalArgumentException("Product is out of stock");
            }
        } finally {
            InventoryServiceLookup.end();
        }

    }

    private OrderLineItems mapDto(OrderLineItemsdto orderLineItemsdto) {
        return OrderLineItems.builder()
                .skuCode(orderLineItemsdto.getSkuCode())
                .price(orderLineItemsdto.getPrice())
                .quantity(orderLineItemsdto.getQuantity())
                .build();
    }
}
