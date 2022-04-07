package com.example.foodtopia.model;

public class User {
    private String id;
    private String username;
    private String fullname;
    private String imageurl;
    private String bio;
    private String age;
    private String calories_per_day;
    private String calories_taken;
    private String gender;
    private String height;
    private String weight;
    private String stess;
    private String target;
    private String workload;



    public User(String id, String username, String fullname, String imageurl, String bio) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.imageurl = imageurl;
        this.bio = bio;
    }





    public User(String username,String imageurl,String calories_per_day, String height,
                String weight, String stess, String target, String workload) {
        this.username = username;
        this.imageurl = imageurl;
        this.calories_per_day = calories_per_day;
        this.height = height;
        this.weight = weight;
        this.stess = stess;
        this.target = target;
        this.workload = workload;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCalories_per_day() {
        return calories_per_day;
    }

    public void setCalories_per_day(String calories_per_day) {
        this.calories_per_day = calories_per_day;
    }

    public String getCalories_taken() {
        return calories_taken;
    }

    public void setCalories_taken(String calories_taken) {
        this.calories_taken = calories_taken;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getStess() {
        return stess;
    }

    public void setStess(String stess) {
        this.stess = stess;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getWorkload() {
        return workload;
    }

    public void setWorkload(String workload) {
        this.workload = workload;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
