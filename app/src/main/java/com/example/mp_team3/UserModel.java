package com.example.mp_team3;

import android.widget.EditText;

import java.util.ArrayList;

public class UserModel {
    public String nickname;
    public String profileImageUrl;
    public String uid;
    public String pushToken;
    public ArrayList<String> searchItem;

    public UserModel(String nickname, String profileImageUrl, String uid, ArrayList<String> searchItem) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.uid = uid;
        this.searchItem = searchItem;
    }

    public UserModel(String nickname, String profileImageUrl, String uid, String pushToken) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.uid = uid;
        this.pushToken = pushToken;
    }

    public UserModel(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.nickname = uid;
    }
}
