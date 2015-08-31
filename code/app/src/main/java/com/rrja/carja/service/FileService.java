package com.rrja.carja.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.rrja.carja.constant.Constant;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.CarStore;
import com.rrja.carja.model.CouponGoods;
import com.rrja.carja.model.RecommendGoods;
import com.rrja.carja.model.Forum;
import com.rrja.carja.transaction.HttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FileService extends Service implements Handler.Callback {

    public static final String ACTION_IMG_CAR_LOGO = "rrja.carlogo.img";
    public static final String ACTION_IMG_COUPONS = "rrja.coupons.img";
    public static final String ACTION_IMG_DISCOUNT = "rrja.discount.img";
    public static final String ACTION_IMG_FORUM = "rrja.forum.img";
    public static final String ACTION_IMG_STORE = "rrja.store.img";
    public static final String ACTION_APP_CLOSE = "rrja.app.close";

    private static final int WHAT_COUPONS = 10;
    private static final int WHAT_DISCOUNT = 11;
    private static final int WHAT_FORUM = 12;
    private static final int WHAT_STORE = 13;
    private static final int WHAT_CARINFO = 14;

    private static final int MSG_DOWNLOAD_SUCC = 21;
    private static final int MSG_DOWNLOAD_FAILED = 22;

    Handler mHandler;
    Executor executor;

    HashMap<String, CouponGoods> loadingCouponsMap;
    HashMap<String, RecommendGoods> loadingDiscountMap;
    HashMap<String, CarStore> loadingStoreMap;
    HashMap<String, CarInfo> loadingCarMap;

    private RecycleBoradcastReceiver recycleReceiver;

    public FileService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        executor = Executors.newCachedThreadPool();

        mHandler = new Handler(this);


        loadingCouponsMap = new HashMap<>();
        loadingDiscountMap = new HashMap<>();
        loadingStoreMap = new HashMap<>();
        loadingCarMap = new HashMap<>();

        registRecycleReceiver();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();
        if (ACTION_IMG_COUPONS.equals(action)) {

            Bundle extras = intent.getExtras();
            CouponGoods coupons = extras.getParcelable("coupons_goods");
            if (coupons != null && !TextUtils.isEmpty(coupons.getCouponId()) && !loadingCouponsMap.containsKey(coupons.getCouponId())) {
                loadingCouponsMap.put(coupons.getCouponId(), coupons);

                CouponsImgTask task = new CouponsImgTask(coupons);
                executor.execute(task);
            }
        }

        if (ACTION_IMG_DISCOUNT.equals(action)) {

            Bundle extras = intent.getExtras();
            RecommendGoods discountInfo = extras.getParcelable("recommend_info");
            if (discountInfo != null && !TextUtils.isEmpty(discountInfo.getProductId()) && !loadingDiscountMap.containsKey(discountInfo.getProductId())) {
                loadingDiscountMap.put(discountInfo.getProductId(), discountInfo);

                RecommendImgTask task = new RecommendImgTask(discountInfo);
                executor.execute(task);
            }

        }

        if (ACTION_IMG_FORUM.equals(action)) {

            Bundle extras = intent.getExtras();
            Forum forum = extras.getParcelable("forum");
//            if (!load)

        }

        if (ACTION_IMG_STORE.equals(action)) {

            Bundle extras = intent.getExtras();
            CarStore carStore = extras.getParcelable("car_store");
            if (carStore != null && !TextUtils.isEmpty(carStore.getStoreId()) && !loadingStoreMap.containsKey(carStore.getStoreId())) {
                loadingStoreMap.put(carStore.getStoreId(), carStore);

                StoreImgTask task = new StoreImgTask(carStore);
                executor.execute(task);
            }

        }

        if (ACTION_IMG_CAR_LOGO.equals(action)) {
            Bundle extras = intent.getExtras();
            CarInfo carInfo = extras.getParcelable("car");
            if (carInfo != null && !TextUtils.isEmpty(carInfo.getId()) && !loadingCarMap.containsKey(carInfo.getId())) {
                loadingCarMap.put(carInfo.getId(), carInfo);

                CarImgTask task = new CarImgTask(carInfo);
                executor.execute(task);
            }
        }

        if (ACTION_APP_CLOSE.equals(action)) {
            loadingCouponsMap.clear();
            loadingDiscountMap.clear();
            loadingStoreMap.clear();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        unRegistRecycleReceiver();
        super.onDestroy();
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {

            case WHAT_COUPONS:
                String key = (String) msg.obj;
                if (msg.arg1 == MSG_DOWNLOAD_SUCC) {
                    sendBroadCast(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_COUPONS);
                }
                loadingCouponsMap.remove(key);
                break;
            case WHAT_DISCOUNT:
                String discountKey = (String) msg.obj;
                if (msg.arg1 == MSG_DOWNLOAD_SUCC) {
                    sendBroadCast(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_DISCOUNT);
                }
                loadingDiscountMap.remove(discountKey);
                break;
            case WHAT_FORUM:
                String forumKey = (String) msg.obj;
                if (msg.arg1 == MSG_DOWNLOAD_SUCC) {
                    sendBroadCast(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_FORUM);
                }
//                loadingForumMap.remove(forumKey);
                break;
            case WHAT_STORE:
                String storeKey = (String) msg.obj;
                if (msg.arg1 == MSG_DOWNLOAD_SUCC) {
                    sendBroadCast(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_STORE);
                }
                loadingStoreMap.remove(storeKey);
                break;
            case WHAT_CARINFO:
                String carKey = (String) msg.obj;
                if (msg.arg1 == MSG_DOWNLOAD_SUCC) {
                    sendBroadCast(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_STORE);
                }
                loadingStoreMap.remove(carKey);

        }
        return false;
    }

    private class CouponsImgTask implements Runnable {

        CouponGoods mCoupons;

        CouponsImgTask(CouponGoods coupons) {
            this.mCoupons = coupons;
        }

        @Override
        public void run() {
            Message msg = mHandler.obtainMessage(WHAT_COUPONS);
            msg.obj = mCoupons.getCouponId();
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                msg.arg1 = MSG_DOWNLOAD_FAILED;
            } else {
                String path = Environment.getExternalStorageDirectory().getPath() +
                        File.separatorChar + Constant.DIR_BASE + File.separator +
                        Constant.DIR_IMG_CACHE + File.separator + Constant.DIR_COUPONS + File.separator;

                String url = mCoupons.getPicUrl();

                boolean result = HttpUtils.getPicture(url, path);

                if (result) {
                    msg.arg1 = MSG_DOWNLOAD_SUCC;
                } else {
                    msg.arg1 = MSG_DOWNLOAD_FAILED;
                }
            }
            msg.sendToTarget();
        }
    }

    private class StoreImgTask implements Runnable {

        CarStore mStore;

        StoreImgTask(CarStore store) {
            this.mStore = store;
        }

        @Override
        public void run() {
            Message msg = mHandler.obtainMessage(WHAT_STORE);
            msg.obj = mStore.getStoreId();
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                msg.arg1 = MSG_DOWNLOAD_FAILED;
            } else {
                String path = Environment.getExternalStorageDirectory().getPath() +
                        File.separatorChar + Constant.DIR_BASE + File.separator +
                        Constant.DIR_IMG_CACHE + File.separator + Constant.DIR_STORE + File.separator;

                String url = mStore.getStoreImg();

                boolean result = HttpUtils.getPicture(url, path);

                if (result) {
                    msg.arg1 = MSG_DOWNLOAD_SUCC;
                } else {
                    msg.arg1 = MSG_DOWNLOAD_FAILED;
                }
            }
            msg.sendToTarget();
        }
    }

    private class FuromImgTask implements Runnable {

        Forum forumItem;

        FuromImgTask(Forum item) {
            this.forumItem = item;
        }

        @Override
        public void run() {

        }
    }

    private class RecommendImgTask implements Runnable {

                       RecommendGoods mDiscountInfo;

                       RecommendImgTask(RecommendGoods info) {
                           this.mDiscountInfo = info;
                       }

                       @Override
                       public void run() {

                           Message msg = mHandler.obtainMessage(WHAT_DISCOUNT);
                           msg.obj = mDiscountInfo.getProductId();
                           if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                               msg.arg1 = MSG_DOWNLOAD_FAILED;
                           } else {
                               String path = Environment.getExternalStorageDirectory().getPath() +
                                       File.separatorChar + Constant.DIR_BASE + File.separator +
                                       Constant.DIR_IMG_CACHE + File.separator + Constant.DIR_RECOMMEND + File.separator;

                               String url = mDiscountInfo.getImgUrl();

                               boolean result = HttpUtils.getPicture(url, path);

                               if (result) {
                                   msg.arg1 = MSG_DOWNLOAD_SUCC;
                               } else {
                                   msg.arg1 = MSG_DOWNLOAD_FAILED;
                               }
                           }
                           msg.sendToTarget();

                       }
                   }

    private class CarImgTask implements Runnable {

        CarInfo carInfo;

        CarImgTask(CarInfo info) {
            this.carInfo = info;
        }

        @Override
        public void run() {

            Message msg = mHandler.obtainMessage(WHAT_CARINFO);
            msg.obj = carInfo.getId();
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                msg.arg1 = MSG_DOWNLOAD_FAILED;
            } else {
                String path = Environment.getExternalStorageDirectory().getPath() +
                        File.separatorChar + Constant.DIR_BASE + File.separator +
                        Constant.DIR_IMG_CACHE + File.separator + Constant.DIR_CAR_LOGO + File.separator;

                String url = carInfo.getCarImg();

                boolean result = HttpUtils.getPicture(url, path);

                if (result) {
                    msg.arg1 = MSG_DOWNLOAD_SUCC;
                } else {
                    msg.arg1 = MSG_DOWNLOAD_FAILED;
                }
            }
            msg.sendToTarget();

        }
    }


    private void sendBroadCast(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void registRecycleReceiver() {

        if (recycleReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_STORE);
            filter.addAction(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_FORUM);
            filter.addAction(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_DISCOUNT);
            filter.addAction(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_COUPONS);

            recycleReceiver = new RecycleBoradcastReceiver();
            registerReceiver(recycleReceiver, filter);
        }
    }

    private void unRegistRecycleReceiver() {
        if (recycleReceiver != null) {
            unregisterReceiver(recycleReceiver);
            recycleReceiver = null;
        }
    }

    private class RecycleBoradcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // do nothing
        }
    }

}
