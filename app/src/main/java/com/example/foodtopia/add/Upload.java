package com.example.foodtopia.add;

public class Upload {
    public String userid;
    public String imageUrl;
    public String date;
    public String mealtime;

    public Upload(){}

    public Upload(String userid,String date, String mealtime, String imageUrl){
        this.userid = userid;
        this.date = date;
        this.mealtime = mealtime;
        this.imageUrl = imageUrl;
    }

}
