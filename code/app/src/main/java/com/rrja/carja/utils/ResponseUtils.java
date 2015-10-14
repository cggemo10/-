package com.rrja.carja.utils;

import android.util.Log;

import com.rrja.carja.model.CarBrand;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.CarModel;
import com.rrja.carja.model.CarSeries;
import com.rrja.carja.model.CarStore;
import com.rrja.carja.model.CouponGoods;
import com.rrja.carja.model.RecommendGoods;
import com.rrja.carja.model.Region;
import com.rrja.carja.model.ViolationRecord;
import com.rrja.carja.model.maintenance.MaintenanceGoods;
import com.rrja.carja.model.maintenance.MaintenanceService;
import com.rrja.carja.model.myorder.OrderRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ResponseUtils {

    public static List<RecommendGoods> parseDiscountList(JSONArray discountArray) throws JSONException {

        if (discountArray == null || discountArray.length() == 0) {
            return null;
        }

        List<RecommendGoods> infoList = new ArrayList<>();

        for (int i = 0; i < discountArray.length(); i++) {

            JSONObject discountJson = discountArray.getJSONObject(i);
            RecommendGoods info = RecommendGoods.parse(discountJson);
            if (info != null) {
                infoList.add(info);
            }
        }

        if (infoList.size() == 0) {
            return null;
        } else {
            return infoList;
        }

    }


    public static List<CouponGoods> parseCouponsList(JSONArray goodJson) throws JSONException {
        if (goodJson == null || goodJson.length() == 0) {
            return null;
        }

        List<CouponGoods> goodsList = new ArrayList<>();

        for (int i = 0; i < goodJson.length(); i++) {

            JSONObject goodJ = goodJson.getJSONObject(i);
            CouponGoods goods = CouponGoods.parse(goodJ);
            if (goodJ != null) {
                goodsList.add(goods);
            }
        }

        return goodsList;

    }

    public static List<Region> parseAllCity(JSONArray regionArray) throws JSONException {

        if (regionArray == null || regionArray.length() == 0) {
            return null;
        }

        List<Region> regions = new ArrayList<>();

        for (int i = 0; i < regionArray.length(); i++) {

            JSONObject proviceJson = regionArray.getJSONObject(i);
            Region region = Region.parse(proviceJson);
            regions.add(region);

            if (proviceJson.has("citys")) {

                try {
                    JSONArray cityArray = proviceJson.getJSONArray("citys");
                    if (cityArray.length() != 0) {
                        List<Region> cityList = parseAllCity(cityArray);
                        if (cityList != null) {
                            regions.addAll(cityList);
                        } else {
                            return null;
                        }
                    }

                } catch (Exception e) {
                    Log.e("rrja.ResponseUtils", "parseAllCity" + proviceJson.toString(), e);
                    return null;
                }

            }
        }

        if (regions.size() != 0) {
            return regions;
        } else {
            return null;
        }
    }

    public static List<CarBrand> parseCarBrand(JSONArray brandArray) throws JSONException {

        if (brandArray == null || brandArray.length() == 0) {
            return null;
        }

        List<CarBrand> carBrands = new ArrayList<>();

        for (int i = 0; i < brandArray.length(); i++) {

            JSONObject brandJson = brandArray.getJSONObject(i);
            CarBrand brand = CarBrand.parse(brandJson);
            if (brand != null) {
                carBrands.add(brand);
            } else {
                return null;
            }
        }

        return carBrands;
    }

    public static List<CarSeries> parseCarSeriesList(JSONArray carSeriesJson) throws JSONException {
        if (carSeriesJson == null || carSeriesJson.length() == 0) {
            return null;
        }

        List<CarSeries> carSeriesList = new ArrayList<>();

        for (int i = 0; i < carSeriesJson.length(); i++) {

            JSONObject seriesJson = carSeriesJson.getJSONObject(i);
            CarSeries carSeries = CarSeries.parse(seriesJson);
            if (carSeries != null) {
                carSeriesList.add(carSeries);
            } else {
                return null;
            }
        }

        return carSeriesList;
    }

    public static List<CarModel> parseCarModelList(JSONArray carModelJson) throws JSONException {
        if (carModelJson == null || carModelJson.length() == 0) {
            return null;
        }

        List<CarModel> carModelList = new ArrayList<>();

        for (int i = 0; i < carModelJson.length(); i++) {

            JSONObject modelJson = carModelJson.getJSONObject(i);
            CarModel carModel = CarModel.parse(modelJson);
            if (carModel != null) {
                carModelList.add(carModel);
            } else {
                return null;
            }
        }

        return carModelList;
    }

    public static List<MaintenanceService> parseMaintenanceService (JSONArray jsonArray) throws JSONException {

        if (jsonArray == null || jsonArray.length() == 0) {
            return null;
        }

        List<MaintenanceService> serviceList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject serviceJson = jsonArray.getJSONObject(i);
            MaintenanceService service = MaintenanceService.parse(serviceJson);
            if (service != null) {
                serviceList.add(service);
            } else {
                return null;
            }
        }

        return serviceList;
    }

    public static List<MaintenanceGoods> parseMaintenanceGood (JSONArray jsonArray) throws JSONException {

        if (jsonArray == null || jsonArray.length() == 0) {
            return null;
        }

        List<MaintenanceGoods> goodsList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject goodsJson = jsonArray.getJSONObject(i);
            MaintenanceGoods goods = MaintenanceGoods.parse(goodsJson);
            if (goods != null) {
                goodsList.add(goods);
            } else {
                return null;
            }

        }

        return goodsList;

    }

    public static List<CarInfo> parseCarInfo(JSONArray jsonArray) throws JSONException {

        if (jsonArray == null) {
            return null;
        }

        if (jsonArray.length() == 0) {
            return new ArrayList<>();
        }

        ArrayList<CarInfo> carList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject carJson = jsonArray.getJSONObject(i);
            CarInfo car = CarInfo.parse(carJson);
            if (car != null) {
                carList.add(car);
            }
        }

        return carList;
    }

    public static List<CarStore> parseCatStores(JSONArray jsonArray) throws JSONException{

        if (jsonArray == null) {
            return null;
        }

        if (jsonArray.length() == 0) {
            return new ArrayList<>();
        }

        ArrayList<CarStore> storeList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject storeJson = jsonArray.getJSONObject(i);
            CarStore store = CarStore.parse(storeJson);
            if (store != null) {
                storeList.add(store);
            }
        }
        return storeList;
    }

    public static List<ViolationRecord> parseViolation (JSONArray jsonArray) throws JSONException {
        if (jsonArray == null) {
            return null;
        }

        if (jsonArray.length() == 0) {
            return new ArrayList<>();
        }

        ArrayList<ViolationRecord> records = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject recordJson = jsonArray.getJSONObject(i);
            ViolationRecord record = ViolationRecord.parse(recordJson);
            if (record != null) {
                records.add(record);
            }
        }
        return records;
    }

    public static List<OrderRecord> parseOrderRecord (JSONArray jsonArray) throws JSONException {
        if (jsonArray == null) {
            return null;
        }

        if (jsonArray.length() == 0) {
            return new ArrayList<>();
        }

        ArrayList<OrderRecord> records = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject recordJson = jsonArray.getJSONObject(i);
            OrderRecord record = OrderRecord.parse(recordJson);
            if (record != null) {
                records.add(record);
            }
        }
        return records;
    }
}
