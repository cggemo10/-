package com.rrja.carja.model.maintenance;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.UserInfo;
import com.rrja.carja.model.coupons.UserCoupons;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MaintenanceOrder implements Parcelable{

    public static final int TAG_ORDER_SERVICE = 23;
    public static final int TAG_ORDER_GOODS = 30;


    private String id;
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

    public void  addGoods(String serviceId, MaintenanceService service, TagableSubService subService) {

        if (TextUtils.isEmpty(serviceId) || subService == null) {
            return;
        }

        if (subService.getGoods() != null) {
            String goodsId = subService.getGoods().getId();
            UserCoupons coupons = CoreManager.getManager().usedCouponsByGoodsId(goodsId);
            if (coupons != null) {
                subService.setCoupons(coupons);
            }
        }
        if (orderContent.containsKey(serviceId) && orderContent.get(serviceId) != null) {

            TagableService tagableService = orderContent.get(serviceId);
            tagableService.addTagableGood(subService);

        } else if (service != null) {

            TagableService tagableService = new TagableService();
            tagableService.setService(service);
            tagableService.addTagableGood(subService);
            orderContent.put(serviceId, tagableService);
        }
    }

    public boolean isOrderEmpty() {
        return orderContent.isEmpty();
    }

    public List<TagableService> listOrderInfo() {

        if (orderContent.size() == 0) {
            return new ArrayList();
        }

        ArrayList<TagableService> infoList = new ArrayList<>();

        Set<String> keySet = orderContent.keySet();
        Object[] keyArray = keySet.toArray();
        for (int i = 0; i < keyArray.length; i++) {
            String key = keyArray[i].toString();
            TagableService service = orderContent.get(key);
            infoList.add(service);
        }

        return infoList;
    }

    public double calculateTotalFee() {
        double serviceFee = 0;
        if (orderContent.size() != 0) {
            Set<String> keySet = orderContent.keySet();
            for (String key : keySet) {
                TagableService service = orderContent.get(key);
                serviceFee += service.calculateServiceTotalFee();
            }
        }

        return serviceFee;
    }


    public void removeSubService(String serviceId, TagableSubService subService) {
        if (!orderContent.containsKey(serviceId)) {
            return;
        }
        UserCoupons coupons = subService.getCoupons();
        if (coupons != null) {
            CoreManager.getManager().addUnusedCouponsWithGoodsId(subService.getGoodsId(), coupons);
        }
        TagableService tagableService = orderContent.get(serviceId);
        tagableService.removeSubService(subService);
        if (tagableService.getSubServiceList().size() == 0) {
            removeService(serviceId);
        }

        calculateTotalFee();
    }

    public void removeService(String serviceId) {
        if (orderContent.containsKey(serviceId)) {
            orderContent.remove(serviceId);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeParcelable(userInfo, flags);
        dest.writeParcelable(mCarInfo, flags);
//        Bundle bundle = new Bundle();
//        bundle.("map", orderContent);
        dest.writeMap(orderContent);
    }

    public static Creator<MaintenanceOrder> CREATOR = new Creator<MaintenanceOrder>() {
        @Override
        public MaintenanceOrder createFromParcel(Parcel source) {
            MaintenanceOrder order = new MaintenanceOrder();

            order.id = source.readString();
            order.setUserInfo((UserInfo) source.readParcelable(UserInfo.class.getClassLoader()));
            order.setmCarInfo((CarInfo) source.readParcelable(CarInfo.class.getClassLoader()));
//            Bundle bundle = source.readBundle()
//            order.orderContent = (HashMap<String, TagableService>) source.readBundle().getSerializable("map");
            order.orderContent = source.readHashMap(TagableService.class.getClassLoader());
            return order;
        }

        @Override
        public MaintenanceOrder[] newArray(int size) {
            return new MaintenanceOrder[size];
        }
    };

    public String getCommitContent() {
        JSONObject json = new JSONObject();
        try {
            json.put("userId", userInfo.getId());
            json.put("carId", mCarInfo.getId());
            double totalFee = calculateTotalFee();
            json.put("totlaAmount", totalFee);

            JSONArray array = new JSONArray();
            Object[] keys = orderContent.keySet().toArray();
            for (Object key : keys) {
                TagableService tagableService = orderContent.get(key.toString());
                array.put(tagableService.getCommitContent());
            }

            json.put("content", array);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
