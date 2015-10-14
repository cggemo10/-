package com.rrja.carja.model.myorder;

/**
 * Created by chongge on 15/10/14.
 */
public class SubServiceRecord {

    private double goodsAmount;
    private String goodsId;
    private long subServiceId;
    private double couponsAmount;
    private long couponsId;

    public double getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(double goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public long getSubServiceId() {
        return subServiceId;
    }

    public void setSubServiceId(long subServiceId) {
        this.subServiceId = subServiceId;
    }

    public double getCouponsAmount() {
        return couponsAmount;
    }

    public void setCouponsAmount(double couponsAmount) {
        this.couponsAmount = couponsAmount;
    }

    public long getCouponsId() {
        return couponsId;
    }

    public void setCouponsId(long couponsId) {
        this.couponsId = couponsId;
    }
}
