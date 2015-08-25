package com.rrja.carja.model.maintenance;

import android.text.TextUtils;

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

    public void setSubServiceId(MaintenanceService subService) {
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
        if (subService != null && TextUtils.isEmpty(subService.getName()) && "·þÎñ·Ñ".equals(subService.getName())) {
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

}
