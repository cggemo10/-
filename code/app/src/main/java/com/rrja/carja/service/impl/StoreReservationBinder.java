package com.rrja.carja.service.impl;

import android.content.Intent;
import android.os.Binder;
import android.text.TextUtils;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarStore;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.transaction.HttpUtils;
import com.rrja.carja.utils.ResponseUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Administrator on 2015/9/17.
 */
public class StoreReservationBinder extends Binder {

    private DataCenterService context;

    public StoreReservationBinder(DataCenterService service) {
        this.context = service;
    }

    public void loadStore(int page) {

        Runnable task = new Runnable() {
            @Override
            public void run() {

                String area = "";
                if (CoreManager.getManager().getCurrUser() == null || TextUtils.isEmpty(CoreManager.getManager().getCurrUser().getTel())) {
                    return;
                }

                if (CoreManager.getManager().getCostumerRegion() != null) {
                    area = CoreManager.getManager().getCostumerRegion().getName();
                }

                try {
                    JSONObject js = HttpUtils.storeList(CoreManager.getManager().getCurrUser(), area);
                    if (js.has("code") && js.getInt("code") == 0) {
                        JSONArray data = js.getJSONArray("data");
                        List<CarStore> stores = ResponseUtils.parseCatStores(data);
                        if (stores != null && stores.size() != 0) {
                            CoreManager.getManager().getStores().clear();
                            CoreManager.getManager().getStores().addAll(stores);
                        }

                        Intent intent = new Intent(Constant.ACTION_BORADCAST_GET_STORE_LIST);
                        intent.putExtra("description", "Ok");
                        context.sendBroadcast(intent);

                    } else {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_STORE_LIST_ERROR);
                        String errMsg = context.getString(R.string.str_err_net);
                        if (js.has("description")) {
                            errMsg = js.getString("description");
                        }
                        intent.putExtra("description", errMsg);
                        context.sendBroadcast(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_STORE_LIST_ERROR);
                    String errMsg = context.getString(R.string.str_err_net);
                    intent.putExtra("description", errMsg);

                    context.sendBroadcast(intent);
                }

            }
        };

        context.execute(task);
    }

    public void commitBookStore(final String storeId, final String dataTime) {



        Runnable task = new Runnable() {
            @Override
            public void run() {
                String tel = CoreManager.getManager().getCurrUser().getTel();
                String authToken = CoreManager.getManager().getCurrUser().getAuthToken();
                try {
                    final String data = URLEncoder.encode(dataTime, "utf-8").replace(" ", "%20");
                    JSONObject js = HttpUtils.commitStoreAppointment(tel, authToken, storeId, data);
                    if (js.has("code") && js.getInt("code") == 0) {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_BOOK_STORE);
                        intent.putExtra("result", "succ");
                        context.sendBroadcast(intent);
                    } else {
                        String errMsg = context.getString(R.string.str_err_net);
                        if (js.has("description")) {
                            errMsg = js.getString("description");
                        }
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_BOOK_STORE_ERR);
                        intent.putExtra("description", errMsg);
                        context.sendBroadcast(intent);
                    }
                } catch (Exception e) {
                    String errMsg = context.getString(R.string.str_err_net);
                    Intent intent = new Intent(Constant.ACTION_BROADCAST_BOOK_STORE_ERR);
                    intent.putExtra("description", errMsg);
                    context.sendBroadcast(intent);

                    e.printStackTrace();
                }
            }
        };

        context.execute(task);
    }
}
