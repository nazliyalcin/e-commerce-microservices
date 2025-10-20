package com.example.productservice.grpc;

import java.util.Optional;

import com.example.grpc.ProductRequest;
import com.example.grpc.ProductResponse;
import com.example.grpc.ProductServiceGrpc;
import com.example.grpc.UpdateStockRequest;
import com.example.grpc.UpdateStockResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ProductServiceGrpcImpl extends ProductServiceGrpc.ProductServiceImplBase {

    private final ProductRepository productRepository;

    public ProductServiceGrpcImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void getProductById(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductResponse response = ProductResponse.newBuilder()
                .setId(product.getId())
                .setName(product.getName())
                .setPrice(product.getPrice())
                .setStock(product.getStock())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    @Override
    public void updateStock(UpdateStockRequest request,
                            StreamObserver<UpdateStockResponse> responseObserver) {
        Optional<Product> productOpt = productRepository.findById((long) request.getProductId());

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (product.getStock() >= request.getQuantity()) {
                product.setStock(product.getStock() - request.getQuantity());
                productRepository.save(product);

                responseObserver.onNext(UpdateStockResponse.newBuilder().setSuccess(true).build());
            } else {
                responseObserver.onNext(UpdateStockResponse.newBuilder().setSuccess(false).build());
            }
        } else {
            responseObserver.onNext(UpdateStockResponse.newBuilder().setSuccess(false).build());
        }
        responseObserver.onCompleted();
    }

}

