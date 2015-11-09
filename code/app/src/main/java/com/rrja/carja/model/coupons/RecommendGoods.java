package com.rrja.carja.model.coupons;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/6/6.
 */
@DatabaseTable(tableName = "discount")
public class RecommendGoods implements Parcelable {

    @DatabaseField(id = true)
    private String productId;
    @DatabaseField
    private String name;
    @DatabaseField
    private String scope;
    @DatabaseField
    private long startTime;
    @DatabaseField
    private long endTime;
    @DatabaseField
    private String content;
    @DatabaseField
    private String mobileNo;
    @DatabaseField
    private String detial;
    @DatabaseField
    private String imgUrl;
    @DatabaseField
    private int price;
    @DatabaseField
    private int discountPrice;
    @DatabaseField
    private String serviceId;


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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long time) {
        this.startTime = time;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long time) {
        this.endTime = time;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setDiscountPrice(int price) {
        this.discountPrice = price;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public String getDetial() {
        return detial;
    }

    public void setDetial(String detial) {
        this.detial = detial;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(String id) {
        this.serviceId = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(name);
        dest.writeString(scope);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeString(content);
        dest.writeString(mobileNo);
        dest.writeString(detial);
        dest.writeString(imgUrl);
        dest.writeInt(price);
        dest.writeInt(discountPrice);
        dest.writeString(serviceId);
    }

    public static Creator<RecommendGoods> CREATOR = new Creator<RecommendGoods>() {
        @Override
        public RecommendGoods createFromParcel(Parcel source) {
            RecommendGoods discountInfo = new RecommendGoods();
            discountInfo.setProductId(source.readString());
            discountInfo.setName(source.readString());
            discountInfo.setScope(source.readString());
            discountInfo.setStartTime(source.readLong());
            discountInfo.setEndTime(source.readLong());
            discountInfo.setContent(source.readString());
            discountInfo.setMobileNo(source.readString());
            discountInfo.setDetial(source.readString());
            discountInfo.setImgUrl(source.readString());
            discountInfo.setPrice(source.readInt());
            discountInfo.setDiscountPrice(source.readInt());
            discountInfo.setServiceId(source.readString());
            return discountInfo;
        }

        @Override
        public RecommendGoods[] newArray(int size) {
            return new RecommendGoods[size];
        }
    };

    public static RecommendGoods parse(JSONObject discountJson) throws JSONException {

        if (discountJson == null || discountJson.length() == 0) {
            return null;
        }

        RecommendGoods info = new RecommendGoods();
        info.setProductId(discountJson.getString("id"));
        info.setName(discountJson.getString("name"));
        info.setMobileNo(discountJson.getString("mobile"));
        info.setDetial(discountJson.getString("detail"));
        info.setPrice(discountJson.getInt("price"));
        info.setDiscountPrice(discountJson.getInt("discountPrice"));
        info.setStartTime(discountJson.getLong("start"));
        info.setEndTime(discountJson.getLong("end"));
        info.setServiceId(discountJson.getLong("serviceId") + "");
        info.setContent(discountJson.getString("content"));
        info.setImgUrl(discountJson.getString("img"));
        info.setScope(discountJson.getString("scope"));

        return info;
    }
}
