package com.rrja.carja.model.coupons;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class UserCoupons implements Parcelable {

    private long id;
    private long getTime;
    private long startTime;
    private long endTime;
    private long goodsId;
    private String status;
    private long userId;
    private double couponsPrice;
    private String couponsNum;
    private String goodsName;
    private String type;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeLong(getTime);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeLong(goodsId);
        dest.writeString(status);
        dest.writeLong(userId);
        dest.writeDouble(couponsPrice);
        dest.writeString(couponsNum);
        dest.writeString(goodsName);
        dest.writeString(type);
    }

    public static Creator<UserCoupons> CREATOR = new Creator<UserCoupons>() {
        @Override
        public UserCoupons createFromParcel(Parcel source) {

            UserCoupons coupons = new UserCoupons();
            coupons.setId(source.readLong());
            coupons.setGetTime(source.readLong());
            coupons.setStartTime(source.readLong());
            coupons.setEndTime(source.readLong());
            coupons.setGoodsId(source.readLong());
            coupons.setStatus(source.readString());
            coupons.setUserId(source.readLong());
            coupons.setCouponsPrice(source.readDouble());
            coupons.setCouponsNum(source.readString());
            coupons.setGoodsName(source.readString());
            coupons.setType(source.readString());

            return coupons;
        }

        @Override
        public UserCoupons[] newArray(int size) {
            return new UserCoupons[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGetTime() {
        return getTime;
    }

    public void setGetTime(long getTime) {
        this.getTime = getTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getCouponsPrice() {
        return couponsPrice;
    }

    public void setCouponsPrice(double couponsPrice) {
        this.couponsPrice = couponsPrice;
    }

    public String getCouponsNum() {
        return couponsNum;
    }

    public void setCouponsNum(String couponsNum) {
        this.couponsNum = couponsNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserCoupons parse(JSONObject json) throws JSONException {

        UserCoupons coupons = new UserCoupons();
        coupons.setId(json.getLong("id"));
        coupons.setGetTime(json.getLong("dt"));
        coupons.setGoodsId(json.getLong("goodsId"));
        coupons.setStatus(json.getString("status"));
        coupons.setStartTime(json.getLong("start"));
        coupons.setUserId(json.getLong("userId"));
        coupons.setCouponsPrice(json.getDouble("couponPrice"));
        coupons.setCouponsNum(json.getString("number"));
        coupons.setGoodsName(json.getString("goodsName"));
        coupons.setEndTime(json.getLong("end"));
        coupons.setType(json.getString("type"));
        return coupons;
    }
}
