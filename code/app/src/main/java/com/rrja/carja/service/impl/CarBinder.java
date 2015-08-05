package com.rrja.carja.service.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.text.TextUtils;
import android.util.Log;

import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.CarModel;
import com.rrja.carja.model.CarSeries;
import com.rrja.carja.model.UserInfo;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.transaction.HttpUtils;
import com.rrja.carja.utils.ResponseUtils;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;


public class CarBinder extends Binder{

    private static final String TAG = "rrja.CarBinder";

    DataCenterService mContext;

    public CarBinder(DataCenterService context) {
        this.mContext = context;
    }

    public void getSeriesByBrandId(final String brandId) {

        if (TextUtils.isEmpty(brandId)) {
            Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_CAR_SERIES_ERR);
            intent.putExtra("brand_id", brandId);
            mContext.sendBroadcast(intent);
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject carSeriesJson = HttpUtils.getCarSeries("", brandId);
                    int code = carSeriesJson.getInt("code");
                    if (code == 0) {

                        List<CarSeries> seriesList =  ResponseUtils.parseCarSeriesList(carSeriesJson.getJSONArray("data"));
                        if (seriesList != null) {
                            CoreManager.getManager().setCarSeriesData(brandId, seriesList);
                            // TODO save auth
                            Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_CAR_SERIES);
                            intent.putExtra("brand_id", brandId);
                            mContext.sendBroadcast(intent);
                            return;
                        }

                    } else {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_CAR_SERIES_ERR);
                        intent.putExtra("brand_id", brandId);
                        String errMsg = null;
                        if (carSeriesJson.has("description")) {
                            errMsg = carSeriesJson.getString("description");
                        }

                        if (TextUtils.isEmpty(errMsg)) {
                            errMsg = "网络异常，请稍后再试。";
                        }
                        intent.putExtra("description", errMsg);
                        mContext.sendBroadcast(intent);
                        return;
                    }

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_CAR_SERIES_ERR);
                intent.putExtra("brand_id", brandId);
                mContext.sendBroadcast(intent);


            }
        };

        mContext.execute(task);

    }

    public void getModelBySeriesId(final String seriesId) {

        Runnable task = new Runnable() {
            @Override
            public void run() {

                try {
                    JSONObject seriesJson = HttpUtils.getCarModels("", seriesId);
                    int code = seriesJson.getInt("code");
                    if (code == 0) {

                        List<CarModel> seriesList =  ResponseUtils.parseCarModelList(seriesJson.getJSONArray("data"));
                        if (seriesList != null) {
                            CoreManager.getManager().setCarModelData(seriesId, seriesList);
                            // TODO save auth
                            Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_CAR_MODEL);
                            intent.putExtra("series_id", seriesId);
                            mContext.sendBroadcast(intent);
                            return;
                        }

                    } else {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_CAR_MODEL_ERR);
                        intent.putExtra("series_id", seriesId);
                        String errMsg = null;
                        if (seriesJson.has("description")) {
                            errMsg = seriesJson.getString("description");
                        }

                        if (TextUtils.isEmpty(errMsg)) {
                            errMsg = "网络异常，请稍后再试。";
                        }
                        intent.putExtra("description", errMsg);
                        mContext.sendBroadcast(intent);
                        return;
                    }

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_CAR_MODEL_ERR);
                intent.putExtra("series_id", seriesId);
                mContext.sendBroadcast(intent);


            }
        };

        mContext.execute(task);

    }

    public void addCarForUser(final UserInfo userInfo, final CarInfo carInfo, final String carNum) {

        if (userInfo == null || carInfo == null || carInfo.isDataEmpty() || TextUtils.isEmpty(carNum)) {
            // TODO
            return;
        }

        Runnable task = new Runnable() {

            @Override
            public void run() {

                HttpUtils.addPrivateCar(userInfo, carInfo);
            }
        };

        mContext.execute(task);

    }

}
