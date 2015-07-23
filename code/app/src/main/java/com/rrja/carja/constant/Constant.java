package com.rrja.carja.constant;

import android.os.Environment;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by Administrator on 2015/6/26.
 */
public class Constant {

    public static final String SP_CONSTANT_TIME = "constant_data";

    public static final String ACTION_LOGIN = "LOGIN";
    public static final String ACTION_LOGIN_MOBILE_VERIFY = "MOBILE_VERIFY";
    public static final String ACTION_MODIFY_NICK_NAME = "MODIFY_NICK_NAME";

    public static final String ACTION_INIT_SERVICE = "rrja.request.init";
    public static final String ACTION_USER_SERVICE = "rrja.request.user.service";
    public static final String ACTION_CAR_SERVICE = "rrja.request.car.service";
    public static final String ACTION_FORUM_SERVICE = "rrja.request.forum.service";
    public static final String ACTION_PRODUCT_SERVICE = "rrja.request.product.service";

    public static final String ACTION_BROADCAST_DOWNLOAD_IMG_COUPONS = "rrja.broadcast.download.COUPONS_IMG";
    public static final String ACTION_BROADCAST_DOWNLOAD_IMG_DISCOUNT = "rrja.broadcast.download.DISCOUNT_IMG";
    public static final String ACTION_BROADCAST_DOWNLOAD_IMG_FORUM = "rrja.broadcast.download.FORUM_IMG";
    public static final String ACTION_BROADCAST_DOWNLOAD_IMG_STORE = "rrja.broadcast.download.STORE_IMG";

    public static final String ACTION_BROADCAST_REFRESH_REGION = "rrja.boradcast.refresh.REGIONS";
    public static final String ACTION_BROADCAST_REFRESH_REGION_ERROR = "rrja.boradcast.refresh.REGIONS_ERROR";
    public static final String ACTION_BROADCAST_REFRESH_CARBRAND = "rrja.broadcast.refresh.CARBRAND";
    public static final String ACTION_BROADCAST_REFRESH_CARBRAND_ERROR = "rrja.broadcast.refresh.CARBRAND_ERROR";

    public static final String DIR_BASE = "rrja";
    public static final String DIR_IMG_CACHE = "cacheImg";
    public static final String DIR_DISCOUNT = "discount";
    public static final String DIR_COUPONS = "coupons";
    public static final String DIR_FORUM = "forum";
    public static final String DIR_STORE = "store";

    public static String getDiscountCacheDir() {
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + DIR_BASE + File.separator + DIR_IMG_CACHE +
                    File.separator + DIR_DISCOUNT + File.separator;
        }

        return path;
    }

    public static String getCouponsCacheDir() {
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + DIR_BASE + File.separator + DIR_IMG_CACHE +
                    File.separator + DIR_COUPONS + File.separator;
        }

        return path;
    }

    public static String getStoreCacheDir() {
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + DIR_BASE + File.separator + DIR_IMG_CACHE +
                    File.separator + DIR_STORE + File.separator;
        }

        return path;
    }

    public static String getFuromCacheDir() {
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + DIR_BASE + File.separator + DIR_IMG_CACHE +
                    File.separator + DIR_FORUM + File.separator;
        }

        return path;
    }
}
