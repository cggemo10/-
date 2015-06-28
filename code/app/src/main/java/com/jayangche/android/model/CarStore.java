package com.jayangche.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chongge on 15/6/27.
 */
public class CarStore implements Parcelable {

    private String storeId;
    private String storeName;
    private String address;
    private String storeImg;
    private String area;
    private String tel;
    private String openTime;
    private String payType;

    private String desc;

    private double lat;
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

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
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
}
