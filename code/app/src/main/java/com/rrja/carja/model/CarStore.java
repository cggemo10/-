package com.rrja.carja.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chongge on 15/6/27.
 */
@DatabaseTable(tableName = "car_store")
public class CarStore implements Parcelable {

    @DatabaseField(id = true)
    private String storeId;
    @DatabaseField
    private String storeName;
    @DatabaseField
    private String address;
    @DatabaseField
    private String storeImg;
    @DatabaseField
    private String area;
    @DatabaseField
    private String tel;
    @DatabaseField
    private String openTime;
    @DatabaseField
    private String payType;
    @DatabaseField
    private String desc;
    @DatabaseField
    private double lat;
    @DatabaseField
    private double lng;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStoreImg() {
        return storeImg;
    }

    public void setStoreImg(String storeImg) {
        this.storeImg = storeImg;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(storeId);
        dest.writeString(storeName);
        dest.writeString(address);
        dest.writeString(storeImg);
        dest.writeString(area);
        dest.writeString(tel);
        dest.writeString(openTime);
        dest.writeString(payType);
        dest.writeString(desc);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    public static Creator<CarStore> CREATOR = new Creator<CarStore>() {
        @Override
        public CarStore createFromParcel(Parcel source) {

            CarStore store = new CarStore();
            store.setStoreId(source.readString());
            store.setStoreName(source.readString());
            store.setAddress(source.readString());
            store.setStoreImg(source.readString());
            store.setArea(source.readString());
            store.setTel(source.readString());
            store.setOpenTime(source.readString());
            store.setPayType(source.readString());
            store.setDesc(source.readString());
            store.setLat(source.readDouble());
            store.setLng(source.readDouble());
            return store;
        }

        @Override
        public CarStore[] newArray(int size) {
            return new CarStore[size];
        }
    };


    public String cacluteDistance() {
        return "--";
    }

    public static CarStore parse(JSONObject storeJs) throws JSONException {

        return null;
    }
}
