package com.porejemplo.persist.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @Column(name="qty")
    private Integer qty;

    @Column(name="color")
    private String color;

    @Column(name="material")
    private String material;

    @Column(name = "size")
    private String size;

    public OrderItem() {
    }

    public OrderItem(Long id, Product product, Order order, Integer qty, String color, String material, String size) {
        this.id = id;
        this.product = product;
        this.order = order;
        this.qty = qty;
        this.color = color;
        this.material = material;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public BigDecimal getTotal(){
        return product.getPrice().multiply(new BigDecimal(qty));
    }
}
