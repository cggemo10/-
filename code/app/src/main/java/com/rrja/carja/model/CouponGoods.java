package com.rrja.carja.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Administrator on 2015/6/25.
 */
@DatabaseTable(tableName = "coupons")
public class CouponGoods implements Parcelable {

    @DatabaseField(id = true)
    private String couponId;
    @DatabaseField
    private String name;
    @DatabaseField
    private long startTime;
    @DatabaseField
    private long endTime;
    @DatabaseField
    private String tel;
    @DatabaseField
    private String scope;
    @DatabaseField
    private String content;
    @DatabaseField
    private String detal;
    @DatabaseField
    private String picUrl;
    @DatabaseField
    private int discountPrice;
    @DatabaseField
    private int price;
    @DatabaseField
    private String serviceId;


    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String cuponId) {
        this.couponId = cuponId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long time) {
        this.startTime = time;
    }

    public void setEndTime(long time) {
        this.endTime = time;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getTelNumber() {
        return tel;
    }

    public void setTelNumber(String number) {
        this.tel = number;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String address) {
        this.scope = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetal() {
        return detal;
    }

    public void setDetal(String detal) {
        this.detal = detal;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(couponId);
        dest.writeString(name);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeString(tel);
        dest.writeString(scope);
        dest.writeString(content);
        dest.writeString(detal);
        dest.writeString(picUrl);
        dest.writeString(serviceId);
        dest.writeInt(price);
        dest.writeInt(discountPrice);
    }

    public static Parcelable.Creator<CouponGoods> CREATOR = new Parcelable.Creator<CouponGoods>() {

        @Override
        public CouponGoods createFromParcel(Parcel source) {
            CouponGoods coupon = new CouponGoods();
            coupon.setCouponId(source.readString());
            coupon.setName(source.readString());
            coupon.setStartTime(source.readLong());
            coupon.setEndTime(source.readLong());
            coupon.setTelNumber(source.readString());
            coupon.setScope(source.readString());
            coupon.setContent(source.readString());
            coupon.setDetal(source.readString());
            coupon.setPicUrl(source.readString());
            coupon.setServiceId(source.readString());
            coupon.setPrice(source.readInt());
            coupon.setDiscountPrice(source.readInt());
            return coupon;
        }

        @Override
        public CouponGoods[] newArray(int size) {
            return new CouponGoods[size];
        }
    };

    public static CouponGoods parse(JSONObject json) throws JSONException{
        if (json == null || json.length() == 0) {
            return null;
        }

        CouponGoods goods = new CouponGoods();
        goods.setCouponId(json.getInt("id") + "");
        goods.setName(json.getString("name"));
        goods.setContent(json.getString("content"));
        goods.setDetal(json.getString("detail"));
        goods.setDiscountPrice(json.getInt("discountPrice"));
        goods.setPrice(json.getInt("price"));
        goods.setStartTime(json.getLong("start"));
        goods.setEndTime(json.getLong("end"));
        goods.setPicUrl(json.getString("img"));
        goods.setTelNumber(json.getString("mobile"));
        goods.setScope(json.getString("scope"));
        goods.setServiceId(json.getLong("serviceId") + "");

        return goods;

    }
}
