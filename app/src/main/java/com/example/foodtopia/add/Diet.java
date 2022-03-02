package com.example.foodtopia.add;

public class Diet {
    public String foodname;
    public String amount;
    public String amountQuantifier;
    public String calories;
    public String carbohydrate;
    public String fat;
    public String mealtime;
    public String protein;
    public String sodium;
    public String sugar;
    public String date;
    public String time;
    public String userid;
    public String userid_date;

    public Diet(){
    }

    public Diet (String foodname,String amount,String amountQuantifier, String calories, String carbohydrate,
                 String fat, String mealtime, String protein, String sodium, String sugar,
                 String date, String time, String userid, String userid_date){
        this.foodname = foodname;
        this.amount = amount;
        this.amountQuantifier = amountQuantifier;
        this.calories = calories;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.mealtime = mealtime;
        this.protein = protein;
        this.sodium = sodium;
        this.sugar = sugar;
        this.date = date;
        this.time = time;
        this.userid = userid;
        this.userid_date = userid_date;
    }

}
