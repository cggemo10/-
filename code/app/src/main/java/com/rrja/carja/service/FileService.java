package com.rrja.carja.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcelable;
import android.text.TextUtils;

import com.rrja.carja.model.CarStore;
import com.rrja.carja.model.Coupons;
import com.rrja.carja.model.DiscountInfo;
import com.rrja.carja.model.Forum;
import com.rrja.carja.transaction.HttpUtils;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FileService extends Service {

    public static final String ACTION_IMG_COUPONS = "rrja.coupons.img";
    public static final String ACTION_IMG_DISCOUNT = "rrja.discount.img";
    public static final String ACTION_IMG_FORUM = "rrja.forum.img";
    public static final String ACTION_IMG_STORE = "rrja.store.img";


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

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private static class CouponsImgTask implements Runnable {

        Coupons mCoupons;

        CouponsImgTask(Coupons coupons) {
            this.mCoupons = coupons;
        }

        @Override
        public void run() {

            String url = mCoupons.getPicUrl();
            HttpUtils.getPicture()

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

}
