package com.example.foodtopia;

public class restaurant_product_get_set {
    private String caloris, food_name;

    public restaurant_product_get_set(String caloris, String food_name) {
        this.caloris = caloris;
        this.food_name = food_name;
    }

    public restaurant_product_get_set() {

    }

    public String getCaloris() {
        return caloris;
    }

    public void setCaloris(String caloris) {
        this.caloris = caloris;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }
}
