package com.rrja.carja.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.model.CarBrand;
import com.rrja.carja.model.CarModel;
import com.rrja.carja.model.CarSeries;
import com.rrja.carja.model.CarStore;
import com.rrja.carja.model.CouponGoods;
import com.rrja.carja.model.RecommendGoods;
import com.rrja.carja.model.Forum;
import com.rrja.carja.model.Region;
import com.rrja.carja.model.UserInfo;
import com.rrja.carja.model.maintenance.MaintenanceGoods;
import com.rrja.carja.model.maintenance.MaintenanceService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/6/6.
 */
public class CoreManager {

    private static final String TAG = "rrja.CoreManager";

    private static List<RecommendGoods> discountList = new ArrayList<RecommendGoods>();

    private static List<ImageView> companyInfoImgs = new ArrayList<>();
    private static List<CarStore> stores = new ArrayList<>();
    private static List<CouponGoods> couponsList = new ArrayList<>();
    private static List<Forum> forums = new ArrayList<>();
    private static List<Region> regions = new ArrayList<>();
    private static List<CarBrand> brandList = new ArrayList<>();
    private static HashMap<String, List<CarSeries>> carSeriesMap = new HashMap<>();
    private static HashMap<String, List<CarModel>> carModelMap = new HashMap<>();

    private Region customRegion;
    private UserInfo currUser;


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

    public void setCostumerRegion(Region region) {
        this.customRegion = region;
    }

    public Region getCostumerRegion() {
        return customRegion;
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

    public List<CouponGoods> getCoupons() {
        return couponsList;
    }

    public List<Forum> getForums() {
        return forums;
    }

    public List<RecommendGoods> getDiscounts() {
        return discountList;
    }

    public List<CarBrand> getCarBrand() {
        return brandList;
    }

    public List<Region> getRegions() { return regions; }

    public List<CarSeries> getCarSeriesByBrandId(String brandId) {
        if (carSeriesMap.containsKey(brandId)) {
            return carSeriesMap.get(brandId);
        } else {
            return new ArrayList<>();
        }
    }

    public void setCarSeriesData(String key, List<CarSeries> seriesList) {

        if (seriesList == null) {
            return;
        }

        if (carSeriesMap == null) {
            carSeriesMap = new HashMap<>();
        }

        if (carSeriesMap.containsKey(key)) {
            carSeriesMap.remove(key);
        }

        carSeriesMap.put(key, seriesList);
    }

    public List<CarModel> getCarModelsBySeriesId(String seriesId) {
        if (carModelMap.containsKey(seriesId)) {
            return carModelMap.get(seriesId);
        } else {
            return new ArrayList<>();
        }
    }

    public void setCarModelData(String key, List<CarModel> modelList) {
        if (modelList == null) {
            return;
        }

        if (carModelMap == null) {
            carModelMap = new HashMap<>();
        }

        if (carModelMap.containsKey(key)) {
            carModelMap.remove(key);
        }

        carModelMap.put(key, modelList);
    }

    public UserInfo getCurrUser() {
        return currUser;
    }

    //----------------------------------------------------------------------------------------------
    private HashMap<String, List<MaintenanceService>> maintenanceServiceMap = new HashMap<>();
    public List<MaintenanceService> getMaintenanceService(String serviceId) {
        if (maintenanceServiceMap.containsKey(serviceId)) {
            return maintenanceServiceMap.get(serviceId);
        } else {
            return new ArrayList<>();
        }
    }

    public void addMaintenanceService(String serviceId, List<MaintenanceService> serviceList) {
        if (serviceList == null || serviceList.size() == 0) {
            return;
        }
        if (maintenanceServiceMap.containsKey(serviceId)) {
            List<MaintenanceService> services = maintenanceServiceMap.get(serviceId);
            services.addAll(serviceList);
        } else {
            maintenanceServiceMap.put(serviceId, serviceList);
        }
    }

    private HashMap<String, List<MaintenanceGoods>> maintenanceGoodsMap = new HashMap<>();

    public List<MaintenanceGoods> getMaintenanceGoods(String serviceId) {
        if (maintenanceGoodsMap.containsKey(serviceId)) {
            return maintenanceGoodsMap.get(serviceId);
        } else {
            return new ArrayList<>();
        }
    }

    public void addMaintenanceGoods(String serviceId, List<MaintenanceGoods> goodsList) {
        if (goodsList == null || goodsList.size() == 0) {
            return;
        }

        if (maintenanceGoodsMap.containsKey(serviceId)) {
            List<MaintenanceGoods> goodses = maintenanceGoodsMap.get(serviceId);
            goodses.addAll(goodsList);
        } else {
            maintenanceGoodsMap.put(serviceId, goodsList);
        }
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
