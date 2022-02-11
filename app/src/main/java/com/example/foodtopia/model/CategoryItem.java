package com.example.foodtopia.model;

public class CategoryItem {

// here i am taking only image url. and this is as integer because i am using it from drawable file.

    Integer itemId;
    Integer imageUrl;
    String foodName;

//    public CategoryItem(Integer itemId, Integer imageUrl) {
//        this.itemId = itemId;
//        this.imageUrl = imageUrl;
//    }
//
//    public Integer getItemId() {
//        return itemId;
//    }
//
//    public void setItemId(Integer itemId) {
//        this.itemId = itemId;
//    }
//
//    public Integer getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(Integer imageUrl) {
//        this.imageUrl = imageUrl;
//    }


    public CategoryItem(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
