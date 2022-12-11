package com.example.mp_team3;

public class AuctionModel implements Comparable<AuctionModel> {
    public String uid;
    public String suggestPrice;
    public String profImg;

    public AuctionModel() {}

    public AuctionModel(String uid, String suggestPrice, String profImg) {
        this.uid = uid;
        this.suggestPrice = suggestPrice;
        this.profImg = profImg;
    }

    public String getUid() {
        return uid;
    }

    public String getSuggestPrice() {
        return suggestPrice;
    }

    public String getProfImg() {
        return profImg;
    }

    @Override
    public int compareTo(AuctionModel auctionModel) {
        if (Integer.parseInt(auctionModel.suggestPrice) < Integer.parseInt(suggestPrice)) {
            return 1;
        } else if (Integer.parseInt(auctionModel.suggestPrice) > Integer.parseInt(suggestPrice)) {
            return -1;
        } else {
            return 0;
        }
    }
}
