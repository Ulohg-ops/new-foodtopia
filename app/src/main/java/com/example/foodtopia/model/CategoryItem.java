package com.example.foodtopia.model;

public class CategoryItem {


    String foodName;
    String calories;
    String carbohydrate;
    String fat;
    String protein;
    String saturatedfat;
    String unsaturatedfat;
    String sodium;
    String sugars;

    public CategoryItem(String foodName, String calories) {
        this.foodName = foodName;
        this.calories = calories;
    }

    public CategoryItem(String foodName, String calories, String carbohydrate,
                        String fat, String protein, String saturatedfat,
                        String unsaturatedfat, String sodium, String sugars) {
        this.foodName = foodName;
        this.calories = calories;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.protein = protein;
        this.saturatedfat = saturatedfat;
        this.unsaturatedfat = unsaturatedfat;
        this.sodium = sodium;
        this.sugars = sugars;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public CategoryItem(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(String carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getSaturatedfat() {
        return saturatedfat;
    }

    public void setSaturatedfat(String saturatedfat) {
        this.saturatedfat = saturatedfat;
    }

    public String getUnsaturatedfat() {
        return unsaturatedfat;
    }

    public void setUnsaturatedfat(String unsaturatedfat) {
        this.unsaturatedfat = unsaturatedfat;
    }

    public String getSodium() {
        return sodium;
    }

    public void setSodium(String sodium) {
        this.sodium = sodium;
    }

    public String getSugars() {
        return sugars;
    }

    public void setSugars(String sugars) {
        this.sugars = sugars;
    }
}
