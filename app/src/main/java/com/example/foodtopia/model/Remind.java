package com.example.foodtopia.model;

public class Remind {
    private String medicine;
    private String time;
    private String userid;
    private String des;
    private String remindeid;

    public Remind(String medicine, String time, String userid, String des, String remindeid) {
        this.medicine = medicine;
        this.time = time;
        this.userid = userid;
        this.des = des;
        this.remindeid = remindeid;
    }


    public Remind() {
    }

    public String getRemindeid() {
        return remindeid;
    }

    public void setRemindeid(String remindeid) {
        this.remindeid = remindeid;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
