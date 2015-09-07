package com.rrja.carja.model.maintenance;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.rrja.carja.model.TagableElement;

import java.util.ArrayList;

@DatabaseTable
public class TagableService implements TagableElement {

    @DatabaseField
    private MaintenanceService service;
    @DatabaseField
    private ArrayList<TagableSubService> subServiceList = new ArrayList<>();
    @DatabaseField
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
    }

    public ArrayList<TagableSubService> getSubServiceList() {
        return subServiceList;
    }

    public int calculateServiceFee() {

        serviceAmount = 0;

        if (subServiceList.size() != 0) {
            for (TagableSubService service : subServiceList) {
                if ("服务费".equals(service.getServiceName())) {
                    serviceAmount = service.getServiceAmount();
                }
            }
        }

        return serviceAmount;
    }
}