package com.rrja.carja.model.maintenance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by chongge on 15/7/5.
 */
public class MaintenanceService {

    private String parentId;
    private String level;
    private String id;
    private String name;
    private String type;
    private double amount;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public static MaintenanceService parse(JSONObject serviceJson) throws JSONException{

        MaintenanceService service = new MaintenanceService();
        service.setId(serviceJson.getLong("id") + "");
        service.setParentId(serviceJson.getLong("father") + "");
        service.setLevel(serviceJson.getString("level"));
        service.setAmount(serviceJson.getInt("price"));
        service.setType(serviceJson.getString("type"));
        return service;
    }
}
