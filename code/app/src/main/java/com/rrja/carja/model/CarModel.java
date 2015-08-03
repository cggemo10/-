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
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class CarModel implements Parcelable{

    private String engineCapacity;
    private String id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        dest.writeString(id);
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
            model.setId(source.readString());
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

    public static CarModel parse(JSONObject modelJson) throws JSONException{
        if (modelJson == null || TextUtils.isEmpty(modelJson.toString())) {
            return null;
        }

        CarModel info = new CarModel();

        info.setEngineCapacity(modelJson.getString("engineCapacity"));
        info.setId(modelJson.getInt("id") + "");
        info.setSeriesId(modelJson.getInt("seriesId"));
        info.setSeriesName(modelJson.getString("seriesName"));
        info.setTransmission(modelJson.getString("transmission"));
        info.setTypeName(modelJson.getString("typeName"));
        info.setTypeSeries(modelJson.getString("typeSeries"));

        return info;
    }
}
