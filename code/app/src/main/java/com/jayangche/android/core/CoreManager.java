package com.jayangche.android.core;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jayangche.android.model.CarStore;
import com.jayangche.android.model.Coupons;
import com.jayangche.android.model.DiscountInfoToShow;
import com.jayangche.android.model.Forum;
import com.jayangche.android.model.UserInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/6.
 */
public class CoreManager {

    private static List<DiscountInfoToShow> discountList = new ArrayList<DiscountInfoToShow>();
    private UserInfo currUser;
    private static List<ImageView> companyInfoImgs = new ArrayList<>();
    private static List<CarStore> stores = new ArrayList<>();
    private static List<Coupons> couponsList = new ArrayList<>();
    private static List<Forum> forums = new ArrayList<>();

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

        for (int i = 0; i < 10; i++) {
            CarStore store = new CarStore();
            store.setStoreName("日日建安滨北店");
            store.setArea("厦门");
            store.setTel("0592-1234567");
            store.setOpenTime("8:00~18:00");
            store.setPayType("现金，刷卡，支付宝");
            store.setAddress("思明区金尚路禹州花园三期");
            store.setDesc("汽车维修、汽车美容、汽车装潢、汽车保险、汽车钣喷、汽车精品、24小时救援站等一站式服务的综合性的汽车服务连锁企业");

            stores.add(store);
        }

        for (int i = 0; i < 10; i++) {
            Coupons coupons = new Coupons();
            coupons.setName("爱温无水冷却液");
            coupons.setAddress("厦门各直营店");
            coupons.setTime("2015年5月至\n2015年7月");
            coupons.setTelNumber("123456789");
            coupons.setContent("商品8折优惠，门店更换免收服务费");
            coupons.setDetal("商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费" +
                    "商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费" +
                    "商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费" +
                    "商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费" +
                    "商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费" +
                    "商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费商品8折优惠，门店更换免收服务费");

            couponsList.add(coupons);
        }

        for (int i = 0; i < 10; i++) {
            Forum forum = new Forum();
            forums.add(forum);
        }

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

    // company info
    public void initCompanyInfo(final Context context) {
        for (int i = 0; i < 3; i++) {
            ImageView ad = new ImageView(context);
            ad.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams adParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ad.setLayoutParams(adParams);
            try {
                ad.setImageBitmap(BitmapFactory.decodeStream(context.getAssets().open("banner.jpg")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "TODO 显示公司简介", Toast.LENGTH_LONG).show();
                }
            });

            companyInfoImgs.add(ad);
        }

    }

    public List<ImageView> getCompanyInfo() {
        return companyInfoImgs;
    }

    public List<CarStore> getStores() {
        return stores;
    }

    public List<Coupons> getCoupons() {
        return couponsList;
    }

    public  List<Forum> getForums() {
        return forums;
    }

    // single instance
    private static class ManagerHolder {
        private static CoreManager holder = new CoreManager();
    }

    public static CoreManager getManager() {
        return ManagerHolder.holder;
    }
}
