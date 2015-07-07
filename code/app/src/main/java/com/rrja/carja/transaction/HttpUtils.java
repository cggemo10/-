package com.rrja.carja.transaction;

import android.location.Address;
import android.location.Geocoder;

import com.rrja.carja.model.CareInfo;
import com.rrja.carja.model.UserInfo;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by chongge on 15/7/8.
 */
public class HttpUtils {


    //-------------------------------------------------------------------------------------------------------------------
    // user interface
    public static JSONObject checkAuth(String authToken, String phoneNo) {

        return null;
    }

    public static JSONObject getSmsCode(String phoneNo) {
        return null;
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
        return null;
    }

    // 获取品牌车系
    public static JSONObject getCarSeries(String brandName, String brandId) {
        return null;
    }

    // 获取车系型号
    public static JSONObject getCarModels(String seriesName, String seriesId) {
        return null;
    }

    //-------------------------------------------------------------------------------------------------------------------
    // location interface
    public static JSONObject getProvinceList() {
        return null;
    }

    public static JSONObject getCityList(String provinceId) {
        return null;
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
