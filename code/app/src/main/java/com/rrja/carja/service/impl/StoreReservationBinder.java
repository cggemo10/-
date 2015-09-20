package com.rrja.carja.service.impl;

import android.content.Intent;
import android.os.Binder;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarStore;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.transaction.HttpUtils;
import com.rrja.carja.utils.ResponseUtils;

import org.json.JSONArray;
import org.json.JSONObject;

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
}
