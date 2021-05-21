package com.porejemplo.controller;

import com.porejemplo.controller.repr.CartItemRepr;
import com.porejemplo.controller.repr.ProductRepr;
import com.porejemplo.error.NotFoundException;
import com.porejemplo.service.CartService;
import com.porejemplo.service.ProductService;
import com.porejemplo.service.model.LineItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
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
        model.addAttribute("subTotal", cartService.calculateCartTotalValue());
        return "shopping_cart";
    }

    @PostMapping
    public String addToCart(CartItemRepr cartItemRepr) {
        ProductRepr productRepr = productService.findById(cartItemRepr.getProductId())
                .orElseThrow(NotFoundException::new);
        cartService.addProductQty(productRepr, "", "", cartItemRepr.getSize(), cartItemRepr.getQty());
        return "redirect:/cart";
    }

    @GetMapping("/remove")
    public String removeLineItem(CartItemRepr cartItemRepr) {
        cartService.removeLineItem(new LineItem(cartItemRepr.getProductId(), "", "", cartItemRepr.getSize()));
        return "redirect:/cart";
    }

    @PostMapping("/update-all-qty")
    public String updateAllQty(@RequestParam Map<String, String> paramMap) {
        logger.info("Product Qty Map: {}", paramMap);

        // Sample of the above logger  output: Product Qty Map: {_csrf=885d9d7d-6433-451e-9242-5695a2d3a424, 1_M=2}
        // In order to avoid mistakes in CartServiceImpl we will have to filter out (exclude) from further parsing and processing this csrf token

        cartService.updateAllQty(paramMap);
        return "redirect:/cart";
    }

    @GetMapping("/proceed-to-checkout")
    public String proceedToCheckout(Principal principal) {

        if (principal != null) {
            return "redirect:/order/draft";
        }

        return "checkout";
    }
}
