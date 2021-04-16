package com.porejemplo.controller;

import com.porejemplo.error.NotFoundException;
import com.porejemplo.service.model.LineItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.porejemplo.controller.repr.CartItemRepr;
import com.porejemplo.controller.repr.ProductRepr;
import com.porejemplo.service.CartService;
import com.porejemplo.service.ProductService;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final static Logger logger = LoggerFactory.getLogger(CartController.class);

    public final CartService cartService;

    public final ProductService productService;

    @Autowired
    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("lineItems", cartService.getLineItems());
        model.addAttribute("subTotal", cartService.calculateCartSubTotal());
        return "shopping-cart";
    }

    @PostMapping
    public String addToCart(CartItemRepr cartItemRepr) {
        ProductRepr productRepr = productService.findById(cartItemRepr.getProductId())
                .orElseThrow(NotFoundException::new);
        cartService.addProductQty(productRepr, "", "", cartItemRepr.getSize(), cartItemRepr.getQty());
        return "redirect:/cart";
    }

    @DeleteMapping
    public String removeLineItem(CartItemRepr cartItemRepr) {
        cartService.removeLineItem(new LineItem(cartItemRepr.getProductId(), "", "", cartItemRepr.getSize()));
        return "redirect:/cart";
    }

    @PostMapping("/update_all_qty")
    public String updateAllQty(@RequestParam Map<String, String > paramMap) {
        logger.info("Product Qty Map: {}", paramMap);
        cartService.updateAllQty(paramMap);
        return "redirect:/cart";
    }
}
