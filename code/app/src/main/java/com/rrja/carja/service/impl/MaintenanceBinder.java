package com.rrja.carja.service.impl;

import android.content.Intent;
import android.os.Binder;
import android.text.TextUtils;
import android.util.Log;

import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.maintenance.MaintenanceService;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.transaction.HttpUtils;
import com.rrja.carja.utils.ResponseUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MaintenanceBinder extends Binder {

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
        Runnable task = new Runnable() {
            @Override
            public void run() {

                if (!"101".equals(serviceId) && !"102".equals(serviceId) && !"103".equals(serviceId)
                        && !"104".equals(serviceId)) {

                    Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR);
                    intent.putExtra("description", "Õ¯¬Á“Ï≥££¨«Î…‘∫Û‘Ÿ ‘°£");
                    mContext.sendBroadcast(intent);
                    return;
                }

                JSONObject serviceJs = HttpUtils.getServiceList(serviceId);
                try {
                    int code = serviceJs.getInt("code");
                    if (code == 0) {
//                        Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA);
//                        intent.putExtra("data", serviceJs.toString());
//                        mContext.sendBroadcast(intent);
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

        Runnable task = new Runnable() {
            @Override
            public void run() {

                JSONObject serviceJs = HttpUtils.getServiceList(serviceId);
                try {
                    int code = serviceJs.getInt("code");
                    if (code == 0) {
                        List<MaintenanceService> serviceList = ResponseUtils.parseMaintenanceService(serviceJs.getJSONArray("data"));
                        if (serviceList != null && serviceList.size() > 0) {
                            List<MaintenanceService> maintenanceService = CoreManager.getManager().getMaintenanceService(serviceId);
                            if (maintenanceService != null) {
                                CoreManager.getManager().addMaintenanceService(serviceId, serviceList);
                            }
                        }
//                        Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA);
//                        intent.putExtra("data", serviceJs.toString());
//                        mContext.sendBroadcast(intent);
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

    public void getServiceGoods(final String serviceId, int page) {

        if (TextUtils.isEmpty(serviceId)) {
            Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_GOODS_DATA_ERR);
            intent.putExtra("description", "Õ¯¬Á“Ï≥££¨«Î…‘∫Û‘Ÿ ‘°£");
            mContext.sendBroadcast(intent);

            return;
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {

                JSONObject serviceJs = HttpUtils.getServiceList(serviceId);
                try {
                    int code = serviceJs.getInt("code");
                    if (code == 0) {
//                        Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_GOODS_DATA);
//                        intent.putExtra("data", serviceJs.toString());
//                        mContext.sendBroadcast(intent);
                        return;
                    } else {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_GOODS_DATA_ERR);
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

                Intent intent = new Intent(Constant.ACTION_BROADCAST_MAINTENANCE_GOODS_DATA_ERR);
                intent.putExtra("description", "Õ¯¬Á“Ï≥££¨«Î…‘∫Û‘Ÿ ‘°£");
                mContext.sendBroadcast(intent);
            }
        };

        mContext.execute(task);
    }
}
