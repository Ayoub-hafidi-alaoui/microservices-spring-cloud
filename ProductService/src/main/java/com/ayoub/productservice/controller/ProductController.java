package com.ayoub.productservice.controller;

import com.ayoub.productservice.model.ProductRequest;
import com.ayoub.productservice.model.ProductResponse;
import com.ayoub.productservice.service.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    private IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Long> addProduct(@RequestBody ProductRequest productRequest) {
        long productId = productService.addProduct(productRequest);
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") long id) {
        ProductResponse productResponse = productService.getProductById(id);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/reduceQuantity/{id}")
    public ResponseEntity<Long> reduceQuantity(@PathVariable("id") long productId, @RequestParam long quantity) {
        long productQuantity = productService.reduceQuantity(productId, quantity);
        return new ResponseEntity<>( productQuantity,HttpStatus.OK);
    }

}
