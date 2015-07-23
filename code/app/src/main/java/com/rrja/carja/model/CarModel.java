package com.rrja.carja.model;

/*
engineCapacity: "2000",
id: 60,
seriesId: 8,
seriesName: "奥迪A4(进口)",
transmission: "手自一体",
typeName: "3.0 quattro敞蓬",
typeSeries: "2003 款"
 */

import android.os.Parcel;
import android.os.Parcelable;

public class CarModel implements Parcelable{

    private String engineCapacity;
    private int id;
    private int seriesId;
    private String seriesName;
    private String transmission;
    private String typeName;
    private String typeSeries;


    public String getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(String engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeSeries() {
        return typeSeries;
    }

    public void setTypeSeries(String typeSeries) {
        this.typeSeries = typeSeries;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(engineCapacity);
        dest.writeInt(id);
        dest.writeInt(seriesId);
        dest.writeString(seriesName);
        dest.writeString(transmission);
        dest.writeString(typeName);
        dest.writeString(typeSeries);
    }

    public static Parcelable.Creator<CarModel> CREATOR = new Parcelable.Creator<CarModel>() {

        @Override
        public CarModel createFromParcel(Parcel source) {

            CarModel model = new CarModel();
            model.setEngineCapacity(source.readString());
            model.setId(source.readInt());
            model.setSeriesId(source.readInt());
            model.setSeriesName(source.readString());
            model.setTransmission(source.readString());
            model.setTypeName(source.readString());
            model.setTypeSeries(source.readString());

            return model;
        }

        @Override
        public CarModel[] newArray(int size) {
            return new CarModel[size];
        }
    };
}
