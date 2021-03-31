package com.porejemplo.service;

import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ItemService<T> {

    List<T> findAll();

    Page<T> findWithFilter(String likeFilter, BigDecimal minFilter, BigDecimal maxFilter, Integer page, Integer size, String sortField);

    Optional<T> findById(long id);

    void save(T item);

    void delete(long id);

}
