package com.example.mp_team3;

public class PostModel {
    public String uid;
    public String title;
    public String price;
    public String category;
    public String postingTime;
    public String endTime;
    public String detail;

    public PostModel(String uid, String title, String price, String category, String postingTime, String endTime, String detail) {
        this.uid = uid;
        this.title = title;
        this.price = price;
        this.category = category;
        this.postingTime = postingTime;
        this.endTime = endTime;
        this.detail = detail;
    }
}
