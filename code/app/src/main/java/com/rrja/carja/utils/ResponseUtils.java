package com.rrja.carja.utils;

import android.util.Log;

import com.rrja.carja.model.CarBrand;
import com.rrja.carja.model.CarModel;
import com.rrja.carja.model.CarSeries;
import com.rrja.carja.model.CouponGoods;
import com.rrja.carja.model.RecommendGoods;
import com.rrja.carja.model.Region;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/23.
 */
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
                        List cityList = parseAllCity(cityArray);
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
            if (carSeriesList != null) {
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
            if (carModelList != null) {
                carModelList.add(carModel);
            } else {
                return null;
            }
        }

        return carModelList;
    }

}
