package com.rrja.carja.model;

import java.util.List;

/**
 * Created by chongge on 15/7/5.
 */
public class MaintenanceService {

    private int id;
    private String name;
    private String type;
    private double amount;
    private List<MaintenanceGoods> goodsList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<MaintenanceGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<MaintenanceGoods> goodsList) {
        this.goodsList = goodsList;
    }
}
