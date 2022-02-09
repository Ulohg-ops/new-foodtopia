package com.example.foodtopia;

public class restaurant_get_set {
    private int image;
    private String name;
    private int bg;

    public restaurant_get_set() {
        super();
    }

    public restaurant_get_set(int image, String name, int bg) {
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
