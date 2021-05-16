package com.porejemplo.service;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.porejemplo.controller.repr.ProductRepr;
import com.porejemplo.service.model.LineItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CartServiceImpl.class, name="CartServiceImpl")
})
public interface CartService {

    void addProductQty(ProductRepr productRepr, String color, String material, String size, int qty);

    void removeProductQty(ProductRepr productRepr, String color, String material, String size, int qty);

    void removeLineItem(LineItem lineItem);

    List<LineItem> getLineItems();

    BigDecimal calculateCartSubTotal();

    void updateAllQty(Map<String, String> paramMap);

    void clearCart();
}
