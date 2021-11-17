package com.zhyar.controller;

public class Purchase_row {
    private Integer id;
    private Integer product_id;
    private String name;
    private Integer quantity;
    private Double unit_price;

    public Purchase_row(Integer id, Integer product_id, String name, Integer quantity, Double unit_price) {
        this.id = id;
        this.product_id = product_id;
        this.name = name;
        this.quantity = quantity;
        this.unit_price = unit_price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }
}
