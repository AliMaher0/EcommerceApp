package com.example.ecommerceapp.models;

public class CategoryModel {

    String img_url;
    String Name;
    String type;

    public CategoryModel() {
    }

    public CategoryModel(String img_url, String name, String type) {
        this.img_url = img_url;
        this.Name = name;
        this.type = type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
