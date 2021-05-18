package com.porejemplo.service;

import com.porejemplo.persist.model.Order;
import com.porejemplo.persist.model.OrderItem;
import com.porejemplo.persist.model.Product;
import com.porejemplo.persist.model.User;
import com.porejemplo.persist.repo.BrandRepository;
import com.porejemplo.persist.repo.CategoryRepository;
import com.porejemplo.persist.repo.OrderRepository;
import com.porejemplo.service.model.LineItem;
import com.porejemplo.repr.TextMessage;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final CartService cartService;

    private final PictureService pictureService;

    private final BrandRepository brandRepository;

    private final CategoryRepository categoryRepository;

    private final AmqpTemplate rabbitTemplate;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService, PictureService pictureService, BrandRepository brandRepository, CategoryRepository categoryRepository, AmqpTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.pictureService = pictureService;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Order createOrder(User user) {
        Order order = new Order(user);
        Order finalOrder = order;
        cartService.getLineItems().stream().forEach(lineItem -> order.addOrderItem(mapToOrderItem(lineItem)));
        cartService.clearCart();
        finalOrder = orderRepository.save(order);
        rabbitTemplate.convertAndSend("users.exchange", "shop", new TextMessage("shop",
                "Order id: " + finalOrder.getId() + ", Order value: " + finalOrder.getOrderValue()));
        return order;
    }

    private OrderItem mapToOrderItem(LineItem lineItem) {
        OrderItem orderItem = new OrderItem();
        Product product = new Product(
                lineItem.getProductId(),
                lineItem.getProductRepr().getTitle(),
                lineItem.getProductRepr().getDescription(),
                lineItem.getProductRepr().getPrice(),
                categoryRepository.findByName(lineItem.getProductRepr().getCategory()).get(),
                brandRepository.findByName(lineItem.getProductRepr().getBrand()).get(),
                pictureService.findPicturesByIds(lineItem.getProductRepr().getPictureIds())
        );
        orderItem.setProduct(product);
        orderItem.setQty(lineItem.getQty());
        orderItem.setColor(lineItem.getColor());
        orderItem.setMaterial(lineItem.getMaterial());
        orderItem.setSize(lineItem.getSize());
        return orderItem;
    }

    @Override
    public List<Order> findAllByUser(User user) {
        return orderRepository.findAllByUser(user);
    }
}
