package com.rrja.carja.model.maintenance;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class MaintenanceService implements Parcelable{

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
        service.setAmount(serviceJson.getDouble("price"));
        service.setType(serviceJson.getString("type"));
        service.setName(serviceJson.getString("name"));

        return service;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(parentId);
        dest.writeString(level);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeDouble(amount);
    }

    public static Creator<MaintenanceService> CREATOR = new Creator<MaintenanceService>() {
        @Override
        public MaintenanceService createFromParcel(Parcel source) {

            MaintenanceService service = new MaintenanceService();
            service.setParentId(source.readString());
            service.setLevel(source.readString());
            service.setId(source.readString());
            service.setName(source.readString());
            service.setType(source.readString());
            service.setAmount(source.readDouble());

            return service;
        }

        @Override
        public MaintenanceService[] newArray(int size) {
            return new MaintenanceService[size  ];
        }
    };
}
