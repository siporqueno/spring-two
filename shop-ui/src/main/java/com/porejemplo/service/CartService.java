package com.porejemplo.service;

import com.porejemplo.controller.repr.ProductRepr;
import com.porejemplo.service.model.LineItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CartService {

    void addProductQty(ProductRepr productRepr, String color, String material, String size, int qty);

    void removeProductQty(ProductRepr productRepr, String color, String material, String size, int qty);

    void removeLineItem(LineItem lineItem);

    List<LineItem> getLineItems();

    BigDecimal calculateCartSubTotal();

    void updateAllQty(Map<String, String> paramMap);

    void clearCart();
}
