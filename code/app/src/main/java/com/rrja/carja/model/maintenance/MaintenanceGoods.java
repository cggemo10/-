package com.rrja.carja.model.maintenance;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chongge on 15/7/5.
 */

public class MaintenanceGoods implements Parcelable{

    private String id;
    private String content;
    private String detail;
    private double discountPrice;
    private double price;
    private String name;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    /*
    {"content":"",
    "detail":"",
    "discount":false,
    "discountPrice":2.07,
    "end":1440950400000,
    "id":1000001,
    "image":"http:\/\/120.25.201.50\/api\/upload\/goods\/",
    "mobile":"05928077777",
    "name":"嘉实多 极护0W-40（4L）",
    "price":4.14,
    "scope":"日日建安全部直营店",
    "start":1438358400000}
     */
    public static MaintenanceGoods parse(JSONObject goodsJson) throws JSONException{

        MaintenanceGoods goods = new MaintenanceGoods();
        goods.setContent(goodsJson.getString("content"));
        goods.setDiscountPrice(goodsJson.getDouble("discountPrice"));
        goods.setPrice(goodsJson.getDouble("price"));
        goods.setId(goodsJson.getString("id"));
        goods.setName(goodsJson.getString("name"));

        return goods;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(content);
        dest.writeString(detail);
        dest.writeDouble(discountPrice);
        dest.writeDouble(price);
        dest.writeString(name);
    }

    public static Creator<MaintenanceGoods> CREATOR = new Creator<MaintenanceGoods>() {
        @Override
        public MaintenanceGoods createFromParcel(Parcel source) {

            MaintenanceGoods goods = new MaintenanceGoods();
            goods.setId(source.readString());
            goods.setContent(source.readString());
            goods.setDetail(source.readString());
            goods.setDiscountPrice(source.readDouble());
            goods.setPrice(source.readDouble());
            goods.setName(source.readString());

            return goods;
        }

        @Override
        public MaintenanceGoods[] newArray(int size) {
            return new MaintenanceGoods[size];
        }
    };
}
