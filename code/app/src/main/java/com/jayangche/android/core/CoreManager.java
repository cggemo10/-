package com.jayangche.android.core;

import com.jayangche.android.model.DiscountInfoToShow;
import com.jayangche.android.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/6.
 */
public class CoreManager {

    private static List<DiscountInfoToShow> discountList = new ArrayList<DiscountInfoToShow>();
    private UserInfo currUser;

    // demo
    static {
        DiscountInfoToShow discount1 = new DiscountInfoToShow();
//        discount1.
    }

    public List<DiscountInfoToShow> getDiscounts() {
        return discountList;
    }

    public UserInfo getCurrUser() {
        return currUser;
    }

    public void setCurrUser(UserInfo currUser) {
        currUser = currUser;
    }

    private static class ManagerHolder {
        private static CoreManager holder = new CoreManager();
    }

    public static CoreManager getManager() {
        return ManagerHolder.holder;
    }
}
