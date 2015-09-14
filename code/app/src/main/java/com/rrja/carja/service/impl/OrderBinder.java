package com.rrja.carja.service.impl;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.text.TextUtils;

import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.UserInfo;
import com.rrja.carja.model.maintenance.MaintenanceOrder;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.transaction.HttpUtils;

import org.json.JSONObject;

public class OrderBinder extends Binder {

    private DataCenterService context;

    public OrderBinder (DataCenterService ctx) {
        this.context = ctx;
    }

    public void commitOrder(MaintenanceOrder order, final Bundle data) {

        if (CoreManager.getManager().getCurrUser() == null) {
            // TODO
            return;
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {

                UserInfo user = CoreManager.getManager().getCurrUser();
                String nattel = user.getTel();
                String token = user.getAuthToken();
                String contacts = data.getString("contacts");
                String phone = data.getString("phone");
                String plateNum = data.getString("plate_num");
                String serviceLocation = data.getString("server_location");
                String serveiceDateTime = data.getString("server_date");
                boolean isNeedInvoice = data.getBoolean("need_invoice");
                String invoiceTitle = data.getString("invoice_title");
                String receiver = data.getString("receiver");
                String receiverAddress = data.getString("receiver_addr");
                String orderDetails = data.getString("order_details");

                JSONObject orderJs = HttpUtils.commitOrder(nattel, token, contacts, phone, plateNum, serviceLocation, serveiceDateTime,
                        isNeedInvoice+"", invoiceTitle, receiver, receiverAddress, orderDetails);
                if (orderJs.has("code")) {
                    try {
                        int code = orderJs.getInt("code");
                        if (code == 0) {
                            String orderNum = orderJs.getString("orderNum");

                            Intent intent = new Intent(Constant.ACTION_BROADCAST_ORDER_SUCC);
                            intent.putExtra("orderNum", orderNum);
                            context.sendBroadcast(intent);
                        } else {
                            String errMsg = null;
                            if (orderJs.has("description")) {
                                errMsg = orderJs.getString("description");
                            }
                            if (TextUtils.isEmpty(errMsg)) {
                                errMsg = "网络异常，请稍后再试。";
                            }
                            Intent intent = new Intent(Constant.ACTION_BROADCAST_ORDER_ERR);
                            intent.putExtra("description", errMsg);
                            context.sendBroadcast(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        String errMsg = "网络异常，请稍后再试。";
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_ORDER_ERR);
                        intent.putExtra("description", errMsg);
                        context.sendBroadcast(intent);
                    }
                }
            }
        };

        context.execute(task);
    }
}
