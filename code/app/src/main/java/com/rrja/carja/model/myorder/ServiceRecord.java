package com.rrja.carja.model.myorder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chongge on 15/10/14.
 */
public class ServiceRecord {

    private String serviceId;
    private String tag;
    private double amount;
    private List<SubServiceRecord> subServiceRecords = new ArrayList<>();

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<SubServiceRecord> getSubServiceRecords() {
        return subServiceRecords;
    }
}
