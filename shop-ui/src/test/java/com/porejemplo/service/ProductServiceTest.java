package com.porejemplo.service;

import com.porejemplo.controller.repr.ProductRepr;
import com.porejemplo.persist.model.Brand;
import com.porejemplo.persist.model.Category;
import com.porejemplo.persist.model.Product;
import com.porejemplo.persist.repo.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    private ProductService productService;

    private ProductRepository productRepository;

    @BeforeEach
    public void init() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    public void testFindById() {
        Category expectedCategory = new Category();
        expectedCategory.setId(1L);
        expectedCategory.setName("Category name");

        Brand expectedBrand = new Brand();
        expectedBrand.setId(1L);
        expectedBrand.setName("Brand name");

        Product expectedProduct = new Product();
        expectedProduct.setId(1L);
        expectedProduct.setTitle("Product name");
        expectedProduct.setCategory(expectedCategory);
        expectedProduct.setBrand(expectedBrand);
        expectedProduct.setPictures(new ArrayList<>());
        expectedProduct.setPrice(new BigDecimal(12345));

        when(productRepository.findById(eq(1L)))
                .thenReturn(Optional.of(expectedProduct));

        Optional<ProductRepr> opt = productService.findById(1L);

        assertTrue(opt.isPresent());
        assertEquals(expectedProduct.getId(), opt.get().getId());
        assertEquals(expectedProduct.getTitle(), opt.get().getTitle());
        assertEquals(expectedProduct.getPrice(), opt.get().getPrice());
        assertEquals(expectedProduct.getBrand().getName(), opt.get().getBrand());
        assertEquals(expectedProduct.getCategory().getName(), opt.get().getCategory());
    }

    @Test
    public void testFindAll() {
        Category firstExpectedCategory = new Category();
        firstExpectedCategory.setId(1L);
        firstExpectedCategory.setName("First category name");

        Category secondExpectedCategory = new Category();
        secondExpectedCategory.setId(2L);
        secondExpectedCategory.setName("Second category name");

        Brand expectedBrand = new Brand();
        expectedBrand.setId(1L);
        expectedBrand.setName("Brand name");

        Product firstExpectedProduct = new Product();
        firstExpectedProduct.setId(1L);
        firstExpectedProduct.setTitle("First product name");
        firstExpectedProduct.setCategory(firstExpectedCategory);
        firstExpectedProduct.setBrand(expectedBrand);
        firstExpectedProduct.setPictures(new ArrayList<>());
        firstExpectedProduct.setPrice(new BigDecimal(12345));

        Product secondExpectedProduct = new Product();
        secondExpectedProduct.setId(2L);
        secondExpectedProduct.setTitle("Second product name");
        secondExpectedProduct.setCategory(secondExpectedCategory);
        secondExpectedProduct.setBrand(expectedBrand);
        secondExpectedProduct.setPictures(new ArrayList<>());
        secondExpectedProduct.setPrice(new BigDecimal(23456));

        List<Product> listProducts = new ArrayList<>();
        listProducts.add(firstExpectedProduct);
        listProducts.add(secondExpectedProduct);


        when(productRepository.findAll())
                .thenReturn(listProducts);

        List<ProductRepr> listProductReprs = productService.findAll();
        assertNotNull(listProductReprs);
        assertEquals(2, listProductReprs.size());

        ProductRepr firstProductRepr = listProductReprs.get(0);

        assertEquals(firstExpectedProduct.getId(), firstProductRepr.getId());
        assertEquals(firstExpectedProduct.getTitle(), firstProductRepr.getTitle());
        assertEquals(firstExpectedProduct.getPrice(), firstProductRepr.getPrice());
        assertEquals(firstExpectedProduct.getBrand().getName(), firstProductRepr.getBrand());
        assertEquals(firstExpectedProduct.getCategory().getName(), firstProductRepr.getCategory());

        ProductRepr secondProductRepr = listProductReprs.get(1);

        assertEquals(secondExpectedProduct.getId(), secondProductRepr.getId());
        assertEquals(secondExpectedProduct.getTitle(), secondProductRepr.getTitle());
        assertEquals(secondExpectedProduct.getPrice(), secondProductRepr.getPrice());
        assertEquals(secondExpectedProduct.getBrand().getName(), secondProductRepr.getBrand());
        assertEquals(secondExpectedProduct.getCategory().getName(), secondProductRepr.getCategory());
    }
}
