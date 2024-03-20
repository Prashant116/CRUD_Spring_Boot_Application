package com.storeSite.storeSite.service;

import com.storeSite.storeSite.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public void addProduct(Product product){
        productRepository.save(product);
    }

    public Product selectProduct(int id){
        return productRepository.findById(id).get();
    }

    public void deleteProduct(int id){
        productRepository.deleteById(id);
    }


}
