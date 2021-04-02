package com.porejemplo.controller;

import com.porejemplo.error.NotFoundException;
import com.porejemplo.persist.model.Category;
import com.porejemplo.persist.repo.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String listPage(Model model) {
        logger.info("List page for categories requested.");

        model.addAttribute("categories", categoryRepository.findAll());
        return "category";
    }

    @GetMapping("/{id}")
    public String editPage(@PathVariable("id") Long id, Model model) {
        logger.info("Edit page for category with id {} requested", id);

        model.addAttribute("category", categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new));
        return "category_form";
    }

    @GetMapping("/new")
    public String create(Model model) {
        logger.info("new endpoint requested for category");

        model.addAttribute("category", new Category());
        return "category_form";
    }

    @PostMapping("/update")
    public String update(Category category, Model model) {
        logger.info("update endpoint requested for category");

        logger.info("Updating category with id {}", category.getId());
        Category savedCategory = categoryRepository.save(category);
        if (category.getId() == null) {
            category.setId(savedCategory.getId());
        }
        return "redirect:/category";
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") Long id) {
        logger.info("Delete endpoint requested for category with id {}", id);
        categoryRepository.deleteById(id);
        return "redirect:/category";
    }
}
