package com.rrja.carja.constant;

import android.os.Environment;

import java.io.File;

public class Constant {

    public static final String SP_CONSTANT_TIME = "constant_data";

    public static final String ACTION_LOGIN_BY_AUTH = "rrja.LOGIN_AUTH";
    public static final String ACTION_LOGIN_BY_AUTH_ERROR = "rrja.LOGIN_AUTH_ERROR";

    public static final String ACTION_LOGIN_BY_PHONE = "rrja.LOGIN_PHONE";
    public static final String ACTION_LOGIN_BY_PHONE_ERROR = "rrja.LOGIN_PHONE_ERROR";

    public static final String ACTION_LOGIN_MOBILE_SMS = "rrja.MOBILE_SMS";
    public static final String ACTION_LOGIN_MOBILE_SMS_ERROR = "rrja.MOBILE_SMS_ERROR";

    public static final String ACTION_MODIFY_NICK_NAME = "rrja.MODIFY_NICK_NAME";
    public static final String ACTION_MODIFY_NICK_NAME_ERROR = "rrja.MODIFY_NICK_NAME_ERROR";

    public static final String ACTION_INIT_SERVICE = "rrja.request.init";
    public static final String ACTION_USER_SERVICE = "rrja.request.user.service";
    public static final String ACTION_STORE_RESERVATION_SERVICE = "rrja.request.store.reservation";
    public static final String ACTION_CAR_SERVICE = "rrja.request.car.service";
    public static final String ACTION_FORUM_SERVICE = "rrja.request.forum.service";
    public static final String ACTION_MAINTENANCE_SERVICE = "rrja.request.maintenance.service";
    public static final String ACTION_ORDER_SERVICE = "rrja.ORDER_SERVICE";

    public static final String ACTION_BROADCAST_DOWNLOAD_IMG_COUPONS = "rrja.broadcast.download.COUPONS_IMG";
    public static final String ACTION_BROADCAST_DOWNLOAD_IMG_DISCOUNT = "rrja.broadcast.download.DISCOUNT_IMG";
    public static final String ACTION_BROADCAST_DOWNLOAD_IMG_FORUM = "rrja.broadcast.download.FORUM_IMG";
    public static final String ACTION_BROADCAST_DOWNLOAD_IMG_STORE = "rrja.broadcast.download.STORE_IMG";
    public static final String ACTION_BROADCAST_DOWNLOAD_IMG_CARLOGO = "rrja.broadcast.download.CAR_LOGO";

    public static final String ACTION_BROADCAST_REFRESH_REGION = "rrja.boradcast.refresh.REGIONS";
    public static final String ACTION_BROADCAST_REFRESH_REGION_ERROR = "rrja.boradcast.refresh.REGIONS_ERROR";
    public static final String ACTION_BROADCAST_REFRESH_CARBRAND = "rrja.broadcast.refresh.CARBRAND";
    public static final String ACTION_BROADCAST_REFRESH_CARBRAND_ERROR = "rrja.broadcast.refresh.CARBRAND_ERROR";

    public static final String ACTION_BORADCAST_GET_STORE_LIST = "rrja.boradcast.get.STORE";
    public static final String ACTION_BROADCAST_GET_STORE_LIST_ERROR = "rrja.boradcast.get.STORE_ERR";

    public static final String ACTION_BROADCAST_BOOK_STORE = "rrja.broadcast.BOOK_STORE";
    public static final String ACTION_BROADCAST_BOOK_STORE_ERR = "rrja.broadcast.BOOK_STORE_ERR";

    public static final String ACTION_BROADCAST_GET_CAR_SERIES = "rrja.broadcast.get.CAR_SERIES";
    public static final String ACTION_BROADCAST_GET_CAR_SERIES_ERR = "rrja.broadcast.get.CAR_SERIES_ERR";

    public static final String ACTION_BROADCAST_GET_CAR_MODEL = "rrja.broadcast.get.CAR_MODEL";
    public static final String ACTION_BROADCAST_GET_CAR_MODEL_ERR = "rrja.broadcast.get.CAR_MODEL_ERR";

    public static final String ACTION_BROADCAST_ADD_CAR = "rrja.broadcast.add.CAR";
    public static final String ACTION_BROADCAST_ADD_CAR_ERR = "rrja.broadcast.add.CAR_ERR";

    public static final String ACTION_BROADCAST_GET_RECOMMEND_DATA = "rrja.broadcast.get.RECOMMEND_DATA";
    public static final String ACTION_BROADCAST_GET_RECOMMEND_DATA_ERR = "rrja.broadcast.get.RECOMMEND_DATA_ERR";

    public static final String ACTION_BROADCAST_GET_COUPONS_DATA = "rrja.broadcast.get.COUPONS_DATA";
    public static final String ACTION_BROADCAST_GET_COUPONS_DATA_ERR = "rrja.broadcast.get.COUPONS_DATA_ERR";

    public static final String ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA = "rrja.broadcast.MAINTENANCE_SERVICE_DATA";
    public static final String ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR = "rrja.broadcast.MAINTENANCE_SERVICE_DATA_ERR";

    public static final String ACTION_BROADCAST_MAINTENANCE_GOODS_DATA = "rrja.broadcast.MAINTENANCE_GOODS_DATA";
    public static final String ACTION_BROADCAST_MAINTENANCE_GOODS_DATA_ERR = "rrja.broadcast.MAINTENANCE_GOODS_DATA_ERR";

    public static final String ACTION_BROADCAST_ORDER_SUCC = "rrja.broadcast.ORDER_SUCC";
    public static final String ACTION_BROADCAST_ORDER_ERR = "rrja.broadcast.ORDER_ERR";

    public static final String ACTION_DATA_GET_RECOMMEND = "rrja.data.goods.RECOMMEND";
    public static final String ACTION_DATA_GET_COUPONS_GOODS = "rrja.data.goods.COUPONS";

    public static final String ACTION_BROADCAST_GET_USER_CARS = "rrja.data.USER_CAR";
    public static final String ACTION_BROADCAST_GET_USER_CARS_ERR = "rrja.data.USER_CAR_ERR";

    public static final String ACTION_BROADCAST_SYNC_ORDER = "rrja.data.SYNC_ORDER";
    public static final String ACTION_BROADCAST_SYNC_ORDER_ERR = "rrja.data.SYNC_ORDER_ERR";

    public static final String ACTION_BROADCAST_VIOLATION = "rrja.data.VIOLATION";
    public static final String ACTION_BROADCAST_VIOLATION_ERR = "rrja.data.VIOLATION_ERR";

    public static final String ACTION_GAIN_COUPONS = "rrja.gain_COUPONS";

    public static final String ACTION_REQUEST_REFRESH_USER_CAR = "rrja.action.REFRESH_USERCAR";
    public static final String ACTION_REQUEST_VIOLATION = "rrja.request_QUERY_ILLEGAL";

    public static final String DIR_BASE = "rrja";
    public static final String DIR_IMG_CACHE = "cacheImg";
    public static final String DIR_RECOMMEND = "recommend";
    public static final String DIR_COUPONS = "coupons";
    public static final String DIR_FORUM = "forum";
    public static final String DIR_STORE = "store";
    public static final String DIR_CAR_LOGO = "carLogo";

    public static final String ACTION_LOGIN_AFTER_HOMEMAINTENANCE = "rrja.login_HOMEMAINTENANCE";
    public static final String ACTION_LOGIN_AFTER_ONDOREWASH = "rrja.login_ONDOREWASH";
    public static final String ACTION_LOGIN_AFTER_VIOLATION = "rrja.login_VIOLATION";

    public static String getRecommendCacheDir() {
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + DIR_BASE + File.separator + DIR_IMG_CACHE +
                    File.separator + DIR_RECOMMEND + File.separator;
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

    public static String getCarImageCacheDir() {
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + DIR_BASE + File.separator + DIR_IMG_CACHE +
                    File.separator + DIR_CAR_LOGO + File.separator;
        }

        return path;
    }
}
