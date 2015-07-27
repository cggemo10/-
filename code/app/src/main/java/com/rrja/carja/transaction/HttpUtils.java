package com.rrja.carja.transaction;

import com.rrja.carja.model.CareInfo;
import com.rrja.carja.model.UserInfo;

import org.json.JSONObject;

/**
 * Created by chongge on 15/7/8.
 */
public class HttpUtils {

    private static final String BASE_URL = "http://120.25.201.50/api";

    private static final String SERVICE_USER = "/user";
    private static final String SERVICE_AREA = "/area";
    private static final String SERVICE_CAR = "/car";
//    http://120.25.201.50/api/car/getBrands?firstLetter=
//    http://120.25.201.50/api/car/getSeries?brandName=&brandId=1
//    http://120.25.201.50/api/car/getModels?seriesName=&seriesId=8

    private static final String INTERFACE_PROVINCE = "/getProvinceList";
    private static final String INTERFACE_CITY = "/getCityListByProvinceId";
    private static final String INTERFACE_ALL_CITY = "/getAllAreaList";

    private static final String INTERFACE_BRAND = "/getBrands";
    private static final String INTERFACE_SERIES = "/getSeries";
    private static final String INTERFACE_MODEL = "/getModels";

    private static final String INTERFACE_PREREGIST = "/preRegister";

    //-------------------------------------------------------------------------------------------------------------------
    // normal
    public static boolean getPicture(String url, String path) {

        return Network.doDownload(url, path);
    }


    //-------------------------------------------------------------------------------------------------------------------
    // user interface
    public static JSONObject checkAuth(String authToken, String phoneNo) {

        return null;
    }

    public static JSONObject getSmsCode(String phoneNo) {
        String url = String.format("%s%s%s%s%s", BASE_URL, SERVICE_USER, INTERFACE_PREREGIST, "?nattel=", phoneNo);
        return Network.doGet(url);
    }

    public static JSONObject login(String phoneNo, String smsCode) {
        return null;
    }

    public static JSONObject updateUserInfo(UserInfo userInfo) {
        return null;
    }

    public static JSONObject updateUserAvatar(UserInfo userInfo, String avatarPath) {
        return null;
    }

    public static JSONObject addPrivateCar(UserInfo userInfo, CareInfo careInfo) {
        return null;
    }

    public static JSONObject getPrivateCarList(UserInfo userInfo) {
        return null;
    }

    public static JSONObject removePrivateCar(UserInfo userInfo, CareInfo careInfo) {
        return null;
    }

    public static JSONObject getOrderList(UserInfo userInfo) {
        return null;
    }

    public static JSONObject getPrivateCoupons(UserInfo userInfo) {
        return null;
    }

    public static JSONObject getAppointmentList(UserInfo userInfo) {
        return null;
    }

    //-------------------------------------------------------------------------------------------------------------------
    // car interface

    // 获取汽车品牌
    public static JSONObject getCarBrands(String firstLetter) {

        String url = String.format("%s%s%s%s%s", BASE_URL, SERVICE_CAR, INTERFACE_BRAND, "?firstLetter=", firstLetter);
        return Network.doGet(url);
    }

    // 获取品牌车系
    public static JSONObject getCarSeries(String brandName, String brandId) {

        String url = String.format("%s%s%s%s%s%s%s", BASE_URL, SERVICE_CAR, INTERFACE_SERIES, "?brandName=",
                brandName, "&brandId=", brandId);
        return Network.doGet(url);
    }

    // 获取车系型号
    public static JSONObject getCarModels(String seriesName, String seriesId) {

        String url = String.format("%s%s%s%s%s%s%s", BASE_URL, SERVICE_CAR, INTERFACE_MODEL, "?seriesName=",
                seriesName, "&seriesId=", seriesId);
        return Network.doGet(url);
    }

    //-------------------------------------------------------------------------------------------------------------------
    // location interface

    public static JSONObject getAllCityList() {
        String url = String.format("%s%s%s", BASE_URL, SERVICE_AREA, INTERFACE_ALL_CITY);
        return Network.doGet(url);
    }

    public static JSONObject getProvinceList() {
        String url = String.format("%s%s%s", BASE_URL, SERVICE_AREA, INTERFACE_PROVINCE);
        return Network.doGet(url);
    }

    public static JSONObject getCityList(String provinceId) {
        String url = String.format("%s%s%s?provinceId=%s", BASE_URL, SERVICE_AREA, INTERFACE_CITY, provinceId);
        return Network.doGet(url);
    }

    //-------------------------------------------------------------------------------------------------------------------
    // goods interface
    public static JSONObject getRecommendProject(boolean hasCoupons) {
        return null;
    }

    public static JSONObject getServiceList(String parentServiceId) {
        return null;
    }

    public static JSONObject getGoodList(String serviceId, int count, int page) {
        return null;
    }

    public static JSONObject getCoupons() {
        return null;
    }

    //-------------------------------------------------------------------------------------------------------------------
    // order interface
    public static JSONObject commitOrder() {
        return null;
    }

    public static JSONObject orderDetail() {
        return null;
    }

    public static JSONObject syncOrderState() {
        return null;
    }

    //-------------------------------------------------------------------------------------------------------------------
    // forum interface
    public static JSONObject forumPostList() {
        return null;
    }

    public static JSONObject commitForumPost() {
        return null;
    }

    public static JSONObject replayForumPost() {
        return null;
    }

    //-------------------------------------------------------------------------------------------------------------------
    // store interface
    public static JSONObject storeList() {
        return null;
    }

    public static JSONObject commitStoreAppointment() {
        return null;
    }

    //-------------------------------------------------------------------------------------------------------------------
    // query interface
    public static JSONObject queryIllegal(CareInfo careInfo) {
        return null;
    }
}
