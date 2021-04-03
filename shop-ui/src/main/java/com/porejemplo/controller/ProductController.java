package com.porejemplo.controller;

import com.porejemplo.error.NotFoundException;
import com.porejemplo.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String productListPage(Model model) {
        logger.info("Product list page");

        model.addAttribute("products", productService.findAll());
        return "categories-left-sidebar";
    }

    @RequestMapping("/product/{id}")
    public String productPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.findById(id)
                .orElseThrow(NotFoundException::new));
        logger.info("Product description: "+productService.findById(id).get().getDescription());
        return "product-details";
    }
}
