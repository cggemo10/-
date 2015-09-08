package com.rrja.carja.model.maintenance;

import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.rrja.carja.model.TagableElement;

import org.json.JSONObject;

public class TagableSubService implements TagableElement {
    
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

    public MaintenanceService getSubServiceId() {
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

    public int getServiceAmount() {
        if (subService != null && TextUtils.isEmpty(subService.getName()) && "服务费".equals(subService.getName())) {
            return subService.getAmount();
        } else {
            return 0;
        }
    }

    public int calculateGoodsFee() {
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
            if (this.getSubServiceId() != null && oService.getSubServiceId() != null) {
                if (this.getSubServiceId().equals(oService.getSubServiceId())) {
                    return true;
                }
                return false;
            } else {
                if (this.getSubServiceId() == null && oService.getSubServiceId() == null) {
                    return true;
                }
            }
        }
        return false;
    }
}
