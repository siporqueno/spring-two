package com.porejemplo.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.porejemplo.controller.repr.ProductRepr;
import com.porejemplo.service.model.LineItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CartServiceTest {

    private CartService cartService;

    @BeforeEach
    public void init() {
        cartService = new CartServiceImpl();
    }

    @Test
    public void testEmptyCart() {
        assertNotNull(cartService.getLineItems());
        assertEquals(0, cartService.getLineItems().size());
        assertEquals(BigDecimal.ZERO, cartService.calculateCartTotalValue());
    }

    @Test
    public void testAddProduct() {
        ProductRepr expectedProduct = new ProductRepr();
        expectedProduct.setId(1L);
        expectedProduct.setPrice(new BigDecimal(123));
        expectedProduct.setTitle("Product title");

        cartService.addProductQty(expectedProduct, "color", "material", "M", 1);

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(1, lineItems.size());

        LineItem lineItem = lineItems.get(0);
        assertEquals("color", lineItem.getColor());
        assertEquals("material", lineItem.getMaterial());
        assertEquals("M", lineItem.getSize());
        assertEquals(1, lineItem.getQty());

        assertEquals(expectedProduct.getId(), lineItem.getProductId());
        assertNotNull(lineItem.getProductRepr());
        assertEquals(expectedProduct.getTitle(), lineItem.getProductRepr().getTitle());
        assertEquals(expectedProduct.getPrice(), lineItem.getProductRepr().getPrice());
        assertEquals(expectedProduct.getId(), lineItem.getProductRepr().getId());
    }

    @Test
    public void testConstructorWithArgLineItems() {
        ProductRepr firstExpectedProduct = new ProductRepr();
        firstExpectedProduct.setId(1L);
        firstExpectedProduct.setPrice(new BigDecimal(123));
        firstExpectedProduct.setTitle("First product title");

        ProductRepr secondExpectedProduct = new ProductRepr();
        secondExpectedProduct.setId(2L);
        secondExpectedProduct.setPrice(new BigDecimal(456));
        secondExpectedProduct.setTitle("Second product title");

        List<LineItem> expectedLineItems = new ArrayList<>();
        expectedLineItems.add(new LineItem(firstExpectedProduct, 1, "colorOne", "materialOne", "M"));
        expectedLineItems.add(new LineItem(secondExpectedProduct, 2, "colorTwo", "materialTwo", "L"));

        List<LineItem> lineItems = new CartServiceImpl(expectedLineItems).getLineItems();

        assertNotNull(lineItems);
        assertEquals(2, lineItems.size());

        LineItem firstLineItem = lineItems.stream().filter(li -> li.getProductId() == 1).collect(Collectors.toList()).get(0);
        assertEquals("colorOne", firstLineItem.getColor());
        assertEquals("materialOne", firstLineItem.getMaterial());
        assertEquals("M", firstLineItem.getSize());
        assertEquals(1, firstLineItem.getQty());

        assertEquals(firstExpectedProduct.getId(), firstLineItem.getProductId());
        assertNotNull(firstLineItem.getProductRepr());
        assertEquals(firstExpectedProduct.getTitle(), firstLineItem.getProductRepr().getTitle());
        assertEquals(firstExpectedProduct.getPrice(), firstLineItem.getProductRepr().getPrice());
        assertEquals(firstExpectedProduct.getId(), firstLineItem.getProductRepr().getId());

        LineItem secondLineItem = lineItems.stream().filter(li -> li.getProductId() == 2).collect(Collectors.toList()).get(0);
        assertEquals("colorTwo", secondLineItem.getColor());
        assertEquals("materialTwo", secondLineItem.getMaterial());
        assertEquals("L", secondLineItem.getSize());
        assertEquals(2, secondLineItem.getQty());

        assertEquals(secondExpectedProduct.getId(), secondLineItem.getProductId());
        assertNotNull(secondLineItem.getProductRepr());
        assertEquals(secondExpectedProduct.getTitle(), secondLineItem.getProductRepr().getTitle());
        assertEquals(secondExpectedProduct.getPrice(), secondLineItem.getProductRepr().getPrice());
        assertEquals(secondExpectedProduct.getId(), secondLineItem.getProductRepr().getId());
    }

    @Test
    public void testCalculateCartTotalValue() {
        ProductRepr firstExpectedProduct = new ProductRepr();
        firstExpectedProduct.setId(1L);
        firstExpectedProduct.setPrice(new BigDecimal(123));
        firstExpectedProduct.setTitle("First product title");

        ProductRepr secondExpectedProduct = new ProductRepr();
        secondExpectedProduct.setId(2L);
        secondExpectedProduct.setPrice(new BigDecimal(456));
        secondExpectedProduct.setTitle("Second product title");

        List<LineItem> expectedLineItems = new ArrayList<>();
        expectedLineItems.add(new LineItem(firstExpectedProduct, 1, "colorOne", "materialOne", "M"));
        expectedLineItems.add(new LineItem(secondExpectedProduct, 2, "colorTwo", "materialTwo", "L"));

        cartService = new CartServiceImpl(expectedLineItems);

        assertNotNull(cartService);
        assertEquals(new BigDecimal(1035), cartService.calculateCartTotalValue());
    }

    @Test
    public void testAddTheSameProductOfTheSameColorMaterialSizeTwice() {
        ProductRepr expectedProduct = new ProductRepr();
        expectedProduct.setId(1L);
        expectedProduct.setPrice(new BigDecimal(123));
        expectedProduct.setTitle("Product title");

        cartService.addProductQty(expectedProduct, "color", "material", "M", 1);
        cartService.addProductQty(expectedProduct, "color", "material", "M", 3);

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(1, lineItems.size());

        LineItem lineItem = lineItems.get(0);
        assertEquals("color", lineItem.getColor());
        assertEquals("material", lineItem.getMaterial());
        assertEquals("M", lineItem.getSize());
        assertEquals(4, lineItem.getQty());

        assertEquals(expectedProduct.getId(), lineItem.getProductId());
        assertNotNull(lineItem.getProductRepr());
        assertEquals(expectedProduct.getTitle(), lineItem.getProductRepr().getTitle());
        assertEquals(expectedProduct.getPrice(), lineItem.getProductRepr().getPrice());
        assertEquals(expectedProduct.getId(), lineItem.getProductRepr().getId());

        assertEquals(new BigDecimal(492), cartService.calculateCartTotalValue());
    }

    @Test
    public void testAddTheSameProductOfTheSameColorMaterialButOfDifferentSizeTwice() {
        ProductRepr expectedProduct = new ProductRepr();
        expectedProduct.setId(1L);
        expectedProduct.setPrice(new BigDecimal(123));
        expectedProduct.setTitle("Product title");

        cartService.addProductQty(expectedProduct, "color", "material", "M", 1);
        cartService.addProductQty(expectedProduct, "color", "material", "L", 3);

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(2, lineItems.size());

        LineItem firstLineItem = lineItems.stream().filter(li -> li.getSize().equals("M")).collect(Collectors.toList()).get(0);
        assertEquals("color", firstLineItem.getColor());
        assertEquals("material", firstLineItem.getMaterial());
        assertEquals("M", firstLineItem.getSize());
        assertEquals(1, firstLineItem.getQty());

        assertEquals(expectedProduct.getId(), firstLineItem.getProductId());
        assertNotNull(firstLineItem.getProductRepr());
        assertEquals(expectedProduct.getTitle(), firstLineItem.getProductRepr().getTitle());
        assertEquals(expectedProduct.getPrice(), firstLineItem.getProductRepr().getPrice());
        assertEquals(expectedProduct.getId(), firstLineItem.getProductRepr().getId());

        LineItem secondLineItem = lineItems.stream().filter(li -> li.getSize().equals("L")).collect(Collectors.toList()).get(0);
        assertEquals("color", secondLineItem.getColor());
        assertEquals("material", secondLineItem.getMaterial());
        assertEquals("L", secondLineItem.getSize());
        assertEquals(3, secondLineItem.getQty());

        assertEquals(expectedProduct.getId(), secondLineItem.getProductId());
        assertNotNull(secondLineItem.getProductRepr());
        assertEquals(expectedProduct.getTitle(), secondLineItem.getProductRepr().getTitle());
        assertEquals(expectedProduct.getPrice(), secondLineItem.getProductRepr().getPrice());
        assertEquals(expectedProduct.getId(), secondLineItem.getProductRepr().getId());

        assertEquals(new BigDecimal(492), cartService.calculateCartTotalValue());
    }

    @Test
    public void testAddTwoDifferentProductsOfTheSameColorMaterialSize() {
        ProductRepr firstExpectedProduct = new ProductRepr();
        firstExpectedProduct.setId(1L);
        firstExpectedProduct.setPrice(new BigDecimal(123));
        firstExpectedProduct.setTitle("First product title");

        ProductRepr secondExpectedProduct = new ProductRepr();
        secondExpectedProduct.setId(2L);
        secondExpectedProduct.setPrice(new BigDecimal(456));
        secondExpectedProduct.setTitle("Second product title");

        cartService.addProductQty(firstExpectedProduct, "color", "material", "M", 1);
        cartService.addProductQty(secondExpectedProduct, "color", "material", "M", 2);

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(2, lineItems.size());

        LineItem firstLineItem = lineItems.stream().filter(li -> li.getProductId() == 1).collect(Collectors.toList()).get(0);
        assertEquals("color", firstLineItem.getColor());
        assertEquals("material", firstLineItem.getMaterial());
        assertEquals("M", firstLineItem.getSize());
        assertEquals(1, firstLineItem.getQty());

        assertEquals(firstExpectedProduct.getId(), firstLineItem.getProductId());
        assertNotNull(firstLineItem.getProductRepr());
        assertEquals(firstExpectedProduct.getTitle(), firstLineItem.getProductRepr().getTitle());
        assertEquals(firstExpectedProduct.getPrice(), firstLineItem.getProductRepr().getPrice());
        assertEquals(firstExpectedProduct.getId(), firstLineItem.getProductRepr().getId());

        LineItem secondLineItem = lineItems.stream().filter(li -> li.getProductId() == 2).collect(Collectors.toList()).get(0);
        assertEquals("color", secondLineItem.getColor());
        assertEquals("material", secondLineItem.getMaterial());
        assertEquals("M", secondLineItem.getSize());
        assertEquals(2, secondLineItem.getQty());

        assertEquals(secondExpectedProduct.getId(), secondLineItem.getProductId());
        assertNotNull(secondLineItem.getProductRepr());
        assertEquals(secondExpectedProduct.getTitle(), secondLineItem.getProductRepr().getTitle());
        assertEquals(secondExpectedProduct.getPrice(), secondLineItem.getProductRepr().getPrice());
        assertEquals(secondExpectedProduct.getId(), secondLineItem.getProductRepr().getId());

        assertEquals(new BigDecimal(1035), cartService.calculateCartTotalValue());
    }

    @Test
    public void testRemoveProductQty() {
        ProductRepr firstExpectedProduct = new ProductRepr();
        firstExpectedProduct.setId(1L);
        firstExpectedProduct.setPrice(new BigDecimal(123));
        firstExpectedProduct.setTitle("First product title");

        ProductRepr secondExpectedProduct = new ProductRepr();
        secondExpectedProduct.setId(2L);
        secondExpectedProduct.setPrice(new BigDecimal(456));
        secondExpectedProduct.setTitle("Second product title");

        cartService.addProductQty(firstExpectedProduct, "colorOne", "materialOne", "M", 1);
        cartService.addProductQty(secondExpectedProduct, "colorTwo", "materialTwo", "L", 4);

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(2, lineItems.size());

        cartService.removeProductQty(firstExpectedProduct, "colorOne", "materialOne", "M", 2);
        cartService.removeProductQty(secondExpectedProduct, "colorTwo", "materialTwo", "L", 2);
        cartService.removeProductQty(secondExpectedProduct, "colorThree", "materialTwo", "L", 1);
        cartService.removeProductQty(secondExpectedProduct, "colorTwo", "materialThree", "L", 1);
        cartService.removeProductQty(secondExpectedProduct, "colorTwo", "materialTwo", "XL", 1);

        lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(1, lineItems.size());

        LineItem lineItem = lineItems.get(0);
        assertEquals("colorTwo", lineItem.getColor());
        assertEquals("materialTwo", lineItem.getMaterial());
        assertEquals("L", lineItem.getSize());
        assertEquals(2, lineItem.getQty());
    }

    @Test
    public void testRemoveLineItem() {
        ProductRepr expectedProduct = new ProductRepr();
        expectedProduct.setId(1L);
        expectedProduct.setPrice(new BigDecimal(123));
        expectedProduct.setTitle("Product title");

        cartService.addProductQty(expectedProduct, "color", "material", "M", 1);
        cartService.addProductQty(expectedProduct, "color", "material", "L", 3);

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(2, lineItems.size());

        LineItem firstExpectedLineItem = new LineItem(expectedProduct, 1, "color", "material", "M");
        LineItem secondExpectedLineItem = new LineItem(expectedProduct, 3, "color", "material", "L");

        cartService.removeLineItem(firstExpectedLineItem);

        lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(1, lineItems.size());

        LineItem lineItem = lineItems.get(0);
        assertEquals("color", lineItem.getColor());
        assertEquals("material", lineItem.getMaterial());
        assertEquals("L", lineItem.getSize());
        assertEquals(3, lineItem.getQty());

        assertEquals(expectedProduct.getId(), lineItem.getProductId());
        assertNotNull(lineItem.getProductRepr());
        assertEquals(expectedProduct.getTitle(), lineItem.getProductRepr().getTitle());
        assertEquals(expectedProduct.getPrice(), lineItem.getProductRepr().getPrice());
        assertEquals(expectedProduct.getId(), lineItem.getProductRepr().getId());
    }

    @Test
    public void testClearCart() {
        ProductRepr firstExpectedProduct = new ProductRepr();
        firstExpectedProduct.setId(1L);
        firstExpectedProduct.setPrice(new BigDecimal(123));
        firstExpectedProduct.setTitle("First product title");

        ProductRepr secondExpectedProduct = new ProductRepr();
        secondExpectedProduct.setId(2L);
        secondExpectedProduct.setPrice(new BigDecimal(456));
        secondExpectedProduct.setTitle("Second product title");

        cartService.addProductQty(firstExpectedProduct, "colorOne", "materialOne", "M", 1);
        cartService.addProductQty(secondExpectedProduct, "colorTwo", "materialTwo", "L", 4);

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(2, lineItems.size());

        cartService.clearCart();

        assertNotNull(cartService.getLineItems());
        assertEquals(0, cartService.getLineItems().size());
        assertEquals(BigDecimal.ZERO, cartService.calculateCartTotalValue());
    }

    @Test
    public void testUpdateAllQty() {
        ProductRepr firstExpectedProduct = new ProductRepr();
        firstExpectedProduct.setId(1L);
        firstExpectedProduct.setPrice(new BigDecimal(123));
        firstExpectedProduct.setTitle("First product title");

        ProductRepr secondExpectedProduct = new ProductRepr();
        secondExpectedProduct.setId(2L);
        secondExpectedProduct.setPrice(new BigDecimal(456));
        secondExpectedProduct.setTitle("Second product title");

        cartService.addProductQty(firstExpectedProduct, "", "", "M", 1);
        cartService.addProductQty(firstExpectedProduct, "", "", "XL", 2);
        cartService.addProductQty(secondExpectedProduct, "", "", "L", 4);

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(3, lineItems.size());

        Map<String, String> paramMap = new HashMap<>();
        // The below key is the real life input from logs
        paramMap.put("_csrf=885d9d7d-6433-451e-9242-5695a2d3a424", "");
        paramMap.put("1_M", "0");
        paramMap.put("1_XL", "1");
        paramMap.put("2_L", "7");

        cartService.updateAllQty(paramMap);

        lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(2, lineItems.size());

        LineItem firstLineItem = lineItems.stream().filter(li -> li.getProductId() == 1).collect(Collectors.toList()).get(0);
        assertEquals("", firstLineItem.getColor());
        assertEquals("", firstLineItem.getMaterial());
        assertEquals("XL", firstLineItem.getSize());
        assertEquals(1, firstLineItem.getQty());

        assertEquals(firstExpectedProduct.getId(), firstLineItem.getProductId());
        assertNotNull(firstLineItem.getProductRepr());
        assertEquals(firstExpectedProduct.getTitle(), firstLineItem.getProductRepr().getTitle());
        assertEquals(firstExpectedProduct.getPrice(), firstLineItem.getProductRepr().getPrice());
        assertEquals(firstExpectedProduct.getId(), firstLineItem.getProductRepr().getId());

        LineItem secondLineItem = lineItems.stream().filter(li -> li.getProductId() == 2).collect(Collectors.toList()).get(0);
        assertEquals("", secondLineItem.getColor());
        assertEquals("", secondLineItem.getMaterial());
        assertEquals("L", secondLineItem.getSize());
        assertEquals(7, secondLineItem.getQty());

        assertEquals(secondExpectedProduct.getId(), secondLineItem.getProductId());
        assertNotNull(secondLineItem.getProductRepr());
        assertEquals(secondExpectedProduct.getTitle(), secondLineItem.getProductRepr().getTitle());
        assertEquals(secondExpectedProduct.getPrice(), secondLineItem.getProductRepr().getPrice());
        assertEquals(secondExpectedProduct.getId(), secondLineItem.getProductRepr().getId());
    }
}