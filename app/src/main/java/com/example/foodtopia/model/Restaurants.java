package com.example.foodtopia.model;

public class Restaurants {
    private int image;
    private String name;
    private int bg;

    public Restaurants() {
        super();
    }

    public Restaurants(int image, String name, int bg) {
        super();
        this.image = image;
        this.name = name;
        this.bg=bg;
    }

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
