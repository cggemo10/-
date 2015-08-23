package com.rrja.carja.model.maintenance;

import android.text.TextUtils;
import android.widget.ListView;

import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.TagableElement;
import com.rrja.carja.model.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MaintenanceOrder {

    public static final int TAG_ORDER_SERVICE = 23;
    public static final int TAG_ORDER_GOODS = 30;

    private UserInfo userInfo;
    private CarInfo mCarInfo;
    private HashMap<String, TagableService> orderContent = new HashMap<>();

    private String orderId;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public CarInfo getmCarInfo() {
        return mCarInfo;
    }

    public void setmCarInfo(CarInfo mCarInfo) {
        this.mCarInfo = mCarInfo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void addGoods(String serviceId, MaintenanceService service, TagableGoods goods) {

        if (TextUtils.isEmpty(serviceId) || goods == null) {
            return;
        }

        if (orderContent.containsKey(serviceId) && orderContent.get(serviceId) != null) {

            TagableService tagableService = orderContent.get(serviceId);
            tagableService.addTagableGood(goods);
            return;
        } else if (service != null){
            TagableService tagableService = new TagableService();
            tagableService.setService(service);
            tagableService.addTagableGood(goods);
            orderContent.put(serviceId, tagableService);
        }
    }

    public List listOrderInfo() {

        if (orderContent.size() == 0) {
            return new ArrayList();
        }

        ArrayList<TagableElement> infoList = new ArrayList<>();

        Set<String> keySet = orderContent.keySet();
        String[] keyArray = (String[]) keySet.toArray();
        for (int i = 0; i < keyArray.length; i++) {
            String key = keyArray[i];
            TagableService service = orderContent.get(key);
            if (service.getGoodList().size() != 0) {
                infoList.add(service);
                infoList.addAll(service.getGoodList());
            }
        }

        return infoList;
    }

}
