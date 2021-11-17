package com.zhyar;

import java.util.Date;

public class Purchase {
    private Integer id;
    private Integer purchase_no;
    private Integer created_by;
    private Integer supplier_id;
    private Date created_at;


    public Purchase(Integer purchase_no, Integer created_by, Integer supplier_id) {
        this.purchase_no = purchase_no;
        this.created_by = created_by;
        this.supplier_id = supplier_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Integer created_by) {
        this.created_by = created_by;
    }

    public Integer getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(Integer supplier_id) {
        this.supplier_id = supplier_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
