package com.rrja.carja.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.core.DBHelper;
import com.rrja.carja.model.CarBrand;
import com.rrja.carja.model.Region;
import com.rrja.carja.service.impl.CarBinder;
import com.rrja.carja.service.impl.ForumBinder;
import com.rrja.carja.service.impl.ProductBinder;
import com.rrja.carja.service.impl.UserBinder;
import com.rrja.carja.transaction.HttpUtils;
import com.rrja.carja.utils.ResponseUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DataCenterService extends Service implements Handler.Callback {

    private static final int MSG_REFRESH_CITYS = 12;
    private static final int MSG_REFRESH_CAR_BRAND = 13;

    Executor executor;
    Handler mHandler;

    public DataCenterService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mHandler = new Handler(this);
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();
        if (Constant.ACTION_INIT_SERVICE.equals(action)) {
            checkConstantData();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void checkConstantData() {
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        long expiredDate = currentTimeMillis - 30 * 24 * 3600;


        SharedPreferences sp = getSharedPreferences(Constant.SP_CONSTANT_TIME, MODE_PRIVATE);
        long cityUpdateTime = sp.getLong("city_UpdateTime", 0);
        if (cityUpdateTime - expiredDate <= 0) {
            // TODO need update
            loadCityData();
        }

        long carBrandUpdateTime = sp.getLong("carBrand_UpdateTime", 0);
        if (carBrandUpdateTime - expiredDate <= 0) {
            // TODO need update
            loadCarBrandData();
        }

    }



    @Override
    public IBinder onBind(Intent intent) {

        String action = intent.getAction();
        if (Constant.ACTION_USER_SERVICE.equals(action)) {
            return new UserBinder(this);
        }
        if (Constant.ACTION_CAR_SERVICE.equals(action)) {
            return new CarBinder(this);
        }
        if (Constant.ACTION_FORUM_SERVICE.equals(action)) {
            return new ForumBinder(this);
        }
        if (Constant.ACTION_PRODUCT_SERVICE.equals(action)) {
            return new ProductBinder(this);
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void loadCityData() {

        Runnable task = new Runnable() {
            @Override
            public void run() {

                JSONObject allCityList = HttpUtils.getAllCityList();
                try {
                    int code = allCityList.getInt("code");
                    if (code == 0) {
                        JSONArray array = allCityList.getJSONArray("data");
                        List<Region> regions = ResponseUtils.parseAllCity(array);
                        if (regions != null || regions.size() != 0) {

                            Message msg = mHandler.obtainMessage(MSG_REFRESH_CITYS);
                            msg.obj = regions;
                            msg.sendToTarget();
                        }
                    }
                } catch (Exception e) {
                    // TODO nothing
                }
            }
        };

        executor.execute(task);
    }

    private void loadCarBrandData() {

        Runnable task = new Runnable() {
            @Override
            public void run() {

                JSONObject carBrandJson = HttpUtils.getCarBrands("");
                try {
                    int code = carBrandJson.getInt("code");
                    if (code == 0) {
                        JSONArray array = carBrandJson.getJSONArray("data");
                        List<CarBrand> carBrands = ResponseUtils.parseCarBrand(array);
                        if (carBrands != null && carBrands.size() != 0) {

                            Message msg = mHandler.obtainMessage(MSG_REFRESH_CAR_BRAND);
                            msg.obj = carBrands;
                            msg.sendToTarget();
                        }
                    }
                } catch (Exception e) {
                    // TODO nothing
                }
            }
        };

        executor.execute(task);
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case MSG_REFRESH_CITYS:

                try {
                    List<Region> regions = (List<Region>) msg.obj;
                    DBHelper helper = DBHelper.getInstance(DataCenterService.this);

                    TableUtils.clearTable(helper.getConnectionSource(), Region.class);

                    Dao<Region, Integer> regionDao = helper.getRegionDao();
                    regionDao.create(regions);

                    CoreManager.getManager().refreshRegions(DataCenterService.this);
                    sendReceiver(Constant.ACTION_BROADCAST_REFRESH_REGION);
                } catch (Exception e) {
                    sendReceiver(Constant.ACTION_BROADCAST_REFRESH_REGION_ERROR);
                }
                break;
            case MSG_REFRESH_CAR_BRAND:

                try {
                    List<CarBrand> brands = (List<CarBrand>) msg.obj;
                    DBHelper helper = DBHelper.getInstance(DataCenterService.this);

                    TableUtils.clearTable(helper.getConnectionSource(), CarBrand.class);

                    Dao<CarBrand, Integer> carBrandIntegerDao = helper.getCarBrandDao();
                    carBrandIntegerDao.create(brands);

                    CoreManager.getManager().refreshCarBrand(DataCenterService.this);
                    sendReceiver(Constant.ACTION_BROADCAST_REFRESH_CARBRAND);
                } catch (Exception e) {
                    sendReceiver(Constant.ACTION_BROADCAST_REFRESH_CARBRAND_ERROR);
                }
                break;
        }
        return false;
    }

    private void sendReceiver(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
}
