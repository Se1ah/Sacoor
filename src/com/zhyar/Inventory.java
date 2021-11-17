package com.zhyar;

public class Inventory {
  /*These attributes must be identical to your products table columns in your database
    The error you get if not identical category_id in database but category in class model (products)
    Oct 25, 2021 1:13:22 PM javafx.scene.control.cell.PropertyValueFactory getCellDataReflectively
    WARNING: Can not retrieve property 'category_id' in PropertyValueFactory: javafx.scene.control.cell.PropertyValueFactory@2527b2d2 with provided class type: class com.zhyar.Products
    java.lang.IllegalStateException: Cannot read from unreadable property category_id
   */
  private Integer id;
  private String productname;
  private Integer quantity;
  private double purchaseprice;
  private double retailprice;


  public Inventory(Integer id, String productname, Integer quantity, double purchaseprice, double retailPrice) {
    this.id = id;
    this.productname = productname;
    this.quantity = quantity;
    this.purchaseprice = purchaseprice;
    this.retailprice = retailPrice;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getProductname() {
    return productname;
  }

  public void setProductname(String productname) {
    this.productname = productname;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public double getPurchaseprice() {
    return purchaseprice;
  }

  public void setPurchaseprice(double purchaseprice) {
    this.purchaseprice = purchaseprice;
  }

  public double getRetailprice() {
    return retailprice;
  }

  public void setRetailprice(double retailprice) {
    this.retailprice = retailprice;
  }
}
