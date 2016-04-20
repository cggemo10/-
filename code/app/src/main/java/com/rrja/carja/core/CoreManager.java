package com.rrja.carja.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.model.CarBrand;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.CarModel;
import com.rrja.carja.model.CarSeries;
import com.rrja.carja.model.CarStore;
import com.rrja.carja.model.coupons.CouponGoods;
import com.rrja.carja.model.Forum;
import com.rrja.carja.model.Region;
import com.rrja.carja.model.UserInfo;
import com.rrja.carja.model.coupons.RecommendGoods;
import com.rrja.carja.model.coupons.UserCoupons;
import com.rrja.carja.model.maintenance.MaintenanceGoods;
import com.rrja.carja.model.maintenance.MaintenanceService;
import com.rrja.carja.model.myorder.OrderRecord;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class CoreManager {

    private static final String TAG = "rrja.CoreManager";

    private static List<RecommendGoods> discountList = new ArrayList<RecommendGoods>();

    private static List<ImageView> companyInfoImgs = new ArrayList<>();

    private static int storeCurPage = 0;
    private static List<CarStore> stores = new ArrayList<>();
    private static List<CouponGoods> couponsList = new ArrayList<>();
//    private static List<Forum> forums = new ArrayList<>();

    private static List<Region> regions = new ArrayList<>();
    private static HashMap<String, Region> regionsMap = new HashMap<>();

    private static List<CarBrand> brandList = new ArrayList<>();
    private static HashMap<String, List<CarSeries>> carSeriesMap = new HashMap<>();
    private static HashMap<String, List<CarModel>> carModelMap = new HashMap<>();

    private static HashMap<String, List<OrderRecord>> myOrderMap = new HashMap<>();

    private static List<CarInfo> userCars = new ArrayList<>();

    private static HashMap<String, List<UserCoupons>> userCouponsMap = new HashMap<>();
    private static HashMap<String, UserCoupons> unUsedCoupons = new HashMap<>();

    private Region gpsRegion;
    private Region customRegion;
    private boolean isCustomeChange;

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

    public void setUserCars(List<CarInfo> cars) {

        if (cars == null || cars.size() == 0) {
            return;
        }

        userCars.clear();
        userCars.addAll(cars);
    }

    public void clearUserCars() {
        userCars.clear();
    }

    public void setCostumerRegion(Region region) {
        this.customRegion = region;
    }

    public Region getCostumerRegion() {
        return customRegion;
    }

    public Region getGpsRegion() {
        return gpsRegion;
    }

    public void setGpsRegion(Region region) {
        gpsRegion = region;
    }

    public boolean isCustomeChange() {
        return isCustomeChange;
    }

    public void setCustomeChange (boolean isCustomeChange) {
        this.isCustomeChange = isCustomeChange;
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

    public int getStorePage() {
        return storeCurPage;
    }

    public void setStorePage(int storePage) {
        storeCurPage = storePage;
    }

    public List<CouponGoods> getCoupons() {
        return couponsList;
    }

//    public List<Forum> getForums() {
//        return forums;
//    }

    public List<RecommendGoods> getDiscounts() {
        return discountList;
    }

    public List<CarBrand> getCarBrand() {
        return brandList;
    }

    public List<Region> getRegions() { return regions; }

    public HashMap<String, Region> getRegionsMap() {
        return regionsMap;
    }

    public Region getRegionByName(String name) {
        return regionsMap.get(name);
    }

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

    public List<CarInfo> getUserCars() {
        return userCars;
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

    //goodKey  serviceId_page
    public List<MaintenanceGoods> getMaintenanceGoods(String goodsKey) {
        if (maintenanceGoodsMap.containsKey(goodsKey)) {
            return maintenanceGoodsMap.get(goodsKey);
        } else {
            return new ArrayList<>();
        }
    }

    //goodKey  serviceId_page
    public void addMaintenanceGoods(String goodsKey, List<MaintenanceGoods> goodsList) {
        if (goodsList == null || goodsList.size() == 0) {
            return;
        }

        if (maintenanceGoodsMap.containsKey(goodsKey)) {
            List<MaintenanceGoods> goodses = maintenanceGoodsMap.get(goodsKey);
            goodses.addAll(goodsList);
        } else {
            maintenanceGoodsMap.put(goodsKey, goodsList);
        }
    }

    public void clearMaintenanceGoods() {
        Set<String> keySet = maintenanceGoodsMap.keySet();
        for (String key : keySet) {
            List<MaintenanceGoods> remove = maintenanceGoodsMap.remove(key);
            if (remove != null && remove.size() != 0) {
                remove.clear();
            }
        }
    }

    public MaintenanceService getOrderCosmetologyService() {
        return null;
    }

    public MaintenanceService getOrderRepairService() {
        return null;
    }

    public MaintenanceService getOrderMaintenanceService() {
        return null;
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
                regionsMap.clear();

                regions.addAll(queryForAll);
                for (Region region : regions) {
                    regionsMap.put(region.getName(), region);
                }

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

    //----------------------------------------------------------------------------------------------
    //----------------------------------my order----------------------------------------------------
    //----------------------------------------------------------------------------------------------

    // 11 22 33
    public List<OrderRecord> getMyOrders(String key) {
        if (!myOrderMap.containsKey(key)) {
            List<OrderRecord> records = new ArrayList<>();
            myOrderMap.put(key, records);
        }
        return myOrderMap.get(key);
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------my coupons--------------------------------------------------
    //----------------------------------------------------------------------------------------------
    // my coupons
    public List<UserCoupons> getUserCouponsByStatus(String status) {
        if (userCouponsMap.containsKey(status)) {
            return userCouponsMap.get(status);
        } else {
            ArrayList<UserCoupons> userCouponses = new ArrayList<>();
            userCouponsMap.put(status, userCouponses);
            return userCouponses;
        }
    }

    public UserCoupons usedCouponsByGoodsId(String goodsId) {
        if (unUsedCoupons.containsKey(goodsId)) {
            return unUsedCoupons.remove(goodsId);
        } else {
            return null;
        }
    }

    public void addUnusedCouponsWithGoodsId(String goodsId, UserCoupons coupons) {
        if (TextUtils.isEmpty(goodsId) || coupons == null) {
            return;
        }
        if (unUsedCoupons.containsKey(goodsId)) {
            unUsedCoupons.remove(goodsId);
        }

        unUsedCoupons.put(goodsId, coupons);
    }
}
