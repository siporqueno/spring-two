package com.porejemplo.controller;

import com.porejemplo.service.CartService;
import com.porejemplo.service.OrderService;
import com.porejemplo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final static Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private final CartService cartService;

    private final OrderService orderService;

    private final UserService userService;

    @Autowired
    public OrderController(CartService cartService, OrderService orderService, UserService userService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/draft")
    public String draftOfOrderPage(Model model) {
        model.addAttribute("cartItems", cartService.getLineItems());
        model.addAttribute("subTotal", cartService.calculateCartSubTotal());
        return "register";
    }

    @GetMapping("/create")
    public String createOrder(Principal principal) {
        LOGGER.info("!!!!!!!!!!!!!!!!!!!!!!! Principal's name {} !!!!!!!!!!!!!!!!!!!!!!!!!!!!!", principal.getName());
        orderService.createOrder(userService.findUserByLogin(principal.getName()).get());
        return "redirect:/order";
    }

    @GetMapping
    public String listOrders(Principal principal, Model model) {

        model.addAttribute("orders", orderService.findAllByUser(userService.findUserByLogin(principal.getName()).get()));

        return "orders_list";
    }
}
