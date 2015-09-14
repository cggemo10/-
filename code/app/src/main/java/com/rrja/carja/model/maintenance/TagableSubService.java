package com.rrja.carja.model.maintenance;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.rrja.carja.model.TagableElement;

import org.json.JSONException;
import org.json.JSONObject;

public class TagableSubService implements TagableElement, Parcelable {

    private MaintenanceGoods goods;
    private MaintenanceService subService;

    @Override
    public int getTag() {
        return MaintenanceOrder.TAG_ORDER_GOODS;
    }

    public MaintenanceGoods getGoods() {
        return goods;
    }

    public void setGoods(MaintenanceGoods goods) {
        this.goods = goods;
    }

    public MaintenanceService getSubService() {
        return subService;
    }

    public void setSubService(MaintenanceService subService) {
        this.subService = subService;
    }

    public String getGoodsId() {
        if (goods != null && !TextUtils.isEmpty(goods.getId())) {
            return goods.getId();
        } else {
            return "";
        }
    }

    public String getServiceName() {
        if (subService != null && !TextUtils.isEmpty(subService.getName())) {
            return subService.getName();
        } else {
            return "";
        }
    }

    public double getServiceAmount() {
        if (subService != null && !TextUtils.isEmpty(subService.getName()) && "服务费".equals(subService.getName())) {
            return subService.getAmount();
        } else {
            return 0;
        }
    }

    public double calculateGoodsFee() {
        if (goods != null) {
            return goods.getPrice();
        } else {
            return 0;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof TagableSubService) {
            TagableSubService oService = (TagableSubService) o;
            if (this.getSubService() != null && oService.getSubService() != null) {
                if (this.getSubService().equals(oService.getSubService())) {
                    return true;
                }
                return false;
            } else {
                if (this.getSubService() == null && oService.getSubService() == null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(subService,flags);
        dest.writeParcelable(goods,flags);
    }

    public static Creator<TagableSubService> CREATOR = new Creator<TagableSubService>() {
        @Override
        public TagableSubService createFromParcel(Parcel source) {

            TagableSubService tagableSubService = new TagableSubService();
            tagableSubService.setSubService((MaintenanceService) source.readParcelable(MaintenanceService.class.getClassLoader()));
            tagableSubService.setGoods((MaintenanceGoods) source.readParcelable(MaintenanceGoods.class.getClassLoader()));
            return tagableSubService;
        }

        @Override
        public TagableSubService[] newArray(int size) {
            return new TagableSubService[size];
        }
    };

    public JSONObject getCommitContent() throws JSONException {

        if (subService == null) {
            return null;
        }

        if ("服务费".equals(subService.getName())) {
            return null;
        }

        if (goods == null) {
            return null;
        }

        JSONObject json = new JSONObject();
        json.put("goodsAmount", goods.getPrice());
        json.put("goodsId", goods.getId());
        json.put("subServiceId", subService.getAmount());
        json.put("couponsAmount", 0);
        json.put("couponId", 0);

        return json;
    }
}
