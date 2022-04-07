package com.example.foodtopia.model;

import java.util.List;

public class RestaurantProduct {
    private String calories, name;

    public RestaurantProduct(String calories, String name) {
        this.calories = calories;
        this.name = name;
    }

    public RestaurantProduct() {

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

    public static class AllCategory {

        String categoryTitle;
        List<Post.CategoryItem> categoryItemList;

        public AllCategory(String categoryTitle, List<Post.CategoryItem> categoryItemList) {
            this.categoryTitle = categoryTitle;
            this.categoryItemList = categoryItemList;
        }

        public List<Post.CategoryItem> getCategoryItemList() {
            return categoryItemList;
        }

        public void setCategoryItemList(List<Post.CategoryItem> categoryItemList) {
            this.categoryItemList = categoryItemList;
        }

        public String getCategoryTitle() {
            return categoryTitle;
        }

        public void setCategoryTitle(String categoryTitle) {
            this.categoryTitle = categoryTitle;
        }
    }
}
