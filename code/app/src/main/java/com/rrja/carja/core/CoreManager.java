package com.rrja.carja.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.rrja.carja.activity.SplshActivity;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.model.CarBrand;
import com.rrja.carja.model.CarStore;
import com.rrja.carja.model.Coupons;
import com.rrja.carja.model.DiscountInfo;
import com.rrja.carja.model.Forum;
import com.rrja.carja.model.Region;
import com.rrja.carja.model.UserInfo;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.impl.CarBinder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/6.
 */
public class CoreManager {

    private static final String TAG = "rrja.CoreManager";

    private static List<DiscountInfo> discountList = new ArrayList<DiscountInfo>();
    private UserInfo currUser;
    private static List<ImageView> companyInfoImgs = new ArrayList<>();
    private static List<CarStore> stores = new ArrayList<>();
    private static List<Coupons> couponsList = new ArrayList<>();
    private static List<Forum> forums = new ArrayList<>();
    private static List<Region> regions = new ArrayList<>();
    private static List<CarBrand> brandList = new ArrayList<>();

    // demo
    static {

        for (int i = 0; i < 10; i++) {
            DiscountInfo discount1 = new DiscountInfo();
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

    public void setCurrUser(UserInfo currUser) {
        this.currUser = currUser;
    }

    //----------------------------------------------------------------------------------------------
    //-------------------------------------getter---------------------------------------------------
    //----------------------------------------------------------------------------------------------
    public List<ImageView> getCompanyInfo() {
        return companyInfoImgs;
    }

    public List<CarStore> getStores() {
        return stores;
    }

    public List<Coupons> getCoupons() {
        return couponsList;
    }

    public List<Forum> getForums() {
        return forums;
    }

    public List<DiscountInfo> getDiscounts() {
        return discountList;
    }

    public List<CarBrand> getCarBrand() {
        return brandList;
    }

    public UserInfo getCurrUser() {
        return currUser;
    }


    //----------------------------------------------------------------------------------------------
    //----------------------------------single instance---------------------------------------------
    //----------------------------------------------------------------------------------------------
    private static class ManagerHolder {
        private static CoreManager holder = new CoreManager();
    }

    public static CoreManager getManager() {
        return ManagerHolder.holder;
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------load data---------------------------------------------------
    //----------------------------------------------------------------------------------------------
    public void init(Activity context) {
        try {
            refreshRegions(context);
            refreshCarBrand(context);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    public void refreshRegions(Context context) throws SQLException {
        DBHelper helper = DBHelper.getInstance(context);
        if (helper != null) {


            RuntimeExceptionDao<Region, Integer> regionsDao = helper.getRegionDao();
            List<Region> queryForAll = regionsDao.queryForAll();
            if (queryForAll != null && queryForAll.size() != 0) {
                regions.clear();
                regions.addAll(queryForAll);

                Intent intent = new Intent(Constant.ACTION_BROADCAST_REFRESH_REGION);
                intent.putExtra("regions", "regions");
                context.sendBroadcast(intent);

                // TODO notify
            }
        } else {
            Log.e("rrja.CoreManager", "refreshRegions->get DBHelper failed");
        }

    }

    public void refreshCarBrand(Context context) throws SQLException {
        DBHelper helper = DBHelper.getInstance(context);
        if (helper != null) {

            RuntimeExceptionDao<CarBrand, Integer> carBrandsDao = helper.getCarBrandDao();
            List<CarBrand> queryForAll = carBrandsDao.queryForAll();
            if (queryForAll != null && queryForAll.size() != 0) {
                brandList.clear();
                brandList.addAll(queryForAll);

                // TODO notify
            }

        } else {
            Log.e("rrja.CoreManager", "refreshCarBrand->get DBHelper failed");
        }
    }
}
