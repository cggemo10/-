package com.rrja.carja.model.maintenance;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chongge on 15/7/5.
 */
@DatabaseTable
public class MaintenanceGoods {

    @DatabaseField(id = true)
    private String id;
    @DatabaseField
    private String goodsId;
    @DatabaseField
    private String content;
    @DatabaseField
    private String detail;
    @DatabaseField
    private int discountPrice;
    @DatabaseField
    private int price;
    @DatabaseField
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

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public static MaintenanceGoods parse(JSONObject goodsJson) throws JSONException{

        MaintenanceGoods goods = new MaintenanceGoods();
        goods.setContent(goodsJson.getString("content"));
        goods.setDiscountPrice(goodsJson.getInt("discountPrice"));
        goods.setPrice(goodsJson.getInt("price"));
        goods.setGoodsId(goodsJson.getString("id"));
        return goods;
    }
}
