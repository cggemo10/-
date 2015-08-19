package com.rrja.carja.model.maintenance;

import com.rrja.carja.model.TagableElement;

public class TagableService implements TagableElement {

    private MaintenanceService service;

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
}