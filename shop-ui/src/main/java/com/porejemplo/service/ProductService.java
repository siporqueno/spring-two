package com.porejemplo.service;

import com.porejemplo.controller.repr.ProductRepr;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductRepr> findAll();

    Optional<ProductRepr> findById(Long id);

    List<ProductRepr> findByFilter(Long categoryId);
}
