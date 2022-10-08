package com.ayoub.productservice.service;


import com.ayoub.productservice.entity.Product;
import com.ayoub.productservice.exception.ProductServiceCustomException;
import com.ayoub.productservice.model.ProductRequest;
import com.ayoub.productservice.model.ProductResponse;
import com.ayoub.productservice.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Log4j2
public class ProductServiceImpl implements IProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding product....");
        Product product = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("product created");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long id) {
        log.info("get product for product id: {}", id);
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductServiceCustomException("product with given id not found", "PRODUCT NOT FOUND"));
        ProductResponse productResponse = new ProductResponse();
        copyProperties(product, productResponse);
        return productResponse;

    }

    @Override
    public long reduceQuantity(long productId, long quantity) {
        log.info("reduce quantity {} for product id {}", quantity, productId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductServiceCustomException("product with the id given was not found", "PRODUCT NOT FOUND"));
        if (product.getQuantity() - quantity <= 0) {
            throw new ProductServiceCustomException("qunatity is insufficcient");
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        return product.getQuantity();
    }
}
