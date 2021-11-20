package com.porejemplo.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.porejemplo.controller.repr.ProductRepr;

import java.math.BigDecimal;
import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class LineItem {

    private final Long productId;

    private final ProductRepr productRepr;

    private Integer qty;

    private final String color;

    private final String material;

    private final String size;

    private LineItem(Builder builder) {

        if (builder.productRepr != null) {
            this.productRepr = builder.productRepr;
            this.productId = this.productRepr.getId();
        } else {
            this.productRepr = null;
            this.productId = builder.productId;
        }

        this.qty = builder.qty;
        this.color = builder.color;
        this.material = builder.material;
        this.size = builder.size;
    }

    public Long getProductId() {
        return productId;
    }

    public ProductRepr getProductRepr() {
        return productRepr;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getColor() {
        return color;
    }

    public String getMaterial() {
        return material;
    }

    public String getSize() {
        return size;
    }

    @JsonIgnore
    public BigDecimal getTotal() {
        return productRepr.getPrice().multiply(new BigDecimal(qty));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineItem lineItem = (LineItem) o;
        return productId.equals(lineItem.productId) &&
                Objects.equals(color, lineItem.color) &&
                Objects.equals(material, lineItem.material) &&
                Objects.equals(size, lineItem.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, color, material, size);
    }

    public static Builder builder(ProductRepr productRepr, Long productId){
        return new Builder(productRepr, productId);
    }

    public static class Builder {

        private Long productId;

        private ProductRepr productRepr;

        private Integer qty;

        private String color;

        private String material;

        private String size;

        public Builder(ProductRepr productRepr, Long productId) {
            if (productRepr == null && productId == null) {
                throw new IllegalArgumentException("Both productRepr and productId cannot be null at the same time.");
            }

            if (productRepr != null) {
                this.productRepr = productRepr;
            } else {
                this.productId = productId;
            }

            this.qty = 1;
        }

        public Builder withQty(Integer qty) {
            this.qty = qty;
            return this;
        }

        public Builder withColor(String color) {
            this.color = color;
            return this;
        }

        public Builder withMaterial(String material) {
            this.material = material;
            return this;
        }

        public Builder withSize(String size) {
            this.size = size;
            return this;
        }

        public LineItem build() {
            return new LineItem(this);
        }

    }

}
