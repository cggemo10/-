package com.rrja.carja.service.impl;

import android.content.Intent;
import android.os.Binder;
import android.text.TextUtils;
import android.util.Log;

import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.maintenance.MaintenanceGoods;
import com.rrja.carja.model.maintenance.MaintenanceService;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.transaction.HttpUtils;
import com.rrja.carja.utils.ResponseUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MaintenanceBinder extends Binder {

    private static List<String> taskTag = new ArrayList<>();

    private static String TAG = "rrja.MaintenanceBinder";

    DataCenterService mContext;

    public MaintenanceBinder(DataCenterService context) {
        this.mContext = context;
    }

    /*
        101 ±£—¯
        102 Œ¨–ﬁ
        103 √¿»›
     */
    public void getService(final String serviceId) {

        if (!"101".equals(serviceId) && !"102".equals(serviceId) && !"103".equals(serviceId)
                && !"104".equals(serviceId)) {

            Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR);
            intent.putExtra("description", "Õ¯¬Á“Ï≥££¨«Î…‘∫Û‘Ÿ ‘°£");
            mContext.sendBroadcast(intent);
            return;
        }

        if (taskTag.contains(serviceId) ) {
            return;
        }

        taskTag.add(serviceId);

        Runnable task = new Runnable() {
            @Override
            public void run() {

                JSONObject serviceJs = HttpUtils.getServiceList(serviceId);
                try {
                    int code = serviceJs.getInt("code");
                    if (code == 0) {
                        List<MaintenanceService> serviceList = ResponseUtils.parseMaintenanceService(serviceJs.getJSONArray("data"));

                        if (serviceList != null && serviceList.size() > 0) {

                            CoreManager.getManager().addMaintenanceService(serviceId, serviceList);

                            Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA);
                            intent.putExtra("serviceId", serviceId);
                            mContext.sendBroadcast(intent);

                            return;
                        }
                        return;
                    } else {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR);
                        String errMsg = null;
                        if (serviceJs.has("description")) {
                            errMsg = serviceJs.getString("description");
                        }

                        if (TextUtils.isEmpty(errMsg)) {
                            errMsg = "Õ¯¬Á“Ï≥££¨«Î…‘∫Û‘Ÿ ‘°£";
                        }
                        intent.putExtra("description", errMsg);
                        mContext.sendBroadcast(intent);
                        return;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR);
                intent.putExtra("description", "Õ¯¬Á“Ï≥££¨«Î…‘∫Û‘Ÿ ‘°£");
                mContext.sendBroadcast(intent);
            }
        };

        mContext.execute(task);
    }

    public void getSubService(final String serviceId) {

        if (TextUtils.isEmpty(serviceId)) {
            Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR);
            intent.putExtra("description", "Õ¯¬Á“Ï≥££¨«Î…‘∫Û‘Ÿ ‘°£");
            mContext.sendBroadcast(intent);

            return;
        }

        if (taskTag.contains(serviceId) ) {
            return;
        }

        taskTag.add(serviceId);

        Runnable task = new Runnable() {
            @Override
            public void run() {

                JSONObject serviceJs = HttpUtils.getServiceList(serviceId);
                try {
                    int code = serviceJs.getInt("code");
                    if (code == 0) {
                        List<MaintenanceService> serviceList = ResponseUtils.parseMaintenanceService(serviceJs.getJSONArray("data"));
                        if (serviceList != null && serviceList.size() > 0) {

                            CoreManager.getManager().addMaintenanceService(serviceId, serviceList);

                            Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA);
                            intent.putExtra("serviceId", serviceId);
                            mContext.sendBroadcast(intent);

                            return;
                        }
                    } else {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR);
                        String errMsg = null;
                        if (serviceJs.has("description")) {
                            errMsg = serviceJs.getString("description");
                        }

                        if (TextUtils.isEmpty(errMsg)) {
                            errMsg = "Õ¯¬Á“Ï≥££¨«Î…‘∫Û‘Ÿ ‘°£";
                        }
                        intent.putExtra("description", errMsg);
                        mContext.sendBroadcast(intent);
                        return;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR);
                intent.putExtra("description", "Õ¯¬Á“Ï≥££¨«Î…‘∫Û‘Ÿ ‘°£");
                mContext.sendBroadcast(intent);
            }
        };

        mContext.execute(task);
    }

    public void getServiceGoods(final String serviceId, final int page) {

        if (TextUtils.isEmpty(serviceId)) {
            Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_GOODS_DATA_ERR);
            intent.putExtra("description", "Õ¯¬Á“Ï≥££¨«Î…‘∫Û‘Ÿ ‘°£");
            mContext.sendBroadcast(intent);

            return;
        }

        if (taskTag.contains(serviceId) ) {
            return;
        }

        taskTag.add(serviceId);

        Runnable task = new Runnable() {
            @Override
            public void run() {

                JSONObject goodJs = HttpUtils.getGoodList(serviceId, page);
                try {
                    int code = goodJs.getInt("code");
                    if (code == 0) {

                        List<MaintenanceGoods> goodses = ResponseUtils.parseMaintenanceGood(goodJs.getJSONArray("data"));
                        if (goodses != null && goodses.size() > 0) {
                            CoreManager.getManager().addMaintenanceGoods(serviceId + "_" + page, goodses);
                        }
                        return;
                    } else {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_GOODS_DATA_ERR);
                        String errMsg = null;
                        if (goodJs.has("description")) {
                            errMsg = goodJs.getString("description");
                        }

                        if (TextUtils.isEmpty(errMsg)) {
                            errMsg = "Õ¯¬Á“Ï≥££¨«Î…‘∫Û‘Ÿ ‘°£";
                        }
                        intent.putExtra("description", errMsg);
                        mContext.sendBroadcast(intent);
                        return;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_GOODS_DATA_ERR);
                intent.putExtra("description", "Õ¯¬Á“Ï≥££¨«Î…‘∫Û‘Ÿ ‘°£");
                mContext.sendBroadcast(intent);
            }
        };

        mContext.execute(task);
    }
}
