package com.porejemplo.service;

import com.porejemplo.persist.model.Order;
import com.porejemplo.persist.model.OrderItem;
import com.porejemplo.persist.model.User;
import com.porejemplo.service.model.LineItem;

import java.util.List;

public interface OrderService {

    Order createOrder(User user);

    OrderItem mapToOrderItem(LineItem lineItem);

    List<Order> findAllByUser(User user);

    List<Order> findAllByUserWithOrderItemsFetch(User user);
}
