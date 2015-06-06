package com.jayangche.android.core;

import com.jayangche.android.model.DiscountInfoToShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/6.
 */
public class CoreManager {

    private static List<DiscountInfoToShow> discountList = new ArrayList<DiscountInfoToShow>();

    public List<DiscountInfoToShow> getDiscounts() {
        return discountList;
    }


    private static class ManagerHolder {
        private static CoreManager holder = new CoreManager();
    }

    public static CoreManager getManager() {
        return ManagerHolder.holder;
    }
}
