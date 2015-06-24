package com.jayangche.android.core;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jayangche.android.model.DiscountInfoToShow;
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



    // single instance
    private static class ManagerHolder {
        private static CoreManager holder = new CoreManager();
    }

    public static CoreManager getManager() {
        return ManagerHolder.holder;
    }
}
