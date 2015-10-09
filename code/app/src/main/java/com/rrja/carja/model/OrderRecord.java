package com.rrja.carja.model;

import com.rrja.carja.model.maintenance.MaintenanceOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chongge on 15/10/10.
 */
public class OrderRecord {

    private String contact;
    private long id;
    private String invoiceTitle;
    private long orderDateTime;
    private String userId;
    private String carId;
    private double totalAmount;

    private String orderNumber;
    private String orderStatus;
    private String payChannel;
    private String phone;
    private String platNum;
    private double price;
    private String receiverAddress;
    private String receiverName;
    private long serviceDateTime;
    private String serviceLocation;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public long getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(long orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlatNum() {
        return platNum;
    }

    public void setPlatNum(String platNum) {
        this.platNum = platNum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public long getServiceDateTime() {
        return serviceDateTime;
    }

    public void setServiceDateTime(long serviceDateTime) {
        this.serviceDateTime = serviceDateTime;
    }

    public String getServiceLocation() {
        return serviceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    public static OrderRecord parse(JSONObject json) throws JSONException{
        OrderRecord record = new OrderRecord();

        record.setContact(json.getString("contacts"));
        record.setId(json.getLong("id"));
        record.setInvoiceTitle(json.getString("invoiceTitle"));
        record.setOrderDateTime(json.getLong("orderDateTime"));

        String orderDetailsStr = json.getString("orderDetails");
        JSONObject orderDetails = new JSONObject(orderDetailsStr);
        record.setUserId(orderDetails.getString("userId"));
        record.setCarId(orderDetails.getString("carId"));
        record.setTotalAmount(orderDetails.getDouble("totalAmount"));

        record.setOrderNumber(json.getString("orderNumber"));
        record.setOrderStatus(json.getString("orderStatus"));
        record.setPayChannel(json.getString("payChannel"));
        record.setPhone(json.getString("phone"));
        record.setPlatNum(json.getString("plateNum"));
        record.setPrice(json.getDouble("price"));
        record.setReceiverAddress(json.getString("receiverAddress"));
        record.setReceiverName(json.getString("receiverName"));
        record.setServiceDateTime(json.getLong("serviceDateTime"));
        record.setServiceLocation(json.getString("serviceLocation"));

        JSONArray content = orderDetails.getJSONArray("content");
        for (int i = 0; i < content.length(); i++) {
            JSONObject contentJson = content.getJSONObject(i);
            String serviceId = contentJson.getString("serviceId");
            String tag = contentJson.getString("tag");
            int serviceAmount = contentJson.getInt("serviceAmount");
            JSONArray subService = contentJson.getJSONArray("subService");

            for (int j = 0; j < subService.length(); j++) {
                JSONObject subServiceJson = subService.getJSONObject(j);
                double goodsAmount = subServiceJson.getDouble("goodsAmount");
                String goodsId = subServiceJson.getString("goodsId");
                long subServiceId = subServiceJson.getLong("subServiceId");
                double couponsAmount = subServiceJson.getDouble("couponsAmount");
                long couponId = subServiceJson.getLong("couponId");
            }


            // TODO

        }

        return record;

    }

}
