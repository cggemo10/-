package com.rrja.carja.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;

import com.rrja.carja.constant.Constant;
import com.rrja.carja.model.CarStore;
import com.rrja.carja.model.Coupons;
import com.rrja.carja.model.DiscountInfo;
import com.rrja.carja.model.Forum;
import com.rrja.carja.transaction.HttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FileService extends Service implements Handler.Callback{

    public static final String ACTION_IMG_COUPONS = "rrja.coupons.img";
    public static final String ACTION_IMG_DISCOUNT = "rrja.discount.img";
    public static final String ACTION_IMG_FORUM = "rrja.forum.img";
    public static final String ACTION_IMG_STORE = "rrja.store.img";
    public static final String ACTION_APP_CLOSE = "rrja.app.close";

    private static final int WHAT_COUPONS = 10;
    private static final int WHAT_DISCOUNT = 11;
    private static final int WHAT_FORUM = 12;
    private static final int WHAT_STORE = 13;

    private static final int MSG_DOWNLOAD_SUCC = 21;
    private static final int MSG_DOWNLOAD_FAILED = 22;


    Executor executor;

    HashMap<String, Coupons> loadingCouponsMap;
    HashMap<String, DiscountInfo> loadingDiscountMap;
    HashMap<String, CarStore> loadingStoreMap;

    public FileService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        executor = Executors.newCachedThreadPool();

        loadingCouponsMap = new HashMap<>();
        loadingDiscountMap = new HashMap<>();
        loadingStoreMap = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();
        if (ACTION_IMG_COUPONS.equals(action)) {

            Bundle extras = intent.getExtras();
            Coupons coupons = extras.getParcelable("coupons");
            if (coupons != null && !TextUtils.isEmpty(coupons.getCouponId()) && !loadingCouponsMap.containsKey(coupons.getCouponId())) {
                loadingCouponsMap.put(coupons.getCouponId(), coupons);

                CouponsImgTask task = new CouponsImgTask(coupons);
                executor.execute(task);
            }
        }

        if (ACTION_IMG_DISCOUNT.equals(action)) {

            Bundle extras = intent.getExtras();
            DiscountInfo discountInfo = extras.getParcelable("discount_info");
            if (discountInfo != null && !TextUtils.isEmpty(discountInfo.getProductId()) && !loadingDiscountMap.containsKey(discountInfo.getProductId())) {
                loadingDiscountMap.put(discountInfo.getProductId(), discountInfo);

                DiscountImgTask task = new DiscountImgTask(discountInfo);
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
    public boolean handleMessage(Message msg) {

        switch (msg.what) {

            case WHAT_COUPONS:
                String key = (String) msg.obj;
                if (msg.arg1 == MSG_DOWNLOAD_SUCC) {
                    // TODO send broadcast
                }
                loadingCouponsMap.remove(key);
                break;
            case WHAT_DISCOUNT:
                String discountKey = (String) msg.obj;
                if (msg.arg1 == MSG_DOWNLOAD_SUCC) {
                    // TODO send broadcast
                }
                loadingDiscountMap.remove(discountKey);
                break;
            case WHAT_FORUM:
                String forumKey = (String) msg.obj;
                if (msg.arg1 == MSG_DOWNLOAD_SUCC) {
                    // TODO send broadcast
                }
//                loadingForumMap.remove(forumKey);
                break;
            case WHAT_STORE:
                String storeKey = (String) msg.obj;
                if (msg.arg1 == MSG_DOWNLOAD_SUCC) {
                    // TODO send broadcast
                }
                loadingStoreMap.remove(storeKey);
                break;
        }
        return false;
    }

    private static class CouponsImgTask implements Runnable {

        Coupons mCoupons;

        CouponsImgTask(Coupons coupons) {
            this.mCoupons = coupons;
        }

        @Override
        public void run() {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // TODO send message
                return;
            }
            String path = Environment.getExternalStorageDirectory().getPath() +
                    File.separatorChar + Constant.DIR_BASE + File.separator +
                    Constant.DIR_IMG_CACHE + File.separator + Constant.DIR_COUPONS + File.separator;

            String url = mCoupons.getPicUrl();

            boolean result = HttpUtils.getPicture(url, path);
            if (result) {

            } else {

            }

        }
    }

    private static class StoreImgTask implements Runnable {

        CarStore mStore;

        StoreImgTask (CarStore store) {
            this.mStore = store;
        }

        @Override
        public void run() {

        }
    }

    private static class FuromImgTask implements Runnable {

        Forum forumItem;

        FuromImgTask(Forum item) {
            this.forumItem = item;
        }

        @Override
        public void run() {

        }
    }

    private static class DiscountImgTask implements Runnable {

        DiscountInfo mDiscountInfo;

        DiscountImgTask(DiscountInfo info) {
            this.mDiscountInfo = info;
        }

        @Override
        public void run() {

        }
    }

    private void sendBroadCast() {
        Intent intent = new Intent(Action)
    }

}
