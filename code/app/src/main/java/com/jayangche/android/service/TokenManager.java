package com.jayangche.android.service;

import com.jayangche.android.model.UserInfo;

/**
 * Created by chongge on 15/5/31.
 */
public class TokenManager {

    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    private static class Holder {
        private static TokenManager manager = new TokenManager();
    }
    public static TokenManager getInsatnce() {
        return  Holder.manager;
    }
}
