package com.porejemplo.service;

import com.porejemplo.controller.repr.ProductRepr;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductRepr> findAll();

    Optional<ProductRepr> findById(Long id);

    Page<ProductRepr> findByFilter(Long categoryId, Integer page, Integer size);
}
