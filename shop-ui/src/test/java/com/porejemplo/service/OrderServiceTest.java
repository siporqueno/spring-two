package com.porejemplo.service;

import com.porejemplo.controller.repr.ProductRepr;
import com.porejemplo.persist.model.*;
import com.porejemplo.persist.repo.BrandRepository;
import com.porejemplo.persist.repo.CategoryRepository;
import com.porejemplo.persist.repo.OrderRepository;
import com.porejemplo.service.model.LineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

    private OrderService orderService;

    private OrderRepository orderRepository;

    private CartService cartService;

    private PictureService pictureService;

    private BrandRepository brandRepository;

    private CategoryRepository categoryRepository;

    @BeforeEach
    public void init() {
        orderRepository = mock(OrderRepository.class);
        cartService = mock(CartServiceImpl.class);
        pictureService = mock(PictureServiceBlobImpl.class);
        brandRepository = mock(BrandRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        orderService = new OrderServiceImpl(orderRepository, cartService, pictureService, brandRepository, categoryRepository);
    }

    @Test
    public void testMapToOrderItem() {
        Category expectedCategory = new Category();
        expectedCategory.setId(1L);
        expectedCategory.setName("Category name");

        Brand expectedBrand = new Brand();
        expectedBrand.setId(1L);
        expectedBrand.setName("Brand name");

        ProductRepr expectedProduct = new ProductRepr();
        expectedProduct.setId(1L);
        expectedProduct.setPrice(new BigDecimal(123));
        expectedProduct.setTitle("Product title");
        expectedProduct.setBrand("Brand name");
        expectedProduct.setCategory("Category name");

        LineItem expectedLineItem = new LineItem(expectedProduct, 2, "color", "material", "M");

        when(pictureService.findPicturesByIds(new ArrayList<>()))
                .thenReturn(new ArrayList<>());

        when(brandRepository.findByName(eq("Brand name")))
                .thenReturn(Optional.of(expectedBrand));

        when(categoryRepository.findByName(eq("Category name")))
                .thenReturn(Optional.of(expectedCategory));

        OrderItem orderItem = orderService.mapToOrderItem(expectedLineItem);

        assertNotNull(orderItem);
        assertNull(orderItem.getId());
        assertNull(orderItem.getOrder());
        assertEquals(expectedLineItem.getColor(), orderItem.getColor());
        assertEquals(expectedLineItem.getMaterial(), orderItem.getMaterial());
        assertEquals(expectedLineItem.getSize(), orderItem.getSize());
        assertEquals(expectedLineItem.getQty(), orderItem.getQty());

        assertNotNull(orderItem.getProduct());
        assertEquals(expectedProduct.getTitle(), orderItem.getProduct().getTitle());
        assertEquals(expectedProduct.getPrice(), orderItem.getProduct().getPrice());
        assertEquals(expectedProduct.getId(), orderItem.getProduct().getId());
        assertEquals(0, orderItem.getProduct().getPictures().size());
    }
}
