package com.zhyar;

public class Categories {
    private int id;
    private String category;

    public Categories(Integer id, String category){
        this.id = id;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public int getId(){
        return this.id;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
