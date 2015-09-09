package com.rrja.carja.model.maintenance;

import android.graphics.Paint;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.rrja.carja.model.TagableElement;

import java.util.ArrayList;

public class TagableService implements TagableElement {

    private MaintenanceService service;
    private ArrayList<TagableSubService> subServiceList = new ArrayList<>();
    private int serviceAmount;

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

    public void addTagableGood(TagableSubService subService) {
        subServiceList.add(subService);
    }

    public void removeTagableGood(TagableSubService subService) {
        if (subServiceList.contains(subService)) {
            subServiceList.remove(subService);
        }

        if (subServiceList.size() == 1) {
            TagableSubService mayFeeubService = subServiceList.get(0);
            if ("服务费".equals(mayFeeubService.getServiceName())) {
                subServiceList.remove(mayFeeubService);
            }
        }

    }

    public ArrayList<TagableSubService> getSubServiceList() {
        return subServiceList;
    }

    public int calculateServiceFee() {

        serviceAmount = 0;

        if (subServiceList.size() != 0) {
            for (TagableSubService service : subServiceList) {
                if ("服务费".equals(service.getServiceName())) {
                    serviceAmount += service.getServiceAmount();
                } else {
                    if (service.getGoods() != null) {
                        serviceAmount += service.getGoods().getPrice();
                    }
                }
            }
        }

        return serviceAmount;
    }

    public void removeSubService(TagableSubService subService) {
        if (subServiceList.contains(subService)) {
            subServiceList.remove(subService);
        }

        if (subServiceList.size() == 1) {
            TagableSubService mayFeeSubService = subServiceList.get(0);
            if ("服务费".equals(mayFeeSubService.getServiceName())) {
                subServiceList.remove(mayFeeSubService);
            }
        }
    }
}