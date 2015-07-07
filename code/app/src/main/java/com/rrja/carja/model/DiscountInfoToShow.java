package com.rrja.carja.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/6/6.
 */
public class DiscountInfoToShow implements Parcelable{

    private String productId;
    private String name;
    private String scope;
    private String time;
    private String content;
    private String mobileNo;
    private String detial;
    private String imgUrl;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getDetial() {
        return detial;
    }

    public void setDetial(String detial) {
        this.detial = detial;
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
        dest.writeString(productId);
        dest.writeString(name);
        dest.writeString(scope);
        dest.writeString(time);
        dest.writeString(content);
        dest.writeString(mobileNo);
        dest.writeString(detial);
        dest.writeString(imgUrl);
    }

    public static Parcelable.Creator<DiscountInfoToShow> CREATOR = new Parcelable.Creator<DiscountInfoToShow>() {
        @Override
        public DiscountInfoToShow createFromParcel(Parcel source) {
            DiscountInfoToShow discountInfo = new DiscountInfoToShow();
            discountInfo.setProductId(source.readString());
            discountInfo.setName(source.readString());
            discountInfo.setScope(source.readString());
            discountInfo.setTime(source.readString());
            discountInfo.setContent(source.readString());
            discountInfo.setMobileNo(source.readString());
            discountInfo.setDetial(source.readString());
            discountInfo.setImgUrl(source.readString());
            return discountInfo;
        }

        @Override
        public DiscountInfoToShow[] newArray(int size) {
            return new DiscountInfoToShow[size];
        }
    };
}
