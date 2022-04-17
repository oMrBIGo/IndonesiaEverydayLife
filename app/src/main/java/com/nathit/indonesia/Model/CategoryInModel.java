package com.nathit.indonesia.Model;

public class CategoryInModel {

    String cat_title = "";
    String cat_image = "";
    String cat_des = "";
    int index;

    public CategoryInModel() {
    }

    public CategoryInModel(String cat_title, String cat_image, String cat_des, int index) {
        this.cat_title = cat_title;
        this.cat_image = cat_image;
        this.cat_des = cat_des;
        this.index = index;
    }

    public String getCat_title() {
        return cat_title;
    }

    public void setCat_title(String cat_title) {
        this.cat_title = cat_title;
    }

    public String getCat_image() {
        return cat_image;
    }

    public void setCat_image(String cat_image) {
        this.cat_image = cat_image;
    }

    public String getCat_des() {
        return cat_des;
    }

    public void setCat_des(String cat_des) {
        this.cat_des = cat_des;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

