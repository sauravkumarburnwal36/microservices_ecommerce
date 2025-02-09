package com.example.ecommerce.inventory_service.controller;

import com.example.ecommerce.inventory_service.clients.OrdersFeignClient;
import com.example.ecommerce.inventory_service.dto.OrderRequestDto;
import com.example.ecommerce.inventory_service.dto.ProductDto;
import com.example.ecommerce.inventory_service.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;
    private final OrdersFeignClient ordersFeignClient;

    @GetMapping("/fetchOrders")
    public String fetchOrdersFromOrdersService(HttpServletRequest request){
        log.info("Get customer header:{}",request.getHeader("x-customer-header"));
//        ServiceInstance serviceInstance=discoveryClient.getInstances("order-service").getFirst();
//        return restClient.get()
//                .uri(serviceInstance.getUri()+"/orders/core/helloOrders")
//                .retrieve()
//                .body(String.class);
        return ordersFeignClient.helloOrders();
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllInventory(){
        List<ProductDto> inventories=productService.getAllInventory();
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getInventoryById(@PathVariable Long productId){
        ProductDto inventory=productService.getInventoryById(productId);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/reduce-stocks")
    public ResponseEntity<Double> reduceStocks(@RequestBody OrderRequestDto orderRequestDto){
        Double totalPrice=productService.reduceStocks(orderRequestDto);
        return ResponseEntity.ok(totalPrice);
    }


}
