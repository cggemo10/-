package com.rrja.carja.utils;

import android.util.Log;

import com.rrja.carja.model.CarBrand;
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

    public static List<Region> parseAllCity(JSONArray regionArray) throws JSONException {

        if (regionArray == null || regionArray.length() == 0) {
            return null;
        }

        List<Region> regions = new ArrayList<>();

        for (int i = 0; i< regionArray.length(); i++) {

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

        for (int i = 0; i<brandArray.length(); i++) {

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
}
