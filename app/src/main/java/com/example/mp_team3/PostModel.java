package com.example.mp_team3;

import android.net.Uri;

public class PostModel {
    public String uid;
    public String title;
    public String price;
    public String category;
    public String postingTime;
    public String endTime;
    public String detail;
    public String prodPic;
    int postNum;

    public PostModel() {};

    public PostModel(String uid, String prodPic, String title, String price, String category, String postingTime, String endTime, String detail, int postNum) {
        this.uid = uid;
        this.prodPic = prodPic;
        this.title = title;
        this.price = price;
        this.category = category;
        this.postingTime = postingTime;
        this.endTime = endTime;
        this.detail = detail;
        this.postNum = postNum;
    }

    public String getUid() {
        return this.uid;
    }

    public String getProdPic() {
        return this.prodPic;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice(){
        return price;
    }

    public String getCategory(){
        return this.category;
    }

    public String getPostingTime(){
        return this.postingTime;
    }

    public String getEndTime(){
        return this.endTime;
    }

    public String getDetail() {
        return this.detail;
    }

    public int getPostNum() {
        return this.postNum;
    }

}
