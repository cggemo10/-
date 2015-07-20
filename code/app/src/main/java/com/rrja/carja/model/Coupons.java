package com.rrja.carja.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2015/6/25.
 */
@DatabaseTable(tableName = "coupons")
public class Coupons implements Parcelable {

    @DatabaseField(id = true)
    private String couponId;
    @DatabaseField
    private String couponCode;
    @DatabaseField
    private String name;
    @DatabaseField
    private String time;
    @DatabaseField
    private String tel;
    @DatabaseField
    private String address;
    @DatabaseField
    private String content;
    @DatabaseField
    private String detal;
    @DatabaseField
    private String picUrl;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String cuponId) {
        this.couponId = cuponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String cuponCode) {
        this.couponCode = cuponCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTelNumber() {
        return tel;
    }

    public void setTelNumber(String number) {
        this.tel = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(couponId);
        dest.writeString(couponCode);
        dest.writeString(name);
        dest.writeString(time);
        dest.writeString(tel);
        dest.writeString(address);
        dest.writeString(content);
        dest.writeString(detal);
        dest.writeString(picUrl);
    }

    public static Parcelable.Creator<Coupons> CREATOR = new Parcelable.Creator<Coupons>() {

        @Override
        public Coupons createFromParcel(Parcel source) {
            Coupons coupon = new Coupons();
            coupon.setCouponId(source.readString());
            coupon.setCouponCode(source.readString());
            coupon.setName(source.readString());
            coupon.setTime(source.readString());
            coupon.setTelNumber(source.readString());
            coupon.setAddress(source.readString());
            coupon.setContent(source.readString());
            coupon.setDetal(source.readString());
            coupon.setPicUrl(source.readString());
            return coupon;
        }

        @Override
        public Coupons[] newArray(int size) {
            return new Coupons[size];
        }
    };
}
