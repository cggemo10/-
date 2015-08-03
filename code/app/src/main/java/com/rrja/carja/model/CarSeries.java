package com.rrja.carja.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/23.
 */
public class CarSeries implements Parcelable{

    private int brandId;
    private String id;
    private String logo;
    private String seriesName;
    private String vehicleClass;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        dest.writeString(id);
        dest.writeString(logo);
        dest.writeString(seriesName);
        dest.writeString(vehicleClass);
    }

    public static Parcelable.Creator<CarSeries> CREATOR = new Parcelable.Creator<CarSeries>() {

        @Override
        public CarSeries createFromParcel(Parcel source) {

            CarSeries series = new CarSeries();
            series.setBrandId(source.readInt());
            series.setId(source.readString());
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

    public static CarSeries parse(JSONObject seriesJson) throws JSONException{
        if (seriesJson == null || TextUtils.isEmpty(seriesJson.toString())) {
            return null;
        }

        CarSeries info = new CarSeries();

        info.setBrandId(seriesJson.getInt("brandId"));
        info.setId(seriesJson.getInt("id") + "");
        info.setLogo(seriesJson.getString("logo"));
        info.setSeriesName(seriesJson.getString("seriesName"));
        info.setVehicleClass(seriesJson.getString("vehicleClass"));

        return info;
    }
}
