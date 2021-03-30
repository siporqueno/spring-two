package com.porejemplo.service;

import com.porejemplo.persist.model.Category;
import com.porejemplo.persist.model.Product;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class ProductRepr {

    private Long id;

    @NotEmpty
    private String title;

    private String description;

    @PositiveOrZero
    private BigDecimal price;

    private Category category;

    public ProductRepr() {
    }

    public ProductRepr(@NotEmpty String title, String description, @PositiveOrZero BigDecimal price, Category category) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public ProductRepr(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.category=product.getCategory();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
