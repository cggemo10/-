package com.rrja.carja.model.maintenance;

import android.widget.ListView;

import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.TagableElement;
import com.rrja.carja.model.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceOrder {

    public static final int TAG_ORDER_SERVICE = 23;
    public static final int TAG_ORDER_GOODS = 30;

    private UserInfo userInfo;
    private CarInfo mCarInfo;
    private List<TagableGoods> service1 = new ArrayList<>();
    private List<TagableGoods> service2 = new ArrayList<>();
    private List<TagableGoods> service3 = new ArrayList<>();

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

    public void addGoods(String serviceId, TagableGoods goods) {

        if (goods == null) {
            return;
        }

        if ("101".equals(serviceId)) {
            service1.add(goods);
        }

        if ("102".equals(serviceId)) {
            service2.add(goods);
        }

        if ("103".equals(serviceId)) {
            service3.add(goods);
        }
    }

    public List listOrderInfo() {
        ArrayList<TagableElement> list = new ArrayList<>();

        if (service1.size() != 0) {
            TagableService service = new TagableService();
            service.setService(CoreManager.getManager().getOrderMaintenanceService());
            list.add(service);

            list.addAll(service1);
        }

        if (service2.size() != 0) {
            TagableService service = new TagableService();
            service.setService(CoreManager.getManager().getOrderRepairService());
            list.add(service);

            list.addAll(service2);
        }

        if (service3.size() != 0) {
            TagableService service = new TagableService();
            service.setService(CoreManager.getManager().getOrderCosmetologyService());
            list.add(service);

            list.addAll(service3);
        }

        return list;
    }

}
