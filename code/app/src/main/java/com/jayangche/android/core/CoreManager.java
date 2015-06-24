package com.jayangche.android.core;

import com.jayangche.android.model.DiscountInfoToShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/6.
 */
public class CoreManager {

    private static List<DiscountInfoToShow> discountList = new ArrayList<DiscountInfoToShow>();


    // demo
    static {

        for (int i = 0; i < 10; i++) {
            DiscountInfoToShow discount1 = new DiscountInfoToShow();
            discount1.setName("爱温无水冷却液");
            discount1.setScope("厦门各直营店");
            discount1.setTime("2015年5月至\n2015年7月");
            discount1.setMobileNo("123456789");
            discount1.setContent("商品8折优惠，门店更换免收服务费");
            discount1.setDetial("商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费" +
                    "商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费" +
                    "商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费" +
                    "商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费" +
                    "商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费" +
                    "商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费");

            discountList.add(discount1);
        }

    }

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
