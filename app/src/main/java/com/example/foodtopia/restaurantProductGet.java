package com.example.foodtopia;

public class restaurantProductGet {
    private String calories, name;

    public restaurantProductGet(String calories, String name) {
        this.calories = calories;
        this.name = name;
    }

    public restaurantProductGet() {

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
