package com.rrja.carja.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "car_info")
public class CarInfo implements Parcelable{

    @DatabaseField(id = true)
    private String id;
    @DatabaseField
    private String frameNo6;
    @DatabaseField
    private String buyTime;
    @DatabaseField
    private String engineNo;
    @DatabaseField
    private String phoneNo;
    @DatabaseField
    private String seriesID;
    @DatabaseField
    private String carBrandID;
    @DatabaseField
    private String carModelID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrameNo6() {
        return frameNo6;
    }

    public void setFrameNo6(String frameNo6) {
        this.frameNo6 = frameNo6;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getSeriesID() {
        return seriesID;
    }

    public void setSeriesID(String seriesID) {
        this.seriesID = seriesID;
    }

    public String getCarBrandID() {
        return carBrandID;
    }

    public void setCarBrandID(String carBrandID) {
        this.carBrandID = carBrandID;
    }

    public String getCarModelID() {
        return carModelID;
    }

    public void setCarModelID(String carModelID) {
        this.carModelID = carModelID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id + "");
        dest.writeString(frameNo6);
        dest.writeString(buyTime);
        dest.writeString(engineNo);
        dest.writeString(phoneNo);
        dest.writeString(seriesID);
        dest.writeString(carBrandID);
        dest.writeString(carModelID);

    }

    public static Creator<CarInfo> CREATOR = new Creator<CarInfo>() {
        @Override
        public CarInfo createFromParcel(Parcel source) {
            CarInfo info = new CarInfo();
            String id = source.readString();
            if (!"null".equals(id)) {
                info.setId(id);
            }
            info.setFrameNo6(source.readString());
            info.setBuyTime(source.readString());
            info.setEngineNo(source.readString());
            info.setPhoneNo(source.readString());
            info.setSeriesID(source.readString());
            info.setCarBrandID(source.readString());
            info.setCarModelID(source.readString());
            return info;
        }

        @Override
        public CarInfo[] newArray(int size) {
            return new CarInfo[size];
        }
    };
}
