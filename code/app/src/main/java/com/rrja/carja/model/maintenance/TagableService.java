package com.rrja.carja.model.maintenance;

import com.rrja.carja.model.TagableElement;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TagableService implements TagableElement {

    private MaintenanceService service;
    private ArrayList<TagableGoods> goodList = new ArrayList<>();

    @Override
    public int getTag() {
        return MaintenanceOrder.TAG_ORDER_SERVICE;
    }

    public MaintenanceService getService() {
        return service;
    }

    public void setService(MaintenanceService service) {

        this.service = service;
    }

    public void addTagableGood(TagableGoods goods) {
        goodList.add(goods);
    }

    public void removeTagableGood(TagableGoods goods) {
        if (goodList.contains(goods)) {
            goodList.remove(goods);
        }
    }

    public ArrayList<TagableGoods> getGoodList() {
        return goodList;
    }
}