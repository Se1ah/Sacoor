package com.zhyar;

public class PurchaseRow {
    protected Integer id;
    protected Integer purchase_id;
    protected Integer product_id;
    protected Integer quantity;
    protected Double unit_price;

    public PurchaseRow(){

    }
    public PurchaseRow(Integer id, Integer purchase_id, Integer product_id, Integer quantity, Double unit_price) {
        this.id = id;
        this.purchase_id = purchase_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPurchase_id() {
        return purchase_id;
    }

    public void setPurchase_id(Integer purchase_id) {
        this.purchase_id = purchase_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
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
