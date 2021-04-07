package com.porejemplo.controller;

import com.porejemplo.error.NotFoundException;
import com.porejemplo.persist.model.Brand;
import com.porejemplo.persist.model.Category;
import com.porejemplo.persist.repo.BrandRepository;
import com.porejemplo.persist.repo.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/brand")
public class BrandController {

    private static final Logger logger = LoggerFactory.getLogger(BrandController.class);

    private final BrandRepository brandRepository;

    @Autowired
    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @GetMapping
    public String listPage(Model model) {
        logger.info("List page for brands requested.");

        model.addAttribute("brands", brandRepository.findAll());
        return "brand";
    }

    @GetMapping("/{id}")
    public String editPage(@PathVariable("id") Long id, Model model) {
        logger.info("Edit page for brand with id {} requested", id);

        model.addAttribute("brand", brandRepository.findById(id)
                .orElseThrow(NotFoundException::new));
        return "brand_form";
    }

    @GetMapping("/new")
    public String create(Model model) {
        logger.info("new endpoint requested for brand");

        model.addAttribute("brand", new Brand());
        return "brand_form";
    }

    @PostMapping("/update")
    public String update(Brand brand, Model model) {
        logger.info("update endpoint requested for brand");

        logger.info("Updating brand with id {}", brand.getId());
        Brand savedBrand = brandRepository.save(brand);
        if (brand.getId() == null) {
            brand.setId(savedBrand.getId());
        }
        return "redirect:/brand";
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") Long id) {
        logger.info("Delete endpoint requested for brand with id {}", id);
        brandRepository.deleteById(id);
        return "redirect:/brand";
    }
}
