package com.rrja.carja.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2015/6/6.
 */
@DatabaseTable(tableName = "discount")
public class DiscountInfo implements Parcelable{

    @DatabaseField(id = true)
    private String productId;
    @DatabaseField
    private String name;
    @DatabaseField
    private String scope;
    @DatabaseField
    private String time;
    @DatabaseField
    private String content;
    @DatabaseField
    private String mobileNo;
    @DatabaseField
    private String detial;
    @DatabaseField
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

    public static Parcelable.Creator<DiscountInfo> CREATOR = new Parcelable.Creator<DiscountInfo>() {
        @Override
        public DiscountInfo createFromParcel(Parcel source) {
            DiscountInfo discountInfo = new DiscountInfo();
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
        public DiscountInfo[] newArray(int size) {
            return new DiscountInfo[size];
        }
    };

    public static DiscountInfo parse(JSONObject discountJson) throws JSONException{

        if (discountJson == null || discountJson.length() == 0) {
            return null;
        }

        DiscountInfo info = new DiscountInfo();
        info.setProductId(discountJson.getString("id"));
        info.setName(discountJson.getString("name"));
        info.setMobileNo(discountJson.getString("mobile"));
        info.setDetial(discountJson.getString("detail"));

        long startData = discountJson.getLong("start");
        long endData = discountJson.getLong("end");
        SimpleDateFormat format = new SimpleDateFormat();
        Date startDate = new Date(startData);
        Date endDate = new Date(endData);
        info.setTime(format.format(startDate) + " 至\n"+ format.format(endDate));

        info.setContent(discountJson.getString("content"));
        info.setImgUrl(discountJson.getString("image"));
        info.setScope(discountJson.getString("scope"));

        return info;
    }
}
