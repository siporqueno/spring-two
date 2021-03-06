package com.porejemplo.service;

import com.fasterxml.jackson.annotation.*;
import com.porejemplo.controller.repr.ProductRepr;
import com.porejemplo.service.model.LineItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@Service
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartServiceImpl implements CartService, Serializable {

    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @JsonProperty("lineItems")
    private final Map<LineItem, Integer> lineItems;

    public CartServiceImpl() {
        this.lineItems = new ConcurrentHashMap<>();
    }

    @JsonCreator
    public CartServiceImpl(@JsonProperty("lineItems") List<LineItem> lineItems) {
        this.lineItems = lineItems.stream().collect(Collectors.toMap(li -> li, LineItem::getQty));
    }

    @JsonIgnore
    @Override
    public void addProductQty(ProductRepr productRepr, String color, String material, String size, int qty) {
        LineItem lineItem = new LineItem(productRepr, color, material, size);
        lineItems.put(lineItem, lineItems.getOrDefault(lineItem, 0) + qty);
    }

    @JsonIgnore
    @Override
    public void removeProductQty(ProductRepr productRepr, String color, String material, String size, int qty) {
        LineItem lineItem = new LineItem(productRepr, color, material, size);
        int currentQty = lineItems.getOrDefault(lineItem, 0);
        if (currentQty - qty > 0) {
            lineItems.put(lineItem, currentQty - qty);
        } else {
            lineItems.remove(lineItem);
        }
    }

    @JsonIgnore
    @Override
    public void removeLineItem(LineItem lineItem) {
        lineItems.remove(lineItem);
    }

    @JsonGetter
    @Override
    public List<LineItem> getLineItems() {
        lineItems.forEach(LineItem::setQty);
        return new ArrayList<>(lineItems.keySet());
    }

    @JsonIgnore
    @Override
    public BigDecimal calculateCartTotalValue() {
        return lineItems.keySet().stream().map(LineItem::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @JsonIgnore
    @Override
    public void updateAllQty(Map<String, String> paramMap) {
        paramMap.entrySet().forEach(paramItem -> {
            String[] params = paramItem.getKey().split("_");
            if (!params[0].isBlank()) {
                Long productId = Long.valueOf(params[0]);
                String size = params[1];
                Integer qty = Integer.parseInt(paramItem.getValue());
                LineItem lineItemToUpdate = new LineItem(productId, "", "", size);
                if (qty > 0) lineItems.put(lineItemToUpdate, qty);
                else lineItems.remove(lineItemToUpdate);
            }
        });
    }

    @Override
    public void clearCart() {
        lineItems.clear();
    }
}
