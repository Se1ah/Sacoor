package com.zhyar;

public class Products {
    private Integer id;
    private String name;
    private String barcode;
    private Integer quantity;
    private Double price_in;
    private Double price_out;
    private Integer color_id;
    private Integer size_id;

    public Products(Integer id, String name, String barcode, Integer stock, Double priceIn, Double priceOut, Integer color, Integer size) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.quantity = stock;
        this.price_in = priceIn;
        this.price_out = priceOut;
        this.color_id = color;
        this.size_id = size;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice_in() {
        return price_in;
    }

    public void setPrice_in(Double price) {
        this.price_in = price;
    }

    public Double getPrice_out() {
        return price_out;
    }

    public void setPrice_out(Double price) {
        this.price_out = price;
    }

    public Integer getColor_id() {
        return color_id;
    }

    public void setColor_id(Integer color_id) {
        this.color_id = color_id;
    }

    public Integer getSize_id() {
        return size_id;
    }

    public void setSize_id(Integer size_id) {
        this.size_id = size_id;
    }

    @Override
    public String toString() {
        return name;
    }
}
