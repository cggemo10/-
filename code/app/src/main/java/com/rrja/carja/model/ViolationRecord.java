package com.rrja.carja.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class ViolationRecord implements Parcelable{

    private String act;
    private String area;
    private String code;
    private String date;
    private String fen;
    private String money;

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public static ViolationRecord parse(JSONObject json) throws JSONException {
        ViolationRecord record = new ViolationRecord();
        record.setAct(json.getString("act"));
        record.setArea(json.getString("area"));
        record.setCode(json.getString("code"));
        record.setDate(json.getString("date"));
        record.setFen(json.getString("fen"));
        record.setMoney(json.getString("money"));

        return record;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(act);
        dest.writeString(area);
        dest.writeString(code);
        dest.writeString(date);
        dest.writeString(fen);
        dest.writeString(money);
    }

    public static Creator<ViolationRecord> CREATOR = new Creator<ViolationRecord>() {
        @Override
        public ViolationRecord createFromParcel(Parcel source) {

            ViolationRecord record = new ViolationRecord();
            record.setAct(source.readString());
            record.setArea(source.readString());
            record.setCode(source.readString());
            record.setDate(source.readString());
            record.setFen(source.readString());
            record.setMoney(source.readString());
            return record;
        }

        @Override
        public ViolationRecord[] newArray(int size) {
            return new ViolationRecord[size];
        }
    };
}
