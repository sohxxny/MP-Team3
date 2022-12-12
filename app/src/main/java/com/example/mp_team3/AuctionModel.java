package com.example.mp_team3;

public class AuctionModel implements Comparable<AuctionModel> {
    public String uid;
    public int suggestPrice;
    public String profImg;

    public AuctionModel() {}

    public AuctionModel(String uid, int suggestPrice, String profImg) {
        this.uid = uid;
        this.suggestPrice = suggestPrice;
        this.profImg = profImg;
    }

    public String getUid() {
        return uid;
    }

    public int getSuggestPrice() {
        return suggestPrice;
    }

    public String getProfImg() {
        return profImg;
    }

    @Override
    public int compareTo(AuctionModel auctionModel) {
        if (auctionModel.suggestPrice < suggestPrice) {
            return 1;
        } else if (auctionModel.suggestPrice > suggestPrice) {
            return -1;
        } else {
            return 0;
        }
    }
}
