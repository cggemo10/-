package com.rrja.carja.model.maintenance;

import com.rrja.carja.model.TagableElement;

public class TagableGoods implements TagableElement {

    private MaintenanceGoods goods;
    private String subServiceId;
    private String thdServiceId;

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

    public String getSubServiceId() {
        return subServiceId;
    }

    public void setSubServiceId(String subServiceId) {
        this.subServiceId = subServiceId;
    }

    public String getThdServiceId() {
        return thdServiceId;
    }

    public void setThdServiceId(String thdServiceId) {
        this.thdServiceId = thdServiceId;
    }
}
