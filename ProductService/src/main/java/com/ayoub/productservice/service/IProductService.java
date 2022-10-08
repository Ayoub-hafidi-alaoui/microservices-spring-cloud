package com.ayoub.productservice.service;

import com.ayoub.productservice.model.ProductRequest;
import com.ayoub.productservice.model.ProductResponse;

public interface IProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long id);

    long reduceQuantity(long productId, long quantity);
}
