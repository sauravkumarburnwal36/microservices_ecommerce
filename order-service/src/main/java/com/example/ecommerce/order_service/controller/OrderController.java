package com.example.ecommerce.order_service.controller;

import com.example.ecommerce.order_service.config.FeatureEnabledConfig;
import com.example.ecommerce.order_service.dto.OrderRequestDto;
import com.example.ecommerce.order_service.service.OrdersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
@RefreshScope
public class OrderController {
    private final OrdersService ordersService;
    private final FeatureEnabledConfig featureTrackingEnabled;
    @Value("${my.variable}")
    private String myVariable;
    @GetMapping("/helloOrders")
   // public String helloOrders(@RequestHeader("X-User-Id") Long userId){
    public String helloOrders() {
        if (featureTrackingEnabled.isFeatureTrackingEnabled()) {
            return "Feature Tracking Enabled wohooo, From Orders Service for variable:" + myVariable;
        } else {
            return "Feature Tracking Disabled awwww,From Orders Service for variable:" + myVariable;
        }
    }
    @GetMapping
    public ResponseEntity<List<OrderRequestDto>> getAllOrders(HttpServletRequest httpServletRequest){
        log.info("Fetching all orders via controller");
        List<OrderRequestDto> orders=ordersService.getAllOrders();
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderRequestDto> getOrderById(@PathVariable Long orderId){
        log.info("Fetching order with id:{} via controller",orderId);
        OrderRequestDto orderRequestDto=ordersService.getOrderById(orderId);
        return ResponseEntity.ok(orderRequestDto);
    }

    @PostMapping("/create-orders")
    public ResponseEntity<OrderRequestDto> createOrders(@RequestBody OrderRequestDto orderRequestDto){
        OrderRequestDto orderRequestDto1=ordersService.createOrders(orderRequestDto);
        return ResponseEntity.ok(orderRequestDto1);
    }
}
