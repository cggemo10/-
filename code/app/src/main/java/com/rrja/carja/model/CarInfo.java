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
    private CarSeries series;
    @DatabaseField
    private CarBrand carBrand;
    @DatabaseField
    private CarModel carModel;

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

    public CarSeries getSeries() {
        return series;
    }

    public void setSeries(CarSeries seriesID) {
        this.series = seriesID;
    }

    public CarBrand getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(CarBrand carBrandID) {
        this.carBrand = carBrandID;
    }

    public CarModel getCarModel() {
        return carModel;
    }

    public void setCarModel(CarModel carModelID) {
        this.carModel = carModelID;
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
        dest.writeParcelable(series, flags);
        dest.writeParcelable(carBrand, flags);
        dest.writeParcelable(carModel, flags);

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
            info.setSeries((CarSeries) source.readParcelable(CarSeries.class.getClassLoader()));
            info.setCarBrand((CarBrand) source.readParcelable(CarBrand.class.getClassLoader()));
            info.setCarModel((CarModel) source.readParcelable(CarModel.class.getClassLoader()));
            return info;
        }

        @Override
        public CarInfo[] newArray(int size) {
            return new CarInfo[size];
        }
    };
}
