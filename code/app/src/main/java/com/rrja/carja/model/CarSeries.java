package com.rrja.carja.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/7/23.
 */
public class CarSeries implements Parcelable{

    private int brandId;
    private int id;
    private String logo;
    private String seriesName;
    private String vehicleClass;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(String vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(brandId);
        dest.writeInt(id);
        dest.writeString(logo);
        dest.writeString(seriesName);
        dest.writeString(vehicleClass);
    }

    public static Parcelable.Creator<CarSeries> CREATOR = new Parcelable.Creator<CarSeries>() {

        @Override
        public CarSeries createFromParcel(Parcel source) {

            CarSeries series = new CarSeries();
            series.setBrandId(source.readInt());
            series.setId(source.readInt());
            series.setLogo(source.readString());
            series.setSeriesName(source.readString());
            series.setVehicleClass(source.readString());

            return series;
        }

        @Override
        public CarSeries[] newArray(int size) {
            return new CarSeries[size];
        }
    };
}
