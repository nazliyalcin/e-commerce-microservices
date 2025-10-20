package com.example.orderservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.grpc.ProductRequest;
import com.example.grpc.ProductResponse;
import com.example.grpc.ProductServiceGrpc;
import com.example.grpc.UpdateStockRequest;
import com.example.grpc.UpdateStockResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @GrpcClient("product-service")
    private ProductServiceGrpc.ProductServiceBlockingStub productStub;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    

    public Order createOrder(Order order) {
        ProductResponse product = productStub.getProductById(
                ProductRequest.newBuilder().setId(order.getProductId()).build()
        );

        if (product.getStock() < order.getQuantity()) {
            throw new RuntimeException("Not enough stock for product " + product.getName());
        }
        
     // stok düşme çağrısı
        UpdateStockResponse updateResponse = productStub.updateStock(
                UpdateStockRequest.newBuilder()
                        .setProductId(order.getProductId().intValue())
                        .setQuantity(order.getQuantity())
                        .build()
        );

        if (!updateResponse.getSuccess()) {
            throw new RuntimeException("Stock update failed for product " + product.getName());
        }
        
                
        order.setTotalPrice(product.getPrice() * order.getQuantity());
        return orderRepository.save(order);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 🟠 ID’ye göre sipariş getir
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    
    public void deleteOrder(Long id) {
    	 orderRepository.deleteById(id);
    }
    
  
}
