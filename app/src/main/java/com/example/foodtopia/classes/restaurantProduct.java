package com.example.foodtopia.classes;

public class restaurantProduct {
    private String calories, name;

    public restaurantProduct(String calories, String name) {
        this.calories = calories;
        this.name = name;
    }

    public restaurantProduct() {

    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
