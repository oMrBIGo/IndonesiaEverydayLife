package com.nathit.indonesia.Model;

public class CategoryModel {

    private String num_id;
    private String num_title;
    private String num_des;
    private String num_img;
    private String num_sound;

    public CategoryModel(String num_id, String num_title, String num_des, String num_img, String num_sound) {
        this.num_id = num_id;
        this.num_title = num_title;
        this.num_des = num_des;
        this.num_img = num_img;
        this.num_sound = num_sound;
    }

    public String getNum_id() {
        return num_id;
    }

    public void setNum_id(String num_id) {
        this.num_id = num_id;
    }

    public String getNum_title() {
        return num_title;
    }

    public void setNum_title(String num_title) {
        this.num_title = num_title;
    }

    public String getNum_des() {
        return num_des;
    }

    public void setNum_des(String num_des) {
        this.num_des = num_des;
    }

    public String getNum_img() {
        return num_img;
    }

    public void setNum_img(String num_img) {
        this.num_img = num_img;
    }

    public String getNum_sound() {
        return num_sound;
    }

    public void setNum_sound(String num_sound) {
        this.num_sound = num_sound;
    }
}
