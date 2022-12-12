package com.example.mp_team3;

public class WishModel {
    public String title;
    public String prodPic;
    public String price;
    public String category;
    public int postNum;
    public boolean isWished;

    public WishModel() {}

    public WishModel(String title, String prodPic, String price, String category, boolean isWished, int postNum){
        this.title = title;
        this.prodPic = prodPic;
        this.price = price;
        this.category = category;
        this.isWished = isWished;
        this.postNum = postNum;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getProdPic() {
        return prodPic;
    }

    public String getCategory() {
        return category;
    }
}
