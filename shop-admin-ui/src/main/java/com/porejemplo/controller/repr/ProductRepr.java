package com.porejemplo.controller.repr;

import com.porejemplo.controllers.repr.PictureRepr;
import com.porejemplo.persist.model.Brand;
import com.porejemplo.persist.model.Category;
import com.porejemplo.persist.model.Product;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductRepr {

    private Long id;

    @NotEmpty
    private String title;

    private String description;

    @PositiveOrZero
    private BigDecimal price;

    private Category category;

    private Brand brand;

    private List<PictureRepr> pictures;

    private MultipartFile[] newPictures;

    public ProductRepr() {
    }

    public ProductRepr(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.category=product.getCategory();
        this.brand=product.getBrand();
        this.pictures = product.getPictures().stream()
                .map(PictureRepr::new)
                .collect(Collectors.toList());
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

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public List<PictureRepr> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureRepr> pictures) {
        this.pictures = pictures;
    }

    public MultipartFile[] getNewPictures() {
        return newPictures;
    }

    public void setNewPictures(MultipartFile[] newPictures) {
        this.newPictures = newPictures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductRepr that = (ProductRepr) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
