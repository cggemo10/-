package com.rrja.carja.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/10/18.
 */
public class PayInfo implements Parcelable{

    // info to pay
    private String tradeNo;
    private double fee;
    private String subject;
    private String body;
    // info to show
    private String userName;
    private String tel;
    private String carPlat;
    private String serviceLoc;
    private String serviceTime;
    private String invoiceTitle;
    private String invoiceReceiver;
    private String invoiceMail;
    private String invoicePayType;

    public PayInfo(){}

    protected PayInfo(Parcel in) {
        tradeNo = in.readString();
        fee = in.readDouble();
        subject = in.readString();
        body = in.readString();
        userName = in.readString();
        tel = in.readString();
        carPlat = in.readString();
        serviceLoc = in.readString();
        serviceTime = in.readString();
        invoiceTitle = in.readString();
        invoiceReceiver = in.readString();
        invoiceMail = in.readString();
        invoicePayType = in.readString();
    }

    public static final Creator<PayInfo> CREATOR = new Creator<PayInfo>() {
        @Override
        public PayInfo createFromParcel(Parcel in) {
            return new PayInfo(in);
        }

        @Override
        public PayInfo[] newArray(int size) {
            return new PayInfo[size];
        }
    };

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCarPlat() {
        return carPlat;
    }

    public void setCarPlat(String carPlat) {
        this.carPlat = carPlat;
    }

    public String getServiceLoc() {
        return serviceLoc;
    }

    public void setServiceLoc(String serviceLoc) {
        this.serviceLoc = serviceLoc;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceReceiver() {
        return invoiceReceiver;
    }

    public void setInvoiceReceiver(String invoiceReceiver) {
        this.invoiceReceiver = invoiceReceiver;
    }

    public String getInvoiceMail() {
        return invoiceMail;
    }

    public void setInvoiceMail(String invoiceMail) {
        this.invoiceMail = invoiceMail;
    }

    public String getInvoicePayType() {
        return invoicePayType;
    }

    public void setInvoicePayType(String invoicePayType) {
        this.invoicePayType = invoicePayType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tradeNo);
        dest.writeDouble(fee);
        dest.writeString(subject);
        dest.writeString(body);
        dest.writeString(userName);
        dest.writeString(tel);
        dest.writeString(carPlat);
        dest.writeString(serviceLoc);
        dest.writeString(serviceTime);
        dest.writeString(invoiceTitle);
        dest.writeString(invoiceReceiver);
        dest.writeString(invoiceMail);
        dest.writeString(invoicePayType);
    }
}
