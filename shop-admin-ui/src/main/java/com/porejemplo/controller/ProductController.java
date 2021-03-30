package com.porejemplo.controller;

import com.porejemplo.persist.model.Product;
import com.porejemplo.persist.repo.CategoryRepository;
import com.porejemplo.service.ItemService;
import com.porejemplo.service.ProductRepr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private ItemService<ProductRepr> productService;

    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductController(ItemService<ProductRepr> productService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String listPage(Model model,
                           @RequestParam("titleFilter") Optional<String> titleFilter,
                           @RequestParam("minPrice") Optional<BigDecimal> minPrice,
                           @RequestParam("maxPrice") Optional<BigDecimal> maxPrice,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size,
                           @RequestParam("sortField") Optional<String> sortField) {
        logger.info("List page requested");

        Page<ProductRepr> products = productService.findWithFilter(
                titleFilter.orElse(null),
                minPrice.orElse(null),
                maxPrice.orElse(null),
                page.orElse(1) - 1,
                size.orElse(3),
                sortField.orElse(null)
        );

        model.addAttribute("products", products);
        model.addAttribute("previousPageNumber", products.previousOrFirstPageable().getPageNumber() + 1);
        model.addAttribute("nextPageNumber", products.nextOrLastPageable().getPageNumber() + 1);
        return "product";
    }

    @GetMapping("/{id}")
    public String editPage(@PathVariable("id") Long id, Model model) {
        logger.info("Edit page for id {} requested", id);

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("product", productService.findById(id)
                .orElseThrow(NotFoundException::new));
        return "product_form";
    }

    @PostMapping("/update")
    public String update(@Valid ProductRepr productRepr, BindingResult result, Model model) {
        logger.info("Update endpoint requested");

        model.addAttribute("categories", categoryRepository.findAll());
        if (result.hasErrors()) {
            return "product_form";
        }

        logger.info("Updating product with id {}", productRepr.getId());
        productService.save(productRepr);
        return "redirect:/product";
    }

    @GetMapping("/new")
    public String create(Model model) {
        logger.info("New endpoint requested");

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("product", new Product());
        return "product_form";
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") Long id) {
        logger.info("Delete endpoint requested for product with id {}", id);
        productService.delete(id);
        return "redirect:/product";
    }
}
