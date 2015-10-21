package com.rrja.carja.transaction;

import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.UserInfo;

import org.apache.http.util.TextUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HttpUtils {

    private static final String BASE_URL = "http://120.25.201.50/api";

    private static final String SERVICE_USER = "/user";
    private static final String SERVICE_AREA = "/area";
    private static final String SERVICE_CAR = "/car";
    private static final String SERVICE_GOODS = "/goods";
    private static final String SERVICE_SERVICE = "/service";
    private static final String SERVICE_ORDER = "/order";
    private static final String SERVICE_STORE = "/store";
    private static final String SERVICE_TRAFFIC = "/traffic";

    private static final String INTERFACE_PROVINCE = "/getProvinceList";
    private static final String INTERFACE_CITY = "/getCityListByProvinceId";
    private static final String INTERFACE_ALL_CITY = "/getAllAreaList";
    private static final String INTERFACE_ILLEGAL = "/queryIllegal";
    private static final String INTERFACE_BRAND = "/getBrands";
    private static final String INTERFACE_SERIES = "/getSeries";
    private static final String INTERFACE_MODEL = "/getModels";
    private static final String INTERFACE_ADD_CAR = "/addUserCar";

    private static final String INTERFACE_CHECK_AUTH = "/login";
    private static final String INTERFACE_PREREGIST = "/preRegister";
    private static final String INTERFACE_REGIST_OR_LOGIN = "/register";
    private static final String INTERFACE_USER_INFO_UPDATE = "/updateUserInfo";
    private static final String INTERFACE_USRE_AVATAR_UPDATE = "/updateAvatar";
    private static final String INTERFACE_USER_CARS = "/getUserCarList";
    private static final String INTERFACE_USER_DEL_CAR = "/deleteUserCar";
    private static final String INTERFACE_USER_ORDER_LIST = "/getUserOrderList";
    private static final String INTERFACE_USER_COUPON_LIST = "/getUserCoupons";
    private static final String INTERFACE_USER_APPOINTMENT = "/getUserAppointmentList";
    private static final String INTERFACE_USER_APPOINTMENT_STORE = "/appointmentStore";

    private static final String INTERFACE_GOOD_RECOMMEND = "/getRecommendGoods";
    private static final String INTERFACE_GOOD_COUPONS = "/getDiscountGoodsList";
    private static final String INTERFACE_SERVICES = "/getServiceList";
    private static final String INTERFACE_SERVICE_GOODS = "/getGoodsList";

    private static final String INTERFACE_ORDER = "/order";
    private static final String INTERFACE_SYNCORDER = "/syncOrderStatus";
    private static final String INTERFACE_QUERYORDER = "/queryOrderInfo";

    private static final String INTERFACE_STORE = "/getStoreList";

    private static final String INTERFACE_FEEDBACK = "/feedback";


    //-------------------------------------------------------------------------------------------------------------------
    // normal
    public static boolean getPicture(String url, String path) {

        return Network.doDownload(url, path);
    }


    //-------------------------------------------------------------------------------------------------------------------
    // user interface
    public static JSONObject checkAuth(String authToken, String phoneNo) {

        String url = String.format("%s%s%s%s%s%s%s", BASE_URL, SERVICE_USER, INTERFACE_CHECK_AUTH, "?nattel=", phoneNo, "&authToken=", authToken);
        return Network.doGet(url);
    }

    public static JSONObject getSmsCode(String phoneNo) {
        String url = String.format("%s%s%s%s%s", BASE_URL, SERVICE_USER, INTERFACE_PREREGIST, "?nattel=", phoneNo);
        return Network.doGet(url);
    }

    public static JSONObject login(String phoneNo, String smsCode) {
        String url = String.format("%s%s%s%s%s%s%s", BASE_URL, SERVICE_USER, INTERFACE_REGIST_OR_LOGIN, "?nattel=", phoneNo,
                "&nickname=&email=&address=&captcha=", smsCode);
        return Network.doGet(url);
    }

    /*
      更新用户信息： http://120.25.201.50/api/user/updateUserInfo?nattel=xxx&authToken=xxx&nickname=xxx&email=xxx&address=xxx
      更新用户头像： http://120.25.201.50/api/user/updateAvatar?nattel=xxx&authToken=xxx&avatar(post头像文件)
     */

    public static JSONObject updateUserInfo(UserInfo userInfo) {
//        try {
//            String nickNameEnc = URLEncoder.encode(userInfo.getNikeName(), "UTF-8");
//            String url =
//            return Network.doGet()
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    public static JSONObject updateUserAvatar(UserInfo userInfo, File avatarFile) {

        String url = BASE_URL + SERVICE_USER + INTERFACE_USRE_AVATAR_UPDATE;
        List<TextKeyValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new TextKeyValuePair("nattel", userInfo.getTel()));
        valuePairs.add(new TextKeyValuePair("authToken", userInfo.getAuthToken()));

        List<FileKeyValuePair> filePairs = new ArrayList<>();
        filePairs.add(new FileKeyValuePair("avatar", avatarFile.getName(), avatarFile.getAbsolutePath()));

        return Network.doPost(url, valuePairs, filePairs);
    }

    public static JSONObject addPrivateCar(UserInfo userInfo, CarInfo carInfo) {
        try {
            String palteNum = TextUtils.isEmpty(carInfo.getPlatNum()) ? "" : URLEncoder.encode(carInfo.getPlatNum(), "UTF-8");
            String engineNo = TextUtils.isEmpty(carInfo.getEngineNo()) ? "" : carInfo.getEngineNo();
            String frameNo = TextUtils.isEmpty(carInfo.getFrameNo6()) ? "" : carInfo.getFrameNo6();
            String brandName = TextUtils.isEmpty(carInfo.getBrandName()) ? "" : URLEncoder.encode(carInfo.getBrandName(), "UTF-8");
            String modelName = TextUtils.isEmpty(carInfo.getModelName()) ? "" : URLEncoder.encode(carInfo.getModelName(), "UTF-8");
            String seriesName = TextUtils.isEmpty(carInfo.getSeriesName()) ? "" : URLEncoder.encode(carInfo.getSeriesName(), "UTF-8");
            String cityId = CoreManager.getManager().getCostumerRegion() == null ?
                    "" : (CoreManager.getManager().getCostumerRegion().getId() + "");
            URLDecoder.decode("xxx", "UTF-8");

            String url = BASE_URL + SERVICE_USER + INTERFACE_ADD_CAR + "?nattel=" + userInfo.getTel() +
                    "&authToken=" + userInfo.getAuthToken() +
                    "&carBrandId=" + carInfo.getBrandId() +
                    "&carBrandName=" + brandName +
                    "&carSeriesId=" + carInfo.getSeriesId() +
                    "&carSeriesName=" + seriesName +
                    "&carModelId=" + carInfo.getModelId() +
                    "&carModelName=" + modelName +
                    "&plateNumber=" + palteNum + "&engineno=" + engineNo +
                    "&frameno=" + frameNo + "&citycode=" + cityId;
            return Network.doGet(url);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }

    }


    public static JSONObject getPrivateCarList(UserInfo userInfo) {

        String url = String.format("%s%s%s%s%s%s%s", BASE_URL, SERVICE_USER, INTERFACE_USER_CARS, "?nattel=", userInfo.getTel(), "&authToken=", userInfo.getAuthToken());
        return Network.doGet(url);
    }

    public static JSONObject removePrivateCar(UserInfo userInfo, CarInfo carInfo) {

        String url = BASE_URL + SERVICE_USER + INTERFACE_USER_DEL_CAR + "?nattel=" + userInfo.getTel() + "&authToken=" + userInfo.getAuthToken() + "&carId=" + carInfo.getId();
        return Network.doGet(url);
    }

    public static JSONObject getOrderList(UserInfo userInfo, String type) {

        String url = BASE_URL + SERVICE_USER + INTERFACE_USER_ORDER_LIST + "?nattel=" + userInfo.getTel() + "&authToken=" + userInfo.getAuthToken() + "&type=" + type;
        return Network.doGet(url);
    }

    public static JSONObject getPrivateCoupons(UserInfo userInfo) {

        String url = BASE_URL + SERVICE_USER + INTERFACE_USER_COUPON_LIST + "?nattel=" + userInfo.getTel() + "&authToken=" + userInfo.getAuthToken();
        return Network.doGet(url);
    }

    //获取用户预约列表
    public static JSONObject getAppointmentList(UserInfo userInfo) {

        String url = BASE_URL + SERVICE_USER + INTERFACE_USER_APPOINTMENT + "?nattel=" + userInfo.getTel() + "&authToken=" + userInfo.getAuthToken();
        return Network.doGet(url);
    }

    public static JSONObject getAppointmentByStoreId(UserInfo info, String storeId) {

        return null;//Network.doGet(url);
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
    public static JSONObject getRecommendGoods(int page) {
        int number = 15;
        String url = String.format("%s%s%s%s%d%s%d", BASE_URL, SERVICE_GOODS, INTERFACE_GOOD_RECOMMEND, "?count=", number, "&page=", page);
        return Network.doGet(url);
    }

    public static JSONObject getCouponsGoods(int page) {
        int number = 15;
        String url = String.format("%s%s%s%s%d%s%d", BASE_URL, SERVICE_GOODS, INTERFACE_GOOD_COUPONS, "?number=", number, "&page=", page);
        return Network.doGet(url);
    }

    /*
        保养下服务
        101 保养
        102 维修
        103 美容
        104 洗车
     */
    public static JSONObject getServiceList(String parentServiceId) {

        String url = String.format("%s%s%s%s%s", BASE_URL, SERVICE_SERVICE, INTERFACE_SERVICES, "?fatherId=", parentServiceId);
        return Network.doGet(url);
    }

    /*
        保养服务下商品列表
     */
    public static JSONObject getGoodList(String serviceId, int page) {

        int count = 15;
        String url = BASE_URL + SERVICE_GOODS + INTERFACE_SERVICE_GOODS + "?serviceId=" + serviceId + "&count=" + count + "&page=" + page;
        return Network.doGet(url);
    }


    //-------------------------------------------------------------------------------------------------------------------
    // order interface
    public static JSONObject commitOrder(String nattel, String authToken, String contacts,
                                         String phone, String plateNum, String serviceLocation,
                                         String serviceDateTime, String isNeedInvoice, String invoiceTitle,
                                         String receiver, String receiverAddr, String orderDetails) {

        String url = BASE_URL + SERVICE_ORDER + INTERFACE_ORDER;

        List<TextKeyValuePair> pairs = new ArrayList<>();
        pairs.add(new TextKeyValuePair("nattel", nattel));
        pairs.add(new TextKeyValuePair("authToken", authToken));
        pairs.add(new TextKeyValuePair("contacts", contacts));
        pairs.add(new TextKeyValuePair("phone", phone));
        pairs.add(new TextKeyValuePair("plateNum", plateNum));
        pairs.add(new TextKeyValuePair("serviceLocation", serviceLocation));
        pairs.add(new TextKeyValuePair("serviceDateTime", serviceDateTime));
        pairs.add(new TextKeyValuePair("isNeedInvoice", isNeedInvoice));
        pairs.add(new TextKeyValuePair("invoiceTitle", invoiceTitle));
        pairs.add(new TextKeyValuePair("receiver", receiver));
        pairs.add(new TextKeyValuePair("receiverAddress", receiverAddr));
        pairs.add(new TextKeyValuePair("orderDetails", orderDetails));

        return Network.doPost(url, pairs, null);
    }

    public static JSONObject orderDetail(String tel, String authToken, String orderId) {

        String url = BASE_URL + SERVICE_ORDER + INTERFACE_QUERYORDER + "?nattel=" + tel + "&authToken=" + authToken + "&orderNum=" + orderId;
        return Network.doGet(url);
    }

    //http://120.25.201.50/api/order/syncOrderStatus?nattel=xxx&authToken=xxx&orderNum=xxx&payChannel=xxx&payNumber=xxx&status=xx
    public static JSONObject syncOrderState(UserInfo userInfo, String tradNm, String state) {
        String payChannel = "";
        try {
            payChannel = URLEncoder.encode("支付宝", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = BASE_URL + SERVICE_ORDER + INTERFACE_SYNCORDER + "?nattel=" + userInfo.getTel() + "&authToken=" + userInfo.getAuthToken()
                + "&orderNum=" + tradNm + "&payChannel=" + payChannel + "&payNumber=" + "0" +
                "&status=" + state;
        return Network.doGet(url);
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
    // store interface http://120.25.201.50/api/store/getStoreList?nattel=sunde&authToken=123456&area=xxx
    public static JSONObject storeList(UserInfo userInfo, String area) {

        String url = BASE_URL + SERVICE_STORE + INTERFACE_STORE + "?nattel=" + userInfo.getTel() + "&authToken=" + userInfo.getAuthToken();
        return Network.doGet(url);
    }

    public static JSONObject commitStoreAppointment(String tel, String authToken, String storeId, String data) {
        String url = BASE_URL + SERVICE_USER + INTERFACE_USER_APPOINTMENT_STORE + "?nattel=" + tel + "&authToken=" + authToken +
                "&storeId=" + storeId + "&datetime=" + data;
        return Network.doGet(url);
    }

    //-------------------------------------------------------------------------------------------------------------------
    // query interface
    public static JSONObject queryIllegal(String carId) {

        String url = BASE_URL + SERVICE_TRAFFIC + INTERFACE_ILLEGAL + "?nattel=" + CoreManager.getManager().getCurrUser().getTel() +
                "?authToken=" + CoreManager.getManager().getCurrUser().getAuthToken() + "?carId=" + carId;
        return Network.doGet(url);
    }

    //-------------------------------------------------------------------------------------------------------------------
    // feedback
    public static JSONObject commitFeedback(String tel, String authToken, String title, String content) {

        String url = BASE_URL + SERVICE_USER + INTERFACE_FEEDBACK;

        List<TextKeyValuePair> pairs = new ArrayList<>();
        pairs.add(new TextKeyValuePair("nattel", tel));
        pairs.add(new TextKeyValuePair("authToken", authToken));
        pairs.add(new TextKeyValuePair("subject", title));
        pairs.add(new TextKeyValuePair("message", content));

        return Network.doPost(url, pairs, null);
    }
}
