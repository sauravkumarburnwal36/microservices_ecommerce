package com.example.ecommerce.order_service.controller;

import com.example.ecommerce.order_service.dto.OrderRequestDto;
import com.example.ecommerce.order_service.service.OrdersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrdersService ordersService;

    @GetMapping("/helloOrders")
    public String helloOrders(){
        return "Hello From Orders Service";
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
