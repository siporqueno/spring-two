package com.porejemplo.service;

import com.porejemplo.persist.model.Order;
import com.porejemplo.persist.model.User;

import java.util.List;

public interface OrderService {

    Order createOrder(User user);

    List<Order> findAllByUser(User user);
}
