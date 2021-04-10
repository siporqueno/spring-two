package com.porejemplo.service;

import com.porejemplo.controller.repr.ProductRepr;
import com.porejemplo.service.model.LineItem;

import java.util.List;

public interface CartService {

    void addProductQty(ProductRepr productRepr, String color, String material, int qty);

    void removeProductQty(ProductRepr productRepr, String color, String material, int qty);

    List<LineItem> getLineItems();
}
