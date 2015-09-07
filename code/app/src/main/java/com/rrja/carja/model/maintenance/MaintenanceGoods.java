package com.rrja.carja.model.maintenance;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chongge on 15/7/5.
 */

public class MaintenanceGoods {

    private String id;
    private String content;
    private String detail;
    private int discountPrice;
    private int price;
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

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public static MaintenanceGoods parse(JSONObject goodsJson) throws JSONException{

        MaintenanceGoods goods = new MaintenanceGoods();
        goods.setContent(goodsJson.getString("content"));
        goods.setDiscountPrice(goodsJson.getInt("discountPrice"));
        goods.setPrice(goodsJson.getInt("price"));
        goods.setId(goodsJson.getString("id"));
        return goods;
    }
}
