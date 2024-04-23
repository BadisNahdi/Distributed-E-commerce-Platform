package com.badis.inventoryservice.Contoller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.badis.inventoryservice.Service.InventoryService;
import com.badis.inventoryservice.dto.InventoryResponse;

// import io.micrometer.observation.annotation.Observed;

import java.util.List;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/inventory")
// @Observed(name = "inventory", contextualName = "InventoryController")
public class InventoryController {
    private InventoryService inventoryService;
    InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    //http://localhost:8082/api/inventory?skuCode=1234&skuCode=1235
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) throws InterruptedException {
        return inventoryService.isInStock(skuCode);
    }
}
