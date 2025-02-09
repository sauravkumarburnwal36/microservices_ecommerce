package com.example.ecommerce.order_service.service;

import com.example.ecommerce.order_service.clients.InventoryFeignClient;
import com.example.ecommerce.order_service.dto.OrderRequestDto;
import com.example.ecommerce.order_service.entity.OrderStatus;
import com.example.ecommerce.order_service.entity.Orders;
import com.example.ecommerce.order_service.entity.OrdersItem;
import com.example.ecommerce.order_service.repository.OrdersRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final ModelMapper modelMapper;
    private final InventoryFeignClient inventoryFeignClient;

    public List<OrderRequestDto> getAllOrders(){
        log.info("Fetching all orders");
        List<Orders> orders=ordersRepository.findAll();
        return orders.stream().map(order->modelMapper.map(order, OrderRequestDto.class)).toList();
    }

    public OrderRequestDto getOrderById(Long orderId){
        log.info("Fetching order with id:{}",orderId);
        Orders orders=ordersRepository.findById(orderId).orElseThrow(()->new RuntimeException(
                "Order not found"));
        return modelMapper.map(orders, OrderRequestDto.class);
    }


 //   @Retry(name="inventoryRetry",fallbackMethod ="createOrdersFallback")
   // @RateLimiter(name="inventoryRateLimiter",fallbackMethod ="createOrdersFallback")
    @CircuitBreaker(name="inventoryCircuitBreaker",fallbackMethod ="createOrdersFallback")
    public OrderRequestDto createOrders(OrderRequestDto orderRequestDto) {
        log.info("Creating the order");
        Double totalPrice=inventoryFeignClient.reduceStocks(orderRequestDto);
        Orders orders=modelMapper.map(orderRequestDto,Orders.class);
        for(OrdersItem ordersItem: orders.getItems()){
            ordersItem.setOrders(orders);
        }
        orders.setTotalPrice(totalPrice);
        orders.setOrderStatus(OrderStatus.CONFIRMED);
        Orders savedOrder=ordersRepository.save(orders);
        return modelMapper.map(savedOrder,OrderRequestDto.class);
    }

    public OrderRequestDto createOrdersFallback(OrderRequestDto orderRequestDto,Throwable throwable){
        log.error("Fallback occured due to:{}",throwable.getMessage());
        return new OrderRequestDto();
    }
}
